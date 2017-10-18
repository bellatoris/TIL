package example.semi

import cats.Semigroup
import cats.implicits._

object Semi {
  def semigroup = {
    Semigroup[Int].combine(1, 2)
    Semigroup[List[Int]].combine(List(1, 2, 3), List(4, 5, 6))
    Semigroup[Option[Int]].combine(Option(1), Option(2))
    Semigroup[Option[Int]].combine(Option(1), None)
    Semigroup[Int ⇒ Int]
      .combine({ (x: Int) ⇒
        x + 1
      }, { (x: Int) ⇒
        x * 10
      })
      .apply(6)
  }
}
