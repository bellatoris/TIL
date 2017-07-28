object Main {
  def main(args: Array[String]): Unit = {
    val foo = """This is
    a multiline
    String"""

    println("print foo", foo)

    val speech = """Four score and
    |seven years ago""".stripMargin

    println("print speech", speech)

    val oneLineSpeech = """Four score and
    |seven years ago
    |our fathers""".stripMargin.replaceAll("\n", " ")

    println("print oneLineSpeech", oneLineSpeech)

  }
}
