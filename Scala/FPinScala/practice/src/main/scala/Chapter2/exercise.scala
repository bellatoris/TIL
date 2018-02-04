package Chapter2
import scala.annotation.tailrec

object Exercise {
  def fib(n: Int): Int = {
    @tailrec
    def go(m: Int, first: Int, second: Int): Int = {
      if (m == n) first
      else go(m + 1, second, first + second)
    }

    go(0, 0, 1)
  }

  def isSorted[A](as: Array[A], ordered: (A, A) => Boolean): Boolean = {
    @tailrec
    def loop(n: Int): Boolean =
      if (n >= as.length - 1) true
      else if (ordered(as(n), as(n + 1))) loop(n + 1)
      else false

    loop(0)
  }

  def curry[A,B,C](f: (A, B) => C): A => (B => C) =
    (a: A) => (b: B) => f(a, b)

  def uncurry[A,B,C](f: A => B => C): (A, B) => C =
    (a: A, b: B) => f(a)(b)

  def compose[A,B,C](f: B => C, g: A => B): A => C =
    (a: A) => f(g(a))

  def main(args: Array[String]): Unit = {
    for (i <- 0 to 3) {
      println(fib(i))
    }
    val a = Array(1, 2, 3)
    println(isSorted[Int](a, (x, y) => x <= y))

    val f = (x: Double) => math.Pi / 2 - x
    val cos = f andThen math.sin
  }
}
