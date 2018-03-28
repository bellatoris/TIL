package Chapter5


/*
def if2[A](cond: Boolean, onTrue: => A, onFalse: => A): A = 
  if (cond) onTrue else onFalse
*/

sealed trait Stream[+A] {
  def headOption: Option[A] = this match {
    case Empty => None
    case Cons(h, t) => Some(h())
  }

  def toList: List[A] = this match {
    case Empty => Nil
    case Cons(h, t) => h() :: t().toList
  }

  def take(n: Int): Stream[A] = this match {
    case Cons(h, t) if n > 0 => Stream.cons(h(), t().take(n - 1))
    case _ => Stream.empty
  }

  def drop(n: Int): Stream[A] = this match {
    case Cons(h, t) if n > 0 => t().drop(n - 1)
    case _ => this
  }

  def takeWhile(p: A => Boolean): Stream[A] = this match {
    case Cons(h, t) if p(h()) => Stream.cons(h(), t().takeWhile(p))
    case _ => Stream.empty
  }

  def exists(p: A => Boolean): Boolean = this match {
    case Cons(h, t) => p(h()) || t().exists(p)
    case _ => false
  }

  def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
    case Cons(h, t) => f(h(), t().foldRight(z)(f))
    case _ => z
  }

  def existsViaFoldRight(p: A => Boolean): Boolean = 
    foldRight(false)((a, b) => p(a) || b)

  def forAll(p: A => Boolean): Boolean =
    foldRight(true)((a, b) => p(a) && b)

  def takeWhileViaFoldRight(p: A => Boolean): Stream[A] = 
    foldRight(Empty: Stream[A])((a, b) => if (p(a)) Stream.cons(a, b) else Stream.empty)

  def headOptionViaFoldRight: Option[A] =
    foldRight(None: Option[A])((a, b) => Some(a))

  def map[B](f: A => B): Stream[B] =
    foldRight(Empty: Stream[B])((a, b) => Stream.cons(f(a), b))

  def filter(f: A => Boolean): Stream[A] =
    foldRight(Empty: Stream[A])((a, b) => if (f(a)) Stream.cons(a, b) else b)

  def append[B>:A](s: Stream[B]): Stream[B] =
    foldRight(s: Stream[B])((a, b) => Stream.cons(a, b))

  def flatMap[B](f: A => Stream[B]): Stream[B] = 
    foldRight(Empty: Stream[B])((a, b) => f(a).append(b))

  def find(p: A => Boolean): Option[A] =
    filter(p).headOption

  def mapViaUnfold[B](f: A => B): Stream[B] =
    Stream.unfold(this)(s => s match {
      case Cons(h, t) => Some(f(h()), t())
      case _ => None
    })

  def takeViaUnfold(n: Int): Stream[A] =
    Stream.unfold((this, n))(p => p match {
      case (Cons(h, t), n) if n > 0 => Some((h(), (t(), n - 1)))
      case _ => None
    })

  def takeWhileViaUnfold(p: A => Boolean): Stream[A] =
    Stream.unfold(this)(s => s match {
      case Cons(h, t) if p(h()) => Some((h(), t()))
      case _ => None
    })

  def zipWith[B,C](as2: Stream[B])(f: (A, B) => C): Stream[C] =
    Stream.unfold((this, as2))(p => p match {
      case (Cons(h1, t1), Cons(h2, t2)) => Some((f(h1(), h2()), (t1(), t2())))
      case _ => None
    })

  // special case of `zipWith`
  def zip[B](s2: Stream[B]): Stream[(A,B)] =
    zipWith(s2)((_,_))

  def zipAll[B](s2: Stream[B]): Stream[(Option[A], Option[B])] = 
    Stream.unfold((this, s2))(p => p match {
      case (Cons(h1, t1), Cons(h2, t2)) => Some(((Some(h1()), Some(h2())), (t1(), t2())))
      case (Cons(h1, t1), _) => Some(((Some(h1()), None), (t1(), Stream.empty)))
      case (_, Cons(h2, t2)) => Some(((None, Some(h2())), (Stream.empty, t2())))
      case _ => None
    })

  def hasSubsequence[A](sub: Stream[A]): Boolean = {
    Stream.unfold(this)(s => s match {
      case Cons(h, t) =>
        if (startsWith2(s)) Some((true, Stream.empty))
        else Some((false, t()))
      case _ => None}).foldRight(false)(_ || _)
  }

  def startsWith[A](s: Stream[A]): Boolean = {
    Stream.unfold(this, s)(p => p match {
      case (Cons(h1, t1), Cons(h2, t2)) => (h1() == h2()) match {
        case true => Some((true, (t1(), t2())))
        case false => Some((false, (Stream.empty, Stream.empty)))
      }
      case (Cons(h1, t1), _) =>
        Some((true, (Stream.empty, Stream.empty)))
      case (_, Cons(h2, t2)) =>
        Some((false, (Stream.empty, Stream.empty)))
      case _ => None
    }).foldRight(true)(_ && _)
  }

  def startsWith2[A](s: Stream[A]): Boolean =
    zipAll(s).takeWhile(!_._2.isEmpty) forAll {
      case(h, h2) => h == h2
    }

  def tails: Stream[Stream[A]] = {
    Stream.unfold(this)(s => s match {
      case Cons(h, t) => Some((t(), t()))
      case _ => None
    }) append Stream(Stream.empty)
  }

  def hasSubsequnce2[A](s: Stream[A]): Boolean =
    tails exists (_ startsWith s)

  def scanRight[B](z: => B)(f: (A, => B) => B): Stream[B] =
    this match {
      case Cons(h, t) => {
        println("scanRight", h())
        lazy val tail = t().scanRight(z)(f)
        Stream.cons(f(h(), tail.headOption.get), tail)
      }
      case _ => Stream(z)
    }

  def scanRight2[B](z: => B)(f: (A, => B) => B): Stream[B] =
    foldRight((z, Stream(z)))((a, p) => {
      // p0 is passed by-name and used in by-name args in f and cons. 
      // So use lazy val to ensure only one evaluation...
      lazy val p1 = p
      val b2 = f(a, p1._1)
      (b2, Stream.cons(b2, p1._2))
    })._2
}


