object Main {
  {
    // exercise 8.3
    trait Prop {
      def check: Boolean
      def &&(p: Prop): Prop = new Prop {
        def check = Prop.this.check && p.check
      }
    }
  }

  object Prop {
    type FailedCase = String
    type SuccessCount = Int
  }

  trait Prop {
    def check: Either[(FailedCase, SuccessCount), SuccessCount]
  }

  def main(args: Array[String]): Unit = {
  }
}
