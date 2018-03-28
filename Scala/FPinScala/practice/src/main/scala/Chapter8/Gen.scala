package Chapter8

import Chapter5.{Stream, Cons, Empty}
import Chapter6._
import Chapter7._
import Chapter7.Par.Par
import java.util.concurrent.{Executors,ExecutorService}
import Prop._


case class SGen[+A](forSize: Int => Gen[A]) {
  def flatMap[B](f: A => SGen[B]): SGen[B] = SGen[B] {
    (n: Int) => forSize(n).flatMap(a => f(a).forSize(n))
  }
  def apply(n: Int): Gen[A] = forSize(n)

  def map[B](f: A => B): SGen[B] = SGen[B] {
    (n: Int) => forSize(n) map f 
  }
}

case class Gen[+A](sample: State[RNG, A]) {
  // exercise 8.6
  def flatMap[B](f: A => Gen[B]): Gen[B] = {
    // val stateGenB: State[RNG, Gen[B]] = this.sample.map(f)
    // val stateB: State[RNG, Gen[B]] = stateGenB.flatMap(genB => genB.sample)
    // Gen(stateB)

    Gen(sample.map(f).flatMap(_.sample))
  }

  def listOfN(size: Gen[Int]): Gen[List[A]] = {
    size.flatMap(n => Gen.listOfN2(n, this))
  }

  // answer
  def flatMap2[B](f: A => Gen[B]): Gen[B] =
    Gen(sample.flatMap(a => f(a).sample))

  // exercise 8.10
  def unsized: SGen[A] = SGen[A](n => this)
  def map[B](f: A => B): Gen[B] = Gen[B](sample.map(f))
}

object Gen {
  // exercise 8.4
  def choose(start: Int, stopExclusive: Int): Gen[Int] = {
    assert(start < stopExclusive)
    val range = stopExclusive - start
    Gen[Int](Rand2.nonNegativeLessThan(range).map(i => start + i))
  }

  def choose2(start: Int, stopExclusive: Int): Gen[Int] = {
    assert(start < stopExclusive)
    val range = stopExclusive - start
    Gen[Int](State[RNG, Int](RNG.nonNegativeInt).map(n => start + n % range))
  }

  // exercise 8.5
  def unit[A](a: => A): Gen[A] = Gen[A](State(rng => (a, rng)))

  def boolean: Gen[Boolean] = Gen[Boolean](State(rng => {
    val (i, nextRng) = rng.nextInt
    (i % 2 == 0, nextRng)
  }))

  def listOfInts(n: Int): Gen[List[Int]] = {
    Gen[List[Int]](State[RNG, List[Int]](RNG.ints(n)))
  }

  def listOfN[A](n: Int, g: Gen[A]): Gen[List[A]] = {
    /*
    def nested(count: Int)(rng: RNG): (List[A], RNG) = count match {
      case c if c > 0 => {
        val (a, r) = g.sample.run(rng)
        val (al, r2) = nested(c - 1)(r)

        (a :: al, r2)
      }
      case _ => (Nil, rng)
    }
    */

    import scala.annotation.tailrec
    @tailrec
    def nested(count: Int, al: List[A])(rng: RNG): (List[A], RNG) = count match {
      case c if c > 0 => {
        val (a, r) = g.sample.run(rng)
        nested(c - 1, a :: al)(r)
      }
      case _ => (al, rng)
    }
    Gen[List[A]](State(nested(n, Nil)))
  }

  def listOfN2[A](n: Int, g: Gen[A]): Gen[List[A]] = {
    Gen(State.sequence(List.fill(n)(g.sample)))
  }

  // play
  def choose2Int(start: Int, stopExclusive: Int): Gen[(Int, Int)] = {
    val g: Gen[List[Int]] = listOfN2(2, choose(start, stopExclusive))
    Gen(g.sample.map((l: List[Int]) => (l(0), l(1))))
  }

  def toOption[A](g: Gen[A]): Gen[Option[A]] = {
    Gen(g.sample.map(a => Some(a)))
  }

  // def toA[A](g: Gen[Option[A]]): Gen[A] = {
  //   Gen(g.sample.map(option => option match {
  //     Some(a) => a
  //     None => {
  //       println("None is coming")
  //     }
  //   }))
  // }

  def nextChar(): Gen[Char] = {
    val low  = 33
    val high = 127
    Gen[Char](choose2(low, high).sample.map(i => i.toChar))
  }

  def nextString(n: Int): Gen[String] = {
    Gen[String](listOfN2(n, nextChar()).sample.map(l => l.mkString))
  }

  // exercise 8.7
  def union[A](g1: Gen[A], g2: Gen[A]): Gen[A] = {
    boolean.flatMap2(b => b match {
      case true => g1
      case false => g2
    })
  }

  // exercise 8.8
  def weighted[A](g1: (Gen[A], Double), g2: (Gen[A], Double)): Gen[A] = {
    val weight: Double = g1._2.abs / (g1._2.abs + g2._2.abs)
    Gen(State[RNG, Int](RNG.nonNegativeInt)).flatMap(n => {
      if (n / (Int.MaxValue.toDouble + 1) < weight) g1._1 else g2._1
    })
  }
}
