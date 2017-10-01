sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tali: List[A]) extends List[A]


object List {
  def sum(ints: List[Int]): Int = ints match {
    case Nil => 0
    case Cons(x, xs) => x + sum(xs)
  }

  def product(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x, xs) => x * product(xs)
  }

  def apply[A](as: A*): List[A] =
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

  def tail[A](l: List[A]): List[A] = l match {
    case Nil => Nil
    case Cons(x, xs) => xs
  }

  def setHead[A](h: A, l: List[A]): List[A] = l match {
    case Nil => Cons(h, Nil)
    case Cons(x, xs) => Cons(h, xs)
  }

  def drop[A](l: List[A], n: Int): List[A] = {
    if (n <= 0) l
    else l match {
      case Nil => Nil
      case Cons(x, xs) => drop(xs, n - 1)
    }
  }

  def dropWhile[A](l: List[A], f: A => Boolean): List[A] = l match {
    case Nil => Nil
    case Cons(x, xs) => {
      if (f(x)) dropWhile(xs, f)
      else l
    }
  }

  // 형식 추론은 왼쪽에서 오른쪽으로 흘러들어가기 때문에 
  // as 에 List[Int] 가 들어가면 f: Int => Boolean 이 들어와야 
  // 한다고 추론이 가능해진다.
  def dropWhile2[A](as: List[A])(f: A => Boolean): List[A] = 
    as match {
      case Cons(h, t) if f(h) => dropWhile2(t)(f)
      case _ => as
    }

  def append[A](a1: List[A], a2: List[A]): List[A] =
    a1 match {
      case Nil => a2
      case Cons(h, t) => Cons(h, append(t, a2))
    }

  def init[A](l: List[A]): List[A] = l match {
    case Nil => Nil
    case Cons(x, Nil) => Nil
    case Cons(x, xs) => Cons(x, init(xs))
  }

  def foldRight[A,B](as: List[A], z: B)(f: (A, B) => B): B =
    as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

  def sum2(ns: List[Int]): Int = foldRight(ns, 0)(_ + _)
  def product2(ns: List[Double]): Double = foldRight(ns, 1.0)(_ * _)
  def length[A](as: List[A]): Int = foldRight(as, 0)((a: A, n: Int) => n + 1)

  def foldLeft[A,B](as: List[A], z: B)(f: (B, A) => B): B = {
    @annotation.tailrec
    def go(as: List[A], acc: B): B = as match {
      case Nil => acc
      case Cons(x, xs) => go(xs, f(acc, x))
    }

    go(as, z)
  }

  def sum3(ns: List[Int]): Int = foldLeft(ns, 0)(_ + _)
  def product3(ns: List[Double]): Double = foldLeft(ns, 1.0)(_ * _)
  def length2[A](as: List[A]): Int = foldLeft(as, 0)((n: Int, a: A) => n + 1)

  def reverse[A](as: List[A]): List[A] = {
    @annotation.tailrec
    def go(as: List[A], rev: List[A]): List[A] = as match {
      case Nil => rev
      case Cons(x, xs) => go(xs, Cons(x, rev))
    }

    go(as, Nil)
  }

  def reverse2[A](as: List[A]): List[A] =
    foldRight(as, Nil: List[A])((a, y) => List.append(y, Cons(a, Nil)))

  def reverse3[A](as: List[A]): List[A] =
    foldLeft(as, Nil: List[A])((y, a) => Cons(a, y))

  def foldRight2[A,B](as: List[A], z: B)(f: (A, B) => B): B = {
    foldLeft(reverse(as), z)((b, a) => f(a, b))
  }

  def foldLeft2[A,B](as: List[A], z: B)(f: (B, A) => B): B = {
    foldRight(reverse(as), z)((a, b) => f(b, a))
  }

  def foldLeftVidFoldRight[A, B](as: List[A], z: B)(f: (B, A) => B): B = {
    foldRight(as, (b: B) => b)((a, g) => ((b: B) => g(f(b, a))))(z)
  }
}


object Main {
  def main(args: Array[String]): Unit = {
    val x = List(1, 2, 3, 4, 5) match {
      case Cons(x, Cons(2, Cons(4, _))) => x
      case Nil => 42
      case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
      case Cons(h, t) => h + List.sum(t)
      case _ => 101
    }
    println(x)

    val a = List.dropWhile[Int](List(1,2,3,4,5), (x: Int) => x <= 3)
    println(a)
    println(List.drop[Int](List(1,2,3,4,5), 3))
    println(List.init[Int](List(1,2,3,4)))


    val xs: List[Int] = List(1,2,3,4,5)
    val ex1 = List.dropWhile(xs, (x: Int) => x < 4)
    println(ex1)

    val ex2 = List.dropWhile2(xs)(x => x < 4)
    println(ex2)

    println(List.sum2(xs))
    println(List.product2(List(1.0, 2.0, 0.0, 4.0, 5.0)))

    println(List.foldRight(List(1,2,3), Nil:List[Int])(Cons(_,_)))
    println(List.length(xs))
    println(List.foldLeft(List(1,2,3), 0)(_ + _))
    println(List.product3(List(1.0, 2.0)))
    println(List.reverse(List(1,2,3,4,5)))
    println(List.reverse2(List(1,2,3,4,5)))
    println(List.reverse3(List(1,2,3,4,5)))
  }
}
