package Chapter7
import java.util.concurrent._


/*
 * def map2[A,B,C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C]
 */


object Par {
  type Par[A] = ExecutorService => Future[A]

  def run[A](s: ExecutorService)(a: Par[A]): Future[A] = a(s)
  def unit[A](a: A): Par[A] = (es: ExecutorService) => UnitFuture(a)

  private case class UnitFuture[A](get: A) extends Future[A] {
    def isDone = true
    def get(timeout: Long, units: TimeUnit) = get
    def isCancelled = false
    def cancel(evenIfRunning: Boolean): Boolean = false
  }

  private case class Map2Future[A,B,C](a: Future[A], b: Future[B],
                                       f: (A,B) => C) extends Future[C] {
    @volatile var cache: Option[C] = None
    def isDone = cache.isDefined
    def isCancelled = a.isCancelled || b.isCancelled
    def cancel(evenIfRunning: Boolean) =
      a.cancel(evenIfRunning) || b.cancel(evenIfRunning)
    def get = compute(Long.MaxValue)
    def get(timeout: Long, units: TimeUnit): C = 
      compute(TimeUnit.NANOSECONDS.convert(timeout, units))

    private def compute(timeoutInNanos: Long): C = cache match {
      case Some(c) => c
      case None =>
        val start = System.nanoTime
        val ar = a.get(timeoutInNanos, TimeUnit.NANOSECONDS)
        val stop = System.nanoTime
        val aTime = stop - start
        val br = b.get(timeoutInNanos - aTime, TimeUnit.NANOSECONDS)
        val ret = f(ar, br)
        cache = Some(ret)
        ret
    }
  }

  def map2[A,B,C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] =
    (es: ExecutorService) => {
      val af: Future[A] = a(es)
      val bf: Future[B] = b(es)
      Map2Future(af, bf, f)
    }

  def map3[A,B,C,D](a: Par[A], b: Par[B], c: Par[C])(f: (A,B,C) => D): Par[D] =
    map2(map2(a,b)((aa, bb) => f(aa, bb, _:C)), c)((cd, c) => cd(c))

  def map4[A,B,C,D,E](a: Par[A], b: Par[B],
                      c: Par[C], d: Par[D])(f: (A,B,C,D) => E): Par[E] =
    map2(map3(a,b,c)((aa, bb, cc) => f(aa, bb, cc, _:D)), d)((de, d) => de(d))

  def map5[A,B,C,D,E,F](a: Par[A], b: Par[B], c: Par[C],
                        d: Par[D], e: Par[E])(f: (A,B,C,D,E) => F): Par[F] =
    map2(map4(a,b,c,d)((aa,bb,cc,dd) => f(aa,bb,cc,dd,_:E)), e)((ef, e) => ef(e))

