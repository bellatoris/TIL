# Implicit Class

기존에 존재하던 class 에 method 를 추가하고 싶은 경우 해당 class 를 
인자로 받는 `implicit class` 를 만든 후 원하는 method 를 추가하면 된다.

```scala
object Main {
  def main(args: Array[String]): Unit = {
    implicit class StringImprovements(s: String) {
      def increment = s.map(c => (c + 1).toChar)
    }


    val result = "HAL".increment
    println(result)   // IBM
  }
}
```

> An Implicit class must be defined in a scope where method 
definitions are allowed (not at the top level)

즉 implicit class 는 다음 세 곳중 하나에 정의 하고, 적절하게 `import` 해서 
사용해야 한다.

* A class
* An object
* A package object

### Put the implicit class in an object

```scala
package com.doogie.utils

object StringUitls {
    implicit class StringImprovements(val s: String) {
        def increment = s.map(c => (c + 1).toChar)
    }
}
```

위와 같이 정의하면 `import` 후에 어디서든 사용 가능하다.

```scala
package foo.bar

import com.doogie.util.StringUtils._

object Main extends App {
    println("HAL".increment)
}
```

### Put the implicit class in a package object

다음 코드는 `package.scala` 파일 에 존재 해야 하며, sbt 를 사용중이라면 파일은 
`src/main/scala/com/doogie/` directory 에 존재해야 할 것이다. 

```scala
package com.doogie

package object utils {
    implicit class StringImprovements(val s: String) {
        def increment = s.map(c => (c + 1).toChar)
    }
}
```

`import` 후 사용할 수 있다.

```scala
package foo.bar

import com.doogie.utils._

objcet MainDriver extends App {
    println("HAL".increment)
}
```

## Using versions of Scala prior to version 2.10

2.10 version 이전의 경우 다음과 같이 사용해야 한다. 먼저 
class 를 정의하고

```scala
class StringImprovements(val s: String) {
    def increment = s.map(c => (c + 1).toChar)
}
```

그 다음, implicit conversion 을 정의해야 한다.

```scala
implicit def stringToString(s: String) = new StringImprovements(s)
```

이제 `"HAL".increment` 를 call 하면, `String` 을 `StringImprovements` 로 
conversion 하여 `increment` 를 call 하게 된다.

반면 아래와 같은 코드는 동작 하지 않는다.

```scala
implicit def increment(s: String) = s.map(c => (c + 1).toChar)

"HAL".increment     // error
```

우선, `String` 에 `increment` method 가 존재하지 않기 때문에, 위의 코드는 
동작하지 않는 것이 당연하다. 반면 implicit conversion 의 경우 `String` 을 
argument 로 받고 `increment` method 가 있는 class 로의 implicit conversion 
을 찾아 내, conversion 을 해 `increment` 를 call 하게 된다.

```scala
implicit class A(s: String) {
    def hi: Int = 1
}

implicit class B(s: String) {
    def hi: Boolean = true
}

val a: Int = "HAL".hi    // error
```

같은 이름의 method 가 다른 implicit class 에 존재하는 경우 return type 이 
달라도 error 가 발생한다.
