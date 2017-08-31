# Mutable

기본값을 가진 선택적 인수는 파이썬 함수 정의에서 아주 좋은 기능으로, 하위 
호환성을 유지하며 API 를 개선할 수 있게 해준다. 그러나 매개변수 기본값으로 
가변 객체를 사용하는 것은 피해야 한다.

```python
class HauntedBus:
    def __init__(self, passengers=[]):
        self.passengers = passengers

    def pick(self, name):
        self.passengers.append(name)

    def drop(self, name):
        self.passengers.remove(name)

bus1 = HauntedBus()
bus1.pick('Carrie')

bus2 = HauntedBus()
bus2.passengers     # ['Carrie']

bus3 = HauntedBus(['Alice'])
bus3.passengers     # ['Alice']
```

명시적인 승객 리스트로 초기화되지 않은 `Bus` 객체들이 승객 리스트를 공유하게 
되는 문제가 발생한다. `self.passengers` 가 `passengers` 의 default 값의 
별명이 되기 때문이다. 문제는 각 default 값이 함수가 정의될 때 (즉, 일반적으로 모듈이 
로딩될 때) evaluate 되고 default 값은 함수 객체의 속성이 된다는 것이다. 
따라서 default 값이 가변 객체고, 이 객체를 변경하면 변경내용이 향후에 이 함수의 
호출에 영향을 미친다.

## Defensive Programming for Mutable Object

가변 매개변수를 받는 함수를 구현할 때는, 전달된 인수가 변경될 것이라는 것을 
호출자가 예상할 수 있는지 없는지 신중하게 고려해야 한다.

예를 들어 여러분이 구현하는 함수가 `dict` 객체를 받아서 `dict` 객체를 변경한다면, 
함수가 반환된 후에도 변경 내용이 남아 있어야 할까 아닐까? 판단은 상황에 따라 다르다. 
**정말 중요한 것은 함수 구현자와 함수 호출자가 예상하는 것을 일치시키는 것이다.**

```python
class TwilightBus:
    def __init__(self, passengers=None):
        if passengers is None:
            self.passengers = []
        else:
            # self.passengers = passengers
            self.passengers = list(passengers)      # make a copy

    def pick(self, name):
        self.passnegers.append(name)

    def drop(self, name):
        self.passengers.remove(name)
```

`self.passengers = passengers` 의 경우 다음과 같은 문제가 생길 수 있다.

```python
basketball_team = ['Sue', 'Tina', 'Maya', 'Diana']
bus = TwilightBus(basketball_team)
bus.drop('Tina')
bus.drop('Maya')
print(basketball_team)      # ['Sue', 'Diana']
```

`TwilightBus` class 는 interface 디자인에서 가장 중요한 '최소 놀람의 법칙'을 
어긴다. 학생이 버스에서 내린다고 해서 그 학생이 농구팀 출전 명단에서 빠진다는 것은 
분명 놀라운 일이다.

> 인수로 받은 객체를 method 가 변경할 것이라는 의도가 명백하지 않은 한 class 
안에서 인수를 변수에 할당하는 것은 주의할 필요가 있다.