  def fork[A](a: => Par[A]): Par[A] =
    es => es.submit(new Callable[A] {
      def call = a(es).get
    })

  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))
  def asyncF[A,B](f: A => B): A => Par[B] = (a: A) => lazyUnit(f(a))

  def map[A,B](pa: Par[A])(f: A => B): Par[B] = 
    map2(pa, unit(()))((a, _) => f(a))

  def sortPar(parList: Par[List[Int]]) = map(parList)(_.sorted)

  def sequence_simple[A](ps: List[Par[A]]): Par[List[A]] = {
    ps.foldLeft(unit[List[A]](List()))((pl, p) => map2(p, pl)(_ :: _))
  }
  def sequenceBalanced[A](as: IndexedSeq[Par[A]]): Par[IndexedSeq[A]] = fork {
    if (as.isEmpty) unit(Vector())
    else if (as.length == 1) map(as.head)(a => Vector(a))
    else {
      val (l, r) = as.splitAt(as.length / 2)
      map2(sequenceBalanced(l), sequenceBalanced(r))(_ ++ _)
    }
  }
  def sequence[A](as: List[Par[A]]): Par[List[A]] = {
    map(sequenceBalanced(as.toIndexedSeq))(_.toList)
  }
  def parMap[A,B](ps: List[A])(f: A => B): Par[List[B]] = fork {
    val fbs: List[Par[B]] = ps.map(asyncF(f))
    sequence(fbs)
  }

  /*
  def sequenceBalancedFilter[A](as: IndexedSeq[Par[(A, Boolean)]]):
    Par[IndexedSeq[A]] = fork {
      if (as.isEmpty) unit(Vector())
      else if (as.length == 1) map(as.head)(a => a._2 match {
        case true => Vector(a._1)
        case false => Vector()
      })
      else {
        val (l, r) = as.splitAt(as.length / 2)
        map2(sequenceBalancedFilter(l), sequenceBalancedFilter(r))(_ ++ _)
      }
    }
  def sequenceFilter[A](as: List[Par[(A, Boolean)]]): Par[List[A]] = {
    map(sequenceBalancedFilter(as.toIndexedSeq))(_.toList)
  }
  def parFilter[A](as: List[A])(f: A => Boolean): Par[List[A]] = fork {
    val fbs: List[Par[(A, Boolean)]] = as.map(asyncF(a => (a, f(a))))
    sequenceFilter(fbs)
  }
  */

  def parFilter[A](as: List[A])(f: A => Boolean): Par[List[A]] = fork {
    val fbs: List[Par[List[A]]] = as.map(asyncF(a => f(a) match {
      case true => List(a)
      case false => List()
    }))
    // sequence(fbs): Par[List[List[A]]
    map(sequence(fbs))(_.flatten)
  }

  def equal[A](e: ExecutorService)(p: Par[A], p2: Par[A]): Boolean =
    p(e).get == p2(e).get

  /*
  def choice[A](cond: Par[Boolean])(t: Par[A], f: Par[A]): Par[A] = 
    es => {
      if (run(es)(cond).get) t(es)
      else f(es)
    }
  */

  def choiceN[A](n: Par[Int])(choices: List[Par[A]]): Par[A] =
    es => {
      val m = run(es)(n).get
      choices(m)(es)
    }

  def choice[A](cond: Par[Boolean])(p: Par[A], p2: Par[A]): Par[A] =
    choiceN(map(cond)(b => if (b) 0 else 1))(List(p, p2))

  def choiceMap[K,V](key: Par[K])(choices: Map[K, Par[V]]): Par[V] =
    es => {
      val k = run(es)(key).get
      choices(k)(es)
    }

  def chooser[A,B](pa: Par[A])(choices: A => Par[B]): Par[B] =
    es => {
      val a = pa(es).get
      choices(a)(es)
    }

  def choiceN2[A](n: Par[Int])(choices: List[Par[A]]): Par[A] = 
    chooser(n)(n => choices(n))

  def choice2[A](cond: Par[Boolean])(p: Par[A], p2: Par[A]): Par[A] =
    chooser(cond)(b => if (b) p else p2)

  def flatMap[A,B](a: Par[A])(choices: A => Par[B]): Par[B] =
    es => {
      val k: A = a(es).get()
      choices(k)(es)
    }

  def join[A](a: Par[Par[A]]): Par[A] =
    es => {
      val pa: Par[A] = a(es).get()
      pa(es)
    }

  def flatMapViaJoin[A,B](a: Par[A])(choices: A => Par[B]): Par[B] =
    join(map(a)(choices))

  def joinViaFlatMap[A](a: Par[Par[A]]): Par[A] = 
    flatMap(a)(pa => pa)
}

object Test {
  import Par._

  def sum(ints: IndexedSeq[Int]): Par[Int] = Par.fork {
    if (ints.length <= 1)
      Par.unit(ints.headOption getOrElse 0)
    else {
      val (l, r) = ints.splitAt(ints.length/2)
      Par.map2(sum(l), sum(r))(_ + _)
    }
  }

  def reduce(ints: IndexedSeq[Int])(init: Int, f: (Int, Int) => Int): Par[Int] =
    Par.fork {
      if (ints.length <= 1)
        Par.unit(ints.headOption getOrElse init)
      else {
        val (l, r) = ints.splitAt(ints.length/2)
        Par.map2(reduce(l)(init, f), reduce(r)(init, f))(f)
      }
    }

  def sum2(ints: IndexedSeq[Int]): Par[Int] = reduce(ints)(0, _ + _)
  def max(ints: IndexedSeq[Int]): Par[Int] =
    reduce(ints)(Int.MinValue, (a, b) => if (a > b) a else b)

  def wordCount(p: List[String]): Par[Int] = {
    def count(p: IndexedSeq[String]): Par[Int] = Par.fork {
      if (p.length <= 1) {
        Par.unit(p.headOption match {
          case Some(s) => s.split("[ ,!.]+").length
          case None => 0
        })
      } else {
        val (l, r) = p.splitAt(p.length/2)
        Par.map2(count(l), count(r))(_ + _)
      }
    }
    count(p.toIndexedSeq)
  }

  def delay[A](fa: => Par[A]): Par[A] =
    es => fa(es)

  def main(args: Array[String]): Unit = {
    val i = List(1,2,3,4,5,6,7,8,9,10)
    // val es = Executors.newFixedThreadPool(2)

    val a = Par.lazyUnit({
      println("hi")
      42 + 1
    })
    val S = Executors.newFixedThreadPool(1)
    a(S)
    S.shutdown()
    // println(Par.equal(S)(a, Par.fork(a)))    // Deadlock!
    // println(run(es)(sum(i.toIndexedSeq)).get())
    // println(run(es)(Par.lazyUnit(i)).get)
  }
}
