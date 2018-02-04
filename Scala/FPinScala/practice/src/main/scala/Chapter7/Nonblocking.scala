package Chapter7.Nonblocking

import Chapter7.Actor
import java.util.concurrent._


/*
 * def map2[A,B,C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C]
 */

sealed trait Future[A] {
  // k is a callback
  private[Chapter7] def apply(k: A => Unit): Unit
}

object Par {
  type Par[A] = ExecutorService => Future[A]

  def run[A](es: ExecutorService)(p: Par[A]): A = {
    val ref = new atomic.AtomicReference[A]
    val latch = new CountDownLatch(1)

    p(es) { a => ref.set(a); latch.countDown }

    latch.await
    ref.get
  }

  def unit[A](a: A): Par[A] =
    es => new Future[A] {
      def apply(cb: A => Unit): Unit =
        cb(a)
    }

  def eval(es: ExecutorService)(r: => Unit): Unit =
    es.submit(new Callable[Unit] { def call = r })

  def fork[A](a: => Par[A]): Par[A] =
    es => new Future[A] {
      def apply(cb: A => Unit): Unit =
        eval(es)(a(es)(cb))
    }

  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))
  def asyncF[A,B](f: A => B): A => Par[B] = (a: A) => lazyUnit(f(a))

  def map2[A,B,C](pa: Par[A], pb: Par[B])(f: (A, B) => C): Par[C] =
    es => new Future[C] {
      def apply(cb: C => Unit): Unit = {
        var ar: Option[A] = None
        var br: Option[B] = None

        val combiner = Actor[Either[A,B]](es) {
          case Left(a) => br match {
            case None => ar = Some(a)
            case Some(b) => eval(es)(cb(f(a, b)))
          }
          case Right(b) => ar match {
            case None => br = Some(b)
            case Some(a) => eval(es)(cb(f(a, b)))
          }
        }

        pa(es)(a => combiner ! Left(a))
        pb(es)(b => combiner ! Right(b))
      }
    }

  def map[A,B](pa: Par[A])(f: A => B): Par[B] = 
    map2(pa, unit(()))((a, _) => f(a))

  def sortPar(parList: Par[List[Int]]) = map(parList)(_.sorted)

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

  def parFilter[A](as: List[A])(f: A => Boolean): Par[List[A]] = fork {
    val fbs: List[Par[List[A]]] = as.map(asyncF(a => f(a) match {
      case true => List(a)
      case false => List()
    }))
    // sequence(fbs): Par[List[List[A]]
    map(sequence(fbs))(_.flatten)
  }

  def equal[A](e: ExecutorService)(p: Par[A], p2: Par[A]): Boolean =
    p(e) == p2(e)
}

object Test {
  import Par._

  def reduce(ints: IndexedSeq[Int])(init: Int, f: (Int, Int) => Int): Par[Int] =
    Par.fork {
      if (ints.length <= 1)
        Par.unit(ints.headOption getOrElse init)
      else {
        val (l, r) = ints.splitAt(ints.length/2)
        Par.map2(reduce(l)(init, f), reduce(r)(init, f))(f)
      }
    }

  def sum(ints: IndexedSeq[Int]): Par[Int] = reduce(ints)(0, _ + _)
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

  def main(args: Array[String]): Unit = {
    val p = Par.parMap(List.range(1, 10))(math.sqrt(_))
    val x = run(Executors.newFixedThreadPool(1))(p)
    println(x)
  }
}
