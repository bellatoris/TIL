package Chapter8

import Chapter6._
import Gen._


object Main {
  def main(args: Array[String]): Unit = {
    val simpleRng = SimpleRNG(0)
    val range2Int = choose2Int(0, 10)

    println(range2Int.sample.run(simpleRng))
    println(nextString(10).sample.run(simpleRng))
  }
}
