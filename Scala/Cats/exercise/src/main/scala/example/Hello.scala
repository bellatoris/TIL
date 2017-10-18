package example

import example.semi.Semi

object Hello extends Greeting with App {
  Semi.semigroup
  println(greeting)
}

trait Greeting {
  lazy val greeting: String = "hello"
}
