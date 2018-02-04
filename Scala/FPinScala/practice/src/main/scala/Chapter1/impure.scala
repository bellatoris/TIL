/*
* 커피를 구매하는 과정을 처리하는 프로그램.
* 신용카드의 청구에는 외부 세계와의 일정한 상호작용이 관여한다.
* 웹 서비스를 통해서 신용카드 회사와 접촉해서 거래를 승인하고, 
* 대금을 청구하고, 이후 참조를 위해 거래 기록을 영구적으로 
* 기록하는 등의 작업이 필요하다. 
*/

// Worst example
/*
class Cafe {
  def buyCoffee(cc: CreditCard): Coffee = {
    val cup = new Coffee()

    cc.charge(cup.price)

    cup
  }
}

class Cafe {
  def buyCoffee(cc: CreditCard, p: Payments): Coffee = {
    val cup = new Coffee()
    p.charge(cc, cup.price)
    cup
  }
}

// pure version
class Cafe {
  def buyCoffee(cc: CreditCard): (Coffee, Charge) = {
    val cup = new Coffee()
    (cup, Charge(cc, cup.price))
  }
}

case class Charge(cc: CreditCard, amount: Double) {
  def combine(other: Charge): Charge =
    if (cc == other.cc)
      Charge(cc, amount + other.amount)
    else
      throw new Exception("Can't combine charges to different cards")
}


class Cafe {
  def buyCoffee(cc: CreditCard): (Coffee, Charge) = ...

  def buyCoffees(cc: CreditCard, n: Int): (List[Coffee], Charge) = {
    val purchases: List[(Coffee, Charge)] = List.fill(n)(buyCoffee(cc))
    val (coffees, charges) = purchases.unzip
    (coffess, charges.reduce((c1, c2) => c1.combine(c2))
  }

  def coalesce(charges: List[Charge]): List[Charge] =
    charges.groupBy(_.cc).values.map(_.reduce(_ combine _)).toList
}
*/
