# Companion Object 

scala 에서 case class 의 companion object 는 compiler 가 생성한다. 그런데 
scala 2.11.\* 버전에서는 case class 의 companion object 를 따로 만들려고 하면 
(method 내부에서) compile error 를 띄웠다. scala 2.12.2 에서는 문제가 해결 되었다. 
또한 `apply` method 의 경우 2.11.\* 에서는 
[issue](https://stackoverflow.com/questions/43365342/scala-method-apply-is-defined-twice-when-trying-to-overload-case-class-apply-me) 
가 존재 했는데, 이 또한 2.12.2 에서는 해결 되었다.

```scala
object Main {
    def main(args: Array[String]): Unit = {
        class A(i: Int) {
        }

        object A {
            def apply(i: Int): A = new A(i)     // 2.11.* 에서는 error 발생
            def apply(d: Double): A = new A(d.toInt)
        }
    }
}
```

Case class 의 **constructor** 를 overloading 한 경우, pattern-matching 시에는 
기본 constructor 로만 matching 된다.
