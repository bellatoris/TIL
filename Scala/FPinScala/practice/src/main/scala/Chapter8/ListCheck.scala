package Chapter8

object Test {
  import org.scalacheck.Gen
  import org.scalacheck.Prop.forAll

  val intList = Gen.listOf(Gen.choose(0, 100))
  val prop =
    forAll(intList)(ns => ns.reverse.reverse == ns) &&
    forAll(intList)(ns => ns.headOption == ns.reverse.lastOption)

  /**
   * exercise 8.1
   */
  val sumProp = 
    forAll(intList)(ns => ns.sum == ns.reverse.sum)

  /**
   * exercise 8.2
   */
  val maxProp = 
    forAll(intList)(ns => ns.max == ns.reverse.max)

  def main(args: Array[String]): Unit = {
    println(prop.check)
  }
}
