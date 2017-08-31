# Decorater
Decorater 는 다른 함수를 인수로 받는 callable 이다. Decorater 는 decorate 된 
함수에 어떤 처리를 수행하고, 함수르 반환하거나 함수를 다른 함수나 callable 객체로 
대체한다.

```python
def deco(func):
    print(func.__name__)
    return func

@deco
def hi():
    print('hi')
```

위의 decorater 는 syntatic sugar 일 뿐으로 아래 코드와 동일 하다. 


```python
def deco(func):
    print(func.__name__)
    return func

def hi():
    print('hi')

hi = deco(hi)
```

`functools.wrap(func)` 를 사용하면 decorate 된 함수의 `__name__` 과 `__doc__` 속성을 
가리지 않을 수 있다.

```python
import time
import functools

def clock(func):
    @functools.wraps(func)
    def clocked(*args, **kwargs):
        t0 = time.time()
        result = func(*args, **kwargs)
        elapsed = time.time() - t0
        name = func.__name__
        arg_list = []

        if args:
            arg_list.append(', '.join(repr(arg) for arg in args))
        if kwargs:
            pairs = ['%s=%r' %(k, w) for k, w in sorted(kwargs.itmes())]
            arg_list.append(', '.join(pairs)

        arg_str = ', '.join(arg_list)
        print('[%0.8fs] %s(%s) -> %r ' % (elapsed, name, arg_str, result))
        return result

    return clocked
```
