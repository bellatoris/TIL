# Argument Name

가끔 function 의 parameter 의 name 을 알고 싶을 때가 있다. 이러한 경우 
`function.__code__.co_varnames` 를 통해 parameter 의 name 을 알아낼 수 있다.
올바른 예인지는 모르겠으나, 한 곳에서 조금 씩 다른 signature 를 가진 
function 들을 계속해서 호출 해야하는 경우에 parameter 이름을 아는 것이 
필요 했다.

```python
def get_arguments(func, **kwargs):
    new_kwargs = {}
    for parameter_name in func.__code__.co_varnames:
        arg = kwargs.get(parameter_name, None)

        if arg:
            new_kwargs[parameter_name] = arg

    return arg
```

`kwargs` 로는 공통적인 keyword arguments 들이 넘어 오는데 function 마다 signature 가 
조금씩 달라서 이러한 방법을 사용했다.
