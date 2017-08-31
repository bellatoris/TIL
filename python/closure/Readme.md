# Closure

Closure 는 함수 본체에서 정의하지 않은 nonglobal 변수를 capture 해서 
자신의 environment 에 가지고 있는 함수이다.

```python
def make_averager():
    series = []

    def averager(new_value):
        series.append(new_value)
        total = sum(series)
        return total/len(series)

    return averager

avg = make_averager()
avg(10)     # 10
avg(11)     # 11
```

`averager` 안에 있는 `series` 는 **free variable** 이다. Free variable 이란 
지역 범위에 binding 되어 있지 않은 변수를 의미한다. 

반환된 `averager()` 객체를조사해보면 파이썬이 컴파일된 함수 본체를 나타내는 
`__code__` 속성 안에 어떻게 local variable 과 free variable 의 '이름'을 
저장하는지 알 수 있다.

```python
avg.__code__.co_varnames    # ('new_value', 'total')
avg.__code__.co_freevars    # ('series',)
```

`series` 에 대한 binding 은 반환된 `avg()` 함수의 `__closure__` 속성에 저장된다. 
즉 closure 는 함수를 정의할 때 존재하던 free variable 에 대한 binding 을 
유지하는 함수이다. 따라서 함수를 정의하는 범위가 사라진 후에 함수를 호출해도 
free variable 에 접근 할 수 있다.

**함수가 'nonglobal' 외부 변수를 다루는 경우는 그 함수가 다른 함수 안에 
정의된 경우뿐이라는 점에 주의하라.**

## nonlocal

앞에서 구현한 `make_averager()` 는 비효율 적이다. 매번 새로 `total` 을 계산하고, 
`len(series)` 를 계산하기 때문이다.

```python
def make_averager():
    count = 0
    total = 0

    def averager(new_value):
        nonlocal count, total
        count += 1
        total += new_value
        return total / count

    return averager
```

`nonlocal` 을 사용하지 않는다면 `count += 1` 시에 error 가 나게된다. 
`count += 1` 은 결국 `count = count + 1` 이기 때문에 `count` 를 
local variable 로 만들어, assignment 전에 reference 하는 꼴이 되버리기 
때문이다.  
변수를 `nonlcoal` 로 선언하면 함수 안에서 변수에 새로운 값을 할당하더라도 
그 변수는 free variable 임을 나타낸다. 새로운 값을 `nonlocal` 변수에 할당하면 
clousre 에 저장된 binding 이 변경되다.
