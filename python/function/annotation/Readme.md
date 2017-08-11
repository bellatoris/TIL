# Annotation
Python3 는 함수의 parameter 와 반환값에 metadata 를 추가할 수 있는 구문을 
제공한다.

```python
def clip(text:str, max_len:'int > 0'=80) -> str:
    """max_len 앞이나 뒤의 마지막 공백에서 잘라낸 텍스트를 반환한다.
    """
    end = None
    if len(text) > max_len:
        space_before = test.rfind(' ', 0, max_len)
        if space_before >= 0:
            end = space_before
        else:
            space_after = text.rfind(' ', max_len)
            if space_after >= 0:
                end = space_after
    if end is None: # 공백이 없다.
        end = len(text)

    return text[:end].rstrip()
```

함수 선언에서 각 parameter 에는 `:` 뒤에 annotation 표현식을 추가할 수 있다. 
Annotation 은 전혀 처리되지 않으며, 단지 함수 객체 안의 dict 형 `__annotation__` 
속성에 저장될 뿐이다. Python interpreter 에는 아무 의미없는 metadata 로 이 것을 
사용하는 표준 라이브러리는 없다. 
