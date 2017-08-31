# Single dispatch

파이썬은 method 나 함수의 overloading 을 지원하지 않으므로, 서로 
다르게 처리하고자 하는 자료형별로 다른 signature 를 가진 method 를 
만들 수 없다. 이때 파이썬에서는 일반적으로 `if/elif/else` 문을 이용해서 
특화된 method 를 호출한다. 그러나 이 방법은 이 module 의 사용자가 코드를 
확장하기 쉽지 않으며, 다루기도 어렵다. 

파이썬의 `functools.singledispatch()` decorater 가 이를 해결 해준다. 
일반 함수를 `@singledispatch` 로 decorate 하면, 이 함수는 **generic function** 
이 된다. 즉, 일련의 함수가 첫 번째 인수의 자료형에 따라 서로 다른 방식으로 연산을 
수행하게 된다.

```python
from functools import singledispatch
from collections import abc
import numbers
import html

@singledispatch
def htmlize(obj):
    content = html.escape(repr(obj))
    return '<pre>{}</pre>'.format(content)

@htmlize.register(str)
def _(text):
    content = html.escape(text).replace('\n', '<br>\n')
    return '<p>{0}</p>'.format(content)

@htmlize.register(numbers.Integral)
def _(n):
    return '<pre>{0} (0x{0:x})</pre>'.format(n)

@htmlize.register(tuple)
@htmlize.register(abc.MutableSequence)
def _(seq):
    inner = '</li>\n<li>'.join(htmlize(item) for item in seq)
    return '<ul>\n<li>' + inner + '</li>\n</ul>'
```

가능하다면 `int` 나 `list` 같은 구상 클래스보다 `numbers.Integral` 이나 
`abc.MutableSequence` 와 같은 추상 베이스 클래스를 처리하도록 특화된 함수를 
등록하는 것이 좋다. 

`singledispatch()` 의 구현은 아마 다음과 같을 것이다. 

```python
def singledispatch(func):
    class NewFunc:
        def __init__(self, func):
            self.__func = func
            self.__func_dict = dict()

        def register(self, type_):
            """ Decorater factory """
            def decorate(func):
                self.__func_dict[type_] = func

            return decorate

        def __call__(self, obj)
            if self.__func_dict.get(type(obj)):
                return self.__func_dict[type(obj)](obj)
            else:
                return self.__func(obj)

    return NewFunc(func)
```
