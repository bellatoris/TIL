# Variadic Function

```scala
def apply[A](as: A*): List[A] = 
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))
```

`apply` 함수는 **가변 인수 함수**이다. 이는 이 함수가 `A` 형식의 인수를 
0개 이상 받을 수 있음을 뜻한다. 가변 인수 함수는 요소들의 순차열을 나타내는 
`Seq` 를 생성하고 전달하기 위한 작은 구문적 겉치레일 뿐이다. 특별한 형식 
annotation 인 `_*` 는 `Seq` 를 가변 인수 메서드에 전달할 수 있게 해준다.
