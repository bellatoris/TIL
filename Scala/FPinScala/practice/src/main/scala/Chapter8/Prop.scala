package Chapter8

import Chapter5._
import Chapter6._
import Prop._
import Gen._


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
// trait Prop {
//   def check: Either[(FailedCase, SuccessCount), SuccessCount]
//   def forAll[A](a: Gen[A])(f: A => Boolean): Prop
// }
sealed trait Result {
  def isFalsified: Boolean
}
case object Passed extends Result {
  def isFalsified = false
}
case class Falsified(failure: FailedCase,
                     successes: SuccessCount) extends Result {
  def isFalsified = true
}


case class Prop(run: (TestCases, RNG) => Result) {
  // exercise 8.9
  def &&(p: Prop): Prop = Prop((testCases, rng) => run(testCases, rng) match {
      case Passed => p.run(testCases, rng)
      case fail => fail
    }
  )
  def ||(p: Prop): Prop = Prop((testCases, rng) => run(testCases, rng) match {
      case Passed => Passed
      case Falsified(failure, successes) => p.tag(failure).run(testCases, rng)
    }
  )
  /* This is rather simplistic - in the event of failure, we simply prepend
   * the given message on a new line in front of the existing message.
   */
  def tag(msg: String) = Prop {
    (testCases, rng) => run(testCases, rng) match {
      case Passed => Passed
      case Falsified(failure, successes) => Falsified(s"${msg}\n${failure}",
                                                      successes)
    }
  }
}

object Prop {
  type FailedCase = String
  type SuccessCount = Int
  type TestCases = Int

  def forAll[A](as: Gen[A])(f: A => Boolean): Prop = Prop {
    (n, rng) => randomStream(as)(rng).zip(Stream.from(0)).take(n).map {
      case (a, i) => try {
        if (f(a)) Passed else Falsified(a.toString, 1)
      } catch { 
        case e: Exception => Falsified(buildMsg(a, e), i)
      }
    }.find(_.isFalsified).getOrElse(Passed)
  }

  def randomStream[A](g: Gen[A])(rng: RNG): Stream[A] = {
    Stream.unfold(rng)(rng => Some(g.sample.run(rng)))
  }
  def buildMsg[A](s: A, e: Exception): String = {
    s"test case: $s\n" +
    s"generated an exception: ${e.getMessage}\n" +
    s"stack trace:\n ${e.getStackTrace.mkString("\n")}"
  }
}
