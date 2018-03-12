package Chapter8

import Chapter5.{Stream, Cons, Empty}
import Chapter6._
import Chapter7._
import Chapter7.Par.Par
import java.util.concurrent.{Executors,ExecutorService}
import Prop._

/*
{
  // exercise 8.3
  trait Prop {
    def check: Boolean
    def &&(p: Prop): Prop = new Prop {
      def check = Prop.this.check && p.check
    }
  }
}
*/

trait Prop {
  def check: Either[(FailedCase, SuccessCount), SuccessCount]
  def forAll[A](a: Gen[A])(f: A => Boolean): Prop
}

object Prop {
  type FailedCase = String
  type SuccessCount = Int
}

case class Gen[A](sample: State[RNG, A]) {
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
}
