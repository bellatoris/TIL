# del

`del` 명령은 이름을 제거하는 것이지, 객체를 제거하는 것이 아니다. `del` 명령의 결과로 
제거된 변수가 객체를 참조하는 최후의 변수이거나, 더 이상 객체에 도달 할 수 없을 때에만, 
객체가 GC 된다.

```python
import weakref

s1 = {1, 2, 3}
s2 = s1
def bye():
    print('Gone with the wind...')

ender = weakref.finalize(s1, bye)
ender.alive     # True

del s1
ender.alive     # True

s2 = 'spam'     # Gone with the wind
ender.alive     # False
```
