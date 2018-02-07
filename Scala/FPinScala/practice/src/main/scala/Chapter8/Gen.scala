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
    Gen[Int](State(rng => {
      val range = stopExclusive - start
      val (i, nextRng) = rng.nextInt
      (i, nextRng)
    }))
  }

  // exercise 8.5
  def unit[A](a: => A): Gen[A] = Gen[A](State(rng => (a, rng)))

  def boolean: Gen[Boolean] = Gen[Boolean](State(rng => {
    val (i, nextRng) = rng.nextInt
    (i % 2 == 0, nextRng)
  }))

  def listOfN[A](n: Int, g: Gen[A]): Gen[List[A]] = Gen[List[A]](State(rng => {
    val (a, nextRng) = g.sample.run(rng)
    (List.fill(n)(a), nextRng)
  }))
}
