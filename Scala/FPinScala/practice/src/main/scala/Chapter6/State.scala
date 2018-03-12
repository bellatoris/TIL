package Chapter6


trait RNG {
  def nextInt: (Int, RNG)
}

case class SimpleRNG(seed: Long) extends RNG {
  def nextInt: (Int, RNG) = {
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    val nextRNG = SimpleRNG(newSeed)
    val n = (newSeed >>> 16).toInt
    (n, nextRNG)
  }
}

object RNG {
  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val (i, r) = rng.nextInt
    (if (i < 0) -(i + 1) else i, r)
  }

  def double(rng: RNG): (Double, RNG) = {
    val (i, r) = nonNegativeInt(rng)
    (i / (Int.MaxValue.toDouble + 1), r)
  }

  def intDouble(rng: RNG): ((Int, Double), RNG) = {
    val (i, r) = rng.nextInt
    val (d, r2) = double(r)

    ((i ,d), r2)
  }

  def doubleInt(rng: RNG): ((Double, Int), RNG) = {
    val ((i, d), r) = intDouble(rng)
    ((d, i), r)
  }

  def double3(rng: RNG): ((Double, Double, Double), RNG) = {
    val (d, r) = double(rng)
    val (d2, r2) = double(r)
    val (d3, r3) = double(r2)

    ((d, d2, d3), r3)
  }

  def ints(count: Int)(rng: RNG): (List[Int], RNG) = count match {
    case c if c > 0 => {
      val (i, r) = rng.nextInt
      println(r)
      val (il, r2) = ints(c - 1)(r)

      (i :: il, r2)
    }
    case _ => (Nil, rng)
  }
}

object Rand {
  import RNG._

  type Rand[+A] = RNG => (A, RNG)
  // type Rand[+A] = State[RNG, A]

  def unit[A](a: A): Rand[A] = 
    rng => (a, rng)

  def map[A,B](s: Rand[A])(f: A => B): Rand[B] = 
    rng => {
      val (a, rng2) = s(rng)
      (f(a), rng2)
    }

  def nonNegativeEven: Rand[Int] =
    map(nonNegativeInt)(i => i - i % 2)

  def doubleViaMap: Rand[Double] =
    map(nonNegativeInt)(i => i / (Int.MaxValue.toDouble + 1))

  def map2[A,B,C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = {
    rng => {
      val (a, rng1) = ra(rng)
      val (b, rng2) = rb(rng1)
      (f(a,b), rng2)
    }
  }

  def both[A,B](ra: Rand[A], rb: Rand[B]): Rand[(A, B)] = {
    map2(ra, rb)((_, _))
  }

  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] = {
    rng => fs.foldLeft((List[A](), rng))((p, rand) => {
      val (a, r2) = rand(p._2)
      (a :: p._1, r2)
    })
  }

  def sequence2[A](fs: List[Rand[A]]): Rand[List[A]] =
    fs.foldRight(unit(List[A]()))((f, acc) => map2(f, acc)(_ :: _))

  def ints2(counts: Int): Rand[List[Int]] = sequence(List.fill(counts)(_.nextInt))

  def flatMap[A,B](f: Rand[A])(g: A => Rand[B]): Rand[B] = rng => {
    val (a, r) = f(rng)
    g(a)(r)
  }

  def nonNegativeLessThan(n: Int): Rand[Int] =
    flatMap(nonNegativeInt) { i => 
      val mod = i % n
      if (i + (n - 1) - mod >= 0) unit(mod)
      else nonNegativeLessThan(n)
    }

  def mapViaFlatMap[A,B](s: Rand[A])(f: A => B): Rand[B] = {
    flatMap(s) { a =>
      unit(f(a))
    }
  }
  def map2ViaFlatMap[A,B,C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = {
    flatMap(ra)(a=> map(rb)(f(a, _)))
  }
}

object Rand2 {
  import RNG._

