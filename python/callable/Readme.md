# Callable
python 에서는 객체를 callable 로 만들 수 있다.

```python
class BingoCage:
    def __init__(self, items):
        self._items = list(times)
        random.shuffle(self._itmes)

    def pick(self):
        try:
            return self._items.pop()]
        except IndexError:
            raise LookupError('pick from empty BingoCage')

    def __call__(self):
        return self.pick()
```

`bingo.pick()` 에 대한 단축 형태로 `bingo()` 를 정의 하였다. 
`BingoCage` 의 경우 객체를 함수처럼 호출할 때마다 항목을 꺼낸 후 변경된 상태를 
유지해야 하는데, `__call__()` 를 구현하면 이런 객체를 생성하기 쉽다. 
