package Chapter8

import Chapter6.state._
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

object Prop {
  type FailedCase = String
  type SuccessCount = Int
}

trait Prop {
  def check: Either[(FailedCase, SuccessCount), SuccessCount]
  def forAll[A](a: Gen[A])(f: A => Boolean): Prop
}
