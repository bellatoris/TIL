# Object 

어떤 class 가 `object` class 를 inherit 하는 경우가 있다. 

python 3 의 경우

```python
# Python 3.x:

class MyClass(object): = new-style class
class MyClass: = new-style class (implicitly inherits from object)
```

python 2 의 경우

```python
# Python 2.x:

class MyClass(object): = new-style class
class MyClass: = OLD-STYLE CLASS
```

Old-style 의 경우 `class` 는 `type` 과 무관하였다. Old-style 의 경우 
`x.__class__` 는 x 의 class 값을 주지만, `type(x)` 는 언제나 
`<type 'instance>` 만을 돌려준다. 그러나 new-style 의 경우 `type(x)` 가 
`x.__class__` 를 돌려준다.
