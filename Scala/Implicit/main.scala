object Main {
  def main(args: Array[String]): Unit = {
    implicit class StringImprovements(s: String) {
      def increment = s.map(c => (c + 1).toChar)
      def hi: Boolean = true
    }

    implicit def increment2(s: String): String = {
      s.map(c => (c + 1).toChar)
    }

    class StringImprovements2(s: String) {
      def increment3 = s.map(c => (c + 1).toChar)
    }

    implicit def conversion(s: String) = new StringImprovements2(s)

    implicit class StringImprovements3(s: String) {
      // def increment = s.map(c => (c + 2).toChar)   // make error
      def hi: Int = 3
    }

    val result = "HAL".increment
    println(result)   // IBM
    // println("HAL".increment2)   // Doesn't work
    println("HAL".increment3)


    val needToBool: Boolean = result.hi
    // needToInt(result.hi)
  }
}
