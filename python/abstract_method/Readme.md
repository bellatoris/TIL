# Abstract Method

abstract method 를 static method, class method 와 비교해가며 알아보자.

### Static Method

```ptyhon
class MethodTest:
    __c_test = 1
    def __init__(self):
        pass

    @staticmethod
    def s_print():
        print('is staticmethod')

MethodTest.s_print()
```

static method 는 해당 클래스를 namespace 처럼 사용하기 위해 사용한다. 즉 해당 
객체를 생성하지 않아도 해당 클래스 이름만 가지고 해당 method 를 호출 할 수 있다.
static method 는 `self` 인자를 받지 않는다.

### Class Method

```python
class MethodTest:
    __c_test = 1
    def __init__(self):
        pass

    @classmethod
    def c_print(cls):
        cls.__c_test += 1
        print(cls.__c_test)

method_test1 = MethodTest()
method_test2 = MethodTest()

method_test1.c_print() // 2
method_test2.c_print() // 3
```

기본 method 는 instance method 로 `self` 인 instance 를 인자로 받고 instance 변수와 같이 
하나의 instance 에만 한정된 데이터를 생성, 변경, 참조한다. 반면 class method는 `cls`인 
class 를 인자로 받고 모든 인스턴스가 공유하는 class 변수와 같은 데이터를 생성, 변경, 
참조하기 위한 method 이다.

class method 는 constructor 와 같은 용도로 사용할 수도 있다.

```python
# -*- coding: utf-8 -*-

class Person:
    def __init__(self, year, month, day, sex):
        self.year = year
        self.month = month
        self.day = day
        self.sex = sex

    def __str__(self):
        return '{}년 {}월 {}일생 {}입니다.'.format(self.year
                                                   self.month
                                                   self.day
                                                   self.sex)

    @classmethod
    def ssn_constructor(cls, ssn):
        front, back = ssn.split('-')
        sex = back[0]

        if sex == '1' or sex == '2':
            year = '19' + front[:2]
        else:
            year = '20' + front[:2]

        if (int(sex) % 2) == 0:
            sex = '여성'
        else:
            sex = '남성'

        month = front[2:4]
        day = front[4:6]

        return cls(year, month, day, sex)


ssn_1 = '900829-1034356'

person_1 = Person.ssn_constructor(ssn_1)
print(person_1)
```

### Abstract Method

abstract method 를 이용하면 `raise` 를 이용하지 않고 자체적으로 
구현되지 않은 interface method 들에 대해 error 를 낼 수 있다.

```python
from abc import ABC, abstractmethod

class Interface(ABC):
    @abstractmethod
    def interface_test(self, *args, **kwargs):
        pass

class Mung(Interface):
    def __init__(self):
        print('Initial')

    def interface_test(self):
        print('interface test')

if __name__ == "__main__":
    munggae = Mung()            // Initial
    munggae.interface_test()    // interface test
```

`interface_method` 를 override 하지 않은 class 를 만들면 error 가 
발생한다.
