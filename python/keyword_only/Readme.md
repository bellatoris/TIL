# Keyword Only Argument

```python
def hi(a, *, b=1):
    print(a, b)

hi(1, 2)        # Error
hi(1, b=2)      # Success
hi(a=1, b=2)    # Success
```

위와 같이 선언하면, `b` 에는 무조건 keyword argument 형태로 argument 를 넘겨야 한다.
