package Chapter3


sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tali: List[A]) extends List[A]


object List {
  def makeList(n: Int): List[Int] = {
    def go(n: Int, acc: List[Int]): List[Int] = n match {
      case 0 => acc
      case n => go(n - 1, Cons(n, acc))
    }

    go(n, Nil)
  }

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

  def foldLeftViaFoldRight[A, B](as: List[A], z: B)(f: (B, A) => B): B = {
    foldRight(as, (b: B) => b)((a, g) => ((b: B) => g(f(b, a))))(z)
  }

  def foldRightViaFoldLeft[A, B](as: List[A], z: B)(f: (A, B) => B): B = {
    foldLeft(as, (b: B) => b)((g, a) => ((b: B) => g(f(a, b))))(z)
  }

  def append2[A](l1: List[A], l2: List[A]): List[A] = {
    foldRightViaFoldLeft(l1, l2)((a, as) => Cons(a, as))
  }

  def append3[A](l1: List[A], l2: List[A]): List[A] = {
    foldLeft(reverse(l1), l2)((as, a) => Cons(a, as))
  }

  def flatten[A](l: List[List[A]]): List[A] = {
    foldRightViaFoldLeft(l, Nil: List[A])((x, xs) => append(x, xs))
  }

  def concat[A](l: List[List[A]]): List[A] =
    foldRightViaFoldLeft(l, List[A]())(append(_,_))

  def addOne(ns: List[Int]): List[Int] = ns match {
    case Nil => Nil
    case Cons(x, xs) => Cons(x + 1, addOne(xs))
  }

  def doubleToString(ds: List[Double]): List[String] = ds match {
    case Nil => Nil
    case Cons(x, xs) => Cons(x.toString, doubleToString(xs))
  }

  def map[A, B](as: List[A])(f: A => B): List[B] = as match {
    case Nil => Nil
    case Cons(a, as) => Cons(f(a), map(as)(f))
  }

  def filter[A](as: List[A])(f: A => Boolean): List[A] = as match {
    case Nil => Nil
    case Cons(a, as) => if (f(a)) Cons(a, filter(as)(f)) else filter(as)(f)
  }

  def flatMap[A, B](as: List[A])(f: A => List[B]): List[B] = as match {
    case Nil => Nil
    case Cons(a, as) => append(f(a), flatMap(as)(f))
  }

  def filterViaFlatMap[A](as: List[A])(f: A => Boolean): List[A] =
    flatMap(as)(a => if (f(a)) List(a) else Nil)

  def addWith(ns1: List[Int], ns2: List[Int]): List[Int] = (ns1, ns2) match {
    case (Nil, _) => Nil
    case (_, Nil) => Nil
    case (Cons(x1, xs1), Cons(x2, xs2)) => Cons(x1 + x2, addWith(xs1, xs2))
  }

  def zipWith[A,B,C](as1: List[A], as2: List[B])(f: (A, B) => C): List[C] =
    (as1, as2) match {
    case (Nil, _) => Nil
    case (_, Nil) => Nil
    case (Cons(x1, xs1), Cons(x2, xs2)) => Cons(f(x1, x2), zipWith(xs1, xs2)(f))
  }

  def hasSubsequence[A](sup: List[A], sub: List[A]): Boolean = {
    def go(sup: List[A]): Boolean =
      foldLeft(zipWith(sup, sub)(_ == _), true)(_ && _)

    sup match {
      case Cons(a, as) if length(sup) >= length(sub) => {
        if (go(sup)) true else hasSubsequence(as, sub)
      }
      case _ => false
    }
  }
}

sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

object Tree {
  def size[A](t: Tree[A]): Int = t match {
    case Leaf(v) => 1
    case Branch(l, r) => size(l) + size(r) + 1
  }

  def maximum(t: Tree[Int]): Int = t match {
    case Leaf(v) => v
    case Branch(l, r) => maximum(l) max maximum(r)
  }

  def depth[A](t: Tree[A]): Int = t match {
    case Leaf(v) => 0
    case Branch(l, r) => (depth(l) max depth(r)) + 1
  }

  def map[A,B](t: Tree[A])(f: A => B): Tree[B] = t match {
    case Leaf(v) => Leaf(f(v))
    case Branch(l, r) => Branch(map(l)(f), map(r)(f))
  }

  def fold[A,B](t: Tree[A])(f: A => B)(g: (B, B) => B): B = t match {
    case Leaf(a) => f(a)
    case Branch(l, r) => g(fold(l)(f)(g), fold(r)(f)(g))
  }

  def sizeViaFold[A](t: Tree[A]): Int =
    fold(t)(a => 1)(_ + _ + 1)

  def maximumViaFold(t: Tree[Int]): Int =
    fold(t)(a => a)(_ max _)

  def depthViaFold[A](t: Tree[A]): Int =
    fold(t)(a => 0)((d1, d2) => (d1 max d2) + 1)

  def mapVidFold[A, B](t: Tree[A])(f: A => B): Tree[B] = 
    fold(t)(a => Leaf(f(a)): Tree[B])(Branch(_, _))
}


object Test {
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

    println(List.append2(List(1,2,3), List(12,24,36)))
    println(List.append3(List(1,2,3), List(12,24,36)))
    println(List.flatten(List(List(1,2,3), List(3,4,5), List(6,7,8))))

    println(List.addOne(List(1,2,3)))
    println(List.filter(List(1,2,3,4,5))(n => n % 2 == 0))
    println(List.flatMap(List(1,2,3))(i => List(i, i)))
    println(List.filterViaFlatMap(List(1,2,3,4,5))(n => n % 2 == 0))

    println(List.addWith(List(1,2,3), List(4,5,6)))

    println(List.hasSubsequence(List(1,2,3,4), List(1,2,4)))
    val longList= List.makeList(1000000)
    println(List.foldLeft(longList, 0)(_ + _))
    // println(List.foldRightViaFoldLeft(longList, 0)(_ + _))
  }
}
