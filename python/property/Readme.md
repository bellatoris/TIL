# Propery

`@property` 를 이용하면, 해당 클래스의 필드처럼 사용하지만, 사실상 메서드로 
동작하는 코드가 가능하다. 이를 통해 마이어의 '일관된 액세스 원칙'을 을 지킬 수 
있다.

**일관된 액세스 원칙**

> 모듈을 통해 제공되는 모든 서비스는 일관된 표기법을 총해 사용할 수 있어야 한다. 
저장을 통해 구현되었건 간에 상관없이.

```python
class Test:

    def __init__(self):
        self.__color = 'red'

    @property
    def color(self):
        return self.__color

    @color.setter
    def color(self, clr):
        self.__color = clr


if __name__  == '__main__':
    t = Test()

    print(t.color)

    t.color = 'blue'

    print(t.color)
```

`@property` 를 이용하면 변수 변경에 제한을 둘 수 있다.

```python
class Celsius:
    def __init__(self):
        pass

    @property
    def temperature(self):
        print('Getting value')
        return self._temperature

    @temperature.setter
    def temperature(self, value):
        if value < -273:
            raise ValueError('Temperature below -273 is not possible')
        print('Setting value')
        self._temperature = value
```
