# Scala 에서 multiline string 만들기

Scala 에서 multiline string 은 세 개의 `"` 로 감싸서 만들 수 있다.

```scala
val foo = """This is
	a multiline
	String"""
```

그러나 두번째와 세번째 line 의 시작이 공백이기 때문에 아래와 같이 print 된다.

``` 
This is 
	a multiline
	String
```

`stripMargin` 을 사용해서 이를 해결 할 수 있다.

```scala
val speech = """Four score and
				|seven years ago""".stripMargin
```

만약 이를 한줄로 표현하고 싶다면 `replaceAll` 을 이용해서 new line character 를 공백으로 바꿔주면 된다.

```scala
val speech = """Four score and 
				|seven years ago
				|our fathers""".stripMargin.replaceAll("\n", " ")