# Stack Trace 

Call back 함수가 도대체 어디서 불리는지 알 수가 없어서, call back 함수가 
불릴 때 현재 stack trace 를 찍어보고 싶었다. `traceback` 을 이용해서 매우 
쉽게 가능하다는 것을 알게 되었다.

```python
import traceback

def f():
    g()

def g():
    for line in traceback.format_stack():
        print(line.strip())


f()

# Prints:
# File "so-stack.py", line 10, in <module>
#     f()
# File "so-stack.py", line 4, in f
#     g()
# File "so-stack.py", line 7, in g
#     for line in traceback.format_stack():
```

