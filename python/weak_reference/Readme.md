# Weak Reference

객체가 메모리에 유지되거나 유지되지 않도록 만드는 것은 참조의 존재 여부다. 
객체 참조 카운트가 0 이 되면 GC 는 해당 객체를 제거한다. 그러나 불필요하게 
객체를 유지시키지 않으면서 객체를 참조할 수 있으면 도움이 되는 경우가 
종종 있다. 캐시가 대표적인 경우다.


Weak reference 는 참조 카운트를 증가시키지 않고 객체를 참조한다. 참조의 대상인 
객체를 **referent** 라고 한다. Weak reference 는 캐시 애플리케이션에서 유용하게 
사용된다. 캐시가 참조하고 있다고 해서 캐시된 객체가 계속 남아 있기 원치 않기 
때문이다.

```python
import weakref

a_set = {0, 1}
wref = weakref.ref(a_set)
print(wref)     # <weakref at 0x100637598; to 'set' at 0x100636748>

print(wref())   # {0, 1}

print(wref() is None)   # False

a_set = {1, 2, 3}
print(wref() is None)   # True
```

`weakref.ref` class 는 고급 사용자를 위한 저수준 interface 이며, 일반 프로그래머는 
`weakref` 컬렉션과 `finalize()` 를 사용하는 것이 좋다고 설명한다. 즉, `weakref.ref` 
객체를 직접 만들기보다는 `WeakKeyDictionary`, `WeakyValueDictionary`, `WeakSet`, 
그리고 내부적으로 weak reference 를 이용하는 `finalize()` 를 사용하는 것이 좋다.


`list` 와 `dict` 객체는 weak reference 의 참조 대상이 될 수 없다. 이 class 를 
상속받은 subclass 는 참조 대상이 될 수 있다. `int` 나 `tuple` class 는 class 
를 상속해도 weak reference 의 참조 대상이 될 수 없다.

## WeakValueDictionary

`WeakValueDictionary` class 는 객체에 대한 약한 참조를 값으로 가지는 가변 매핑을 
구현한다. 참조된 객체가 프로그램 다른 곳에서 GC 되면 해당 키도 `WeakValueDictionary` 
에서 자동으로 제거된다. 이 클래스는 일반적으로 캐시를 구현하기 위해 사용된다.

```python
import weakref


class Cheese:
    def __init__(self, kind):
        self.kind = kind

    def __repr__(self):
        return 'Cheese(%r)' % self.kind


stock = weakref.WeakValueDictionary()
catalog = [Cheese('Red Leicester'), Cheese('Tilsit'),
           Cheese('Brie'), Cheese('Parmesan')]

for cheese in catalog:
    stock[cheese.kind] = cheese

print(sorted(stock.keys()))     # ['Brie', 'Parmesan', 'Red Leicester', 'Tilsit']

del catalog

print(sorted(stock.keys()))     # ['Parmesan']

del cheese

print(sorted(stock.keys()))     # []
```

## WeakKeyDictionary
`WeakKeyDictionary` class 는 키가 약한 참조다. 

> `WeakKeyDictionary` 는 애플리케이션의 다른 부분에서 소유하고 있는 객체에 속성을 
추가하지 않고 추가 적인 데이터를 연결할 수 있다. 이 클래스는 속성 접근을 override 
하는 객체(디스크립터)에 특히 유용하다.

## WeakSet
`WeakSet` class 는 요소를 weak reference 로 가리키는 set class 이다. 어떤 요소에 대한 
참조가 더 이상 존재하지 않으면 해당 요소가 제거 된다. 자신의 객체를 모두 알고 있는 
클래스를 만들어야 한다면, 각 객체에 대한 참조를 모두 `WeakSet` 형의 class 속성에 저장하는 
것이 좋다.
