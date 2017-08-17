# Arguments
Python 에서 iterable 객체나 dictionary 형을 `*` 와 `**` 을 이용해 
별도의 인수로 '폭발' 시킬 수 있다.

```python
def tag(name, *content, cls=None, **attrs):
    """Create more than one HTML tag"""
    if cls is not None:
        attrs['class'] = cls
    if attrs:
        attr_str = ''.join(' %s="%s"' % (attr, value)
                           for attr, value
                           in sorted(attrs.items()))
    else:
        attr_str = ''

    if content:
        return '\n'.join('<%s%s>%s</%s>' %
                         (name, attr_str, c, name) for c in content)
    else:
        return '<%s%s />' % (name, attr_str)
```

그러나 이런 keyword argument 와 positional argument 의 남용은 코드를 이해하기 
힘들게 만든다. 주석을 작성하거나, 위와 같은 특별한 상황에서만 사용해야만 한다.

## Argument 의 정보를 읽기 
함수 객체 안의 `__defaults__` 속성에는 positional parameter 와 keyword parameter 
의 기본값을 가진 tuple 이 들어 있다. `__code__` 속성에 parameter 의 이름이 
들어가 있다. Parameter 명은 `__code__.co_varnames` 에 들어가 있지만, 함수 
본체에서 생성한 지역 변수명도 들어 있다. `__code__.co_argcount` 만큼만 parameter 
이름이다.
