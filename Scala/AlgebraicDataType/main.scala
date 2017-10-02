object Main {
  def main(args: Array[String]): Unit = {
    class Tree () {}
    class Leaf(val value: Int) extends Tree
    class Node(val value: Int, val left: Tree, val right: Tree) extends Tree


    def findValueInTree(t: Tree, value: Int): Unit = t match {
      case l: Leaf => println(l.value)
      case l: Node => {
        println(l.value)
        findValueInTree(l.left, value)
        findValueInTree(l.right, value)
      }
    }

    findValueInTree(new Leaf(3), 4)
    findValueInTree(new Node(5, new Leaf(3), new Leaf(5)), 4)


    def hi[T](a: T) = a match {
      case s: String => println(s)
      case _ => println("None")
    }

    hi[Int](3)
    hi[String]("hi")
  }
}