case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    // Why caching?
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty else cons(as.head, apply(as.tail: _*))

  def ones: Stream[Int] =
    cons(1, ones)

  def constant[A](a: A): Stream[A] =
    cons(a, constant(a))

  def from(n: Int): Stream[Int] =
    cons(n, from(n + 1))

  def fib: Stream[Int] = {
    def nested(state: (Int, Int)): Stream[Int] = {
      val first = state._1
      val second = state._2

      cons(first, nested((second, first + second)))
    }
    nested((0, 1))
  }

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = f(z) match {
    case None => empty
    case Some((a, s)) => cons(a, unfold(s)(f))
  }

  def fibsViaUnfold: Stream[Int] = unfold((0, 1))(p => Some(p._1, (p._2, p._1 + p._2)))
  def fromViaUnfold(n: Int): Stream[Int] = unfold(n)(n => Some(n, n + 1))
  def constantViaUnfold[A](a: A): Stream[A] = unfold(a)(a => Some(a, a))
  def onesViaUnfold: Stream[Int] = unfold(1)(n => Some(1, 1))
}

object Test { 
  def expensive(x: Int): Int = {
    println("expensive")
    2 * x
  }


  def main(args: Array[String]): Unit = {
    /*
    val a = 3
    if2(a < 22,
      println("a"),
      println("b")
    )
    */

    val tl = Stream(1,2,3,4,5)

    val x = Cons(() => expensive(3), () => tl)
    val h1 = x.headOption
    val h2 = x.headOption

    val x2 = Stream.cons(expensive(3), tl)
    val h3 = x2.headOption
    val h4 = x2.headOption

    println(tl.toList)
    println(tl.take(3).toList)
    println(tl.drop(3).toList)

    println(tl.takeWhile(a => a < 3).toList)

    println(tl.headOptionViaFoldRight)
    println(tl.map(x => x).toList)
    println(tl.append(tl).toList)

    println(Stream(1,2,3,4).map(_ + 10).filter(_ % 2 == 0) match {
      case Cons(h, t) => h()
      case _ => ""
    })

    def ones: Stream[Int] = Stream.cons(1, ones)
    println(ones.take(5).toList)
    println(ones.exists(_ % 2 != 0))
    println(ones.map(_ + 1).exists(_ % 2 == 0))
    println(ones.takeWhile(_ == 1))
    println(ones.forAll(_ != 1))

    println(Stream.constant(4).take(5).toList)
    println(Stream.from(4).take(5).toList)

    println(Stream.fib.take(10).toList)
    println(Stream.unfold((0, 1))(p => Some((p._1, (p._2, p._1 + p._2)))).take(10).toList)
    println(Stream.fibsViaUnfold.take(10).toList)
    println(Stream.fromViaUnfold(4).take(5).toList)
    println(Stream.constantViaUnfold(3).take(5).toList)
    println(Stream.onesViaUnfold.take(5).toList)
    println(Stream.onesViaUnfold.map(n => n + 4).take(5).toList)
    println(Stream.ones.map(n => n + 4).take(5).toList)
    println(Stream.ones.mapViaUnfold(n => n + 4).take(5).toList)
    println(Stream.ones.mapViaUnfold(n => n + 4).takeViaUnfold(5).toList)
    println(Stream(1,2,3,4,1,1,1,1).hasSubsequence(Stream(1,1,1,1)))
    println(Stream(1,2,3,4,1,1,1,1).startsWith(Stream(1,1,1,1)))
    println(Stream(1,2,3,4,1,1,1,1).startsWith(Stream(1,2,3,4)))
    println(Stream(1,2,3).scanRight(0)(_ + _).toList)
  }
}
