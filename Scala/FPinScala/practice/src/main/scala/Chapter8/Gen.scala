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
  def choose (start: Int, stopExclusive: Int): Gen[Int] = {
  }
}