  def nonNegativeLessThan(n: Int): State[RNG, Int] = {
    State[RNG, Int](nonNegativeInt).flatMap(i => {
      val mod = i % n
      if (i + (n - 1) - mod >= 0) State.unit(mod)
      else nonNegativeLessThan(n)
    })
  }
}

// type State[S,+A] = S => (A, S)
case class State[S,+A](run: S => (A,S)) {
  def flatMap[B](f: A => State[S,B]): State[S,B] = State(s => {
    val (a, s2) = run(s)
    f(a).run(s2)
  })
  def map[B](f: A => B): State[S,B] = flatMap(a => State.unit(f(a)))
  def map2[B,C](sb: State[S,B])(f: (A,B) => C): State[S,C] = {
    for {
      a <- this
      b <- sb
    } yield f(a, b)
  }
}

object State {
  def unit[S, A](a: A): State[S, A] = State(s => (a, s))
  def sequence[S, A](fs: List[State[S, A]]): State[S, List[A]] =
    fs.foldRight(unit[S, List[A]](List()))((s, acc) => s.map2(acc)(_ :: _))
  def modify[S](f: S => S): State[S, Unit] = for {
    s <- get // Gets the current state and assigns it to `s`.
    _ <- set(f(s)) // Sets the new state to `f` applied to `s`.
  } yield ()

  def get[S]: State[S, S] = State(s => (s, s))

  def set[S](s: S): State[S, Unit] = State(_ => ((), s))
}

sealed trait Input
case object Coin extends Input
case object Turn extends Input

case class Machine(locked: Boolean, candies: Int, coins: Int)

object Machine {
  def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] = {
    State((m: Machine) => {
      inputs.foldLeft(((m.coins, m.candies), m))((p, input) => {
        val newMachine = State.modify((x: Machine) => input match {
          case Coin => {
            if (x.locked && x.candies > 0) Machine(false, x.candies, x.coins + 1)
            else x
          }
          case Turn => {
            if (!x.locked && x.candies > 0) Machine(true, x.candies - 1, x.coins)
            else x
          }
        }).run(p._2)._2
        ((newMachine.coins, newMachine.candies), newMachine)
      })
    })
  }
}

object Candy {
  def update = (i: Input) => (s: Machine) =>
    (i, s) match {
      case (_, Machine(_, 0, _)) => s
      case (Coin, Machine(false, _, _)) => s
      case (Turn, Machine(true, _, _)) => s
      case (Coin, Machine(true, candy, coin)) => Machine(false, candy, coin + 1)
      case (Turn, Machine(false, candy, coin)) => Machine(true, candy - 1, coin)
    }

  def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] = for {
    // List[State[Machine, Unit]] => State[Machine, List[Unit]]
    _ <- State.sequence(inputs map (State.modify[Machine] _ compose update))
    // f compose g = f(g(x))
    // ```
    // scala> def addUmm(x: String) = x + " umm"
    // addUmm: (x: String)String
    // scala>  def addAhem(x: String) = x + " ahem"
    // addAhem: (x: String)String
    //
    // scala> val ummThenAhem = addAhem _ compose addUmm _
    // ummThenAhem: (String) => String = <function1>
    // ```
    // State.modify[Machine] _ compose update
    // State.modify(update(input))
    // _ <- State.sequence(inputs.map(input => State.modify[Machine](update(input))))
    s <- State.get
  } yield (s.coins, s.candies)
}

object Test {
  import RNG._
  import Rand._
  import Machine._

  def main(args: Array[String]): Unit = {
    val rng = SimpleRNG(42)
    val (n1, rng2) = rng.nextInt
    println(n1, rng2)

    val (n2, rng3) = rng2.nextInt
    println(n2, rng3)
    println(nonNegativeInt(rng2))
    println(nonNegativeInt(rng2))
    println(double(rng2))
    println(double(rng3))
    println(ints(3)(rng))

    val int: Rand[Int] = _.nextInt
    val randIntDouble: Rand[(Int, Double)] = both(int, double)
    val randDoubleInt: Rand[(Double, Int)] = both(double, int)

    println(ints(3)(rng))
    println(nonNegativeLessThan(30)(rng))
    println(simulateMachine(List(Coin, Turn, Coin, Turn, Coin, Turn, Coin, Turn)).run(Machine(true, 5, 10)))
  }

  def rollDie: Int = {
    val rng = new scala.util.Random
    rng.nextInt(6)
  }
}
