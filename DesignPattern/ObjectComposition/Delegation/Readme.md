# Delegation
위임에서는 두 객체가 하나의 요청을 처리한다. 수신 객체 (Window) 가 오퍼레이션
처리를 위임자 (Rectangle) 에게 보낸다. 수신 객체가 자신을 파라미터로 위임자에게 
전달해, 메세지를 통해 자신이 정의한 데이터를 얻어가게 해서 진정한 위임을 구현 할 수 있다.

```python
class Rectangle:
    def __init__(self, width, height):
        self.width = width
        self.height = height

    def area(self):
        return self.width * self.height

    def area2(self, owner):
        return owner.get_width() * owner.get_height()


class Window:
    def __init__(self, width, height):
        self.rectangle = Rectangle(width, height)
        self.width =width
        self.height = height

    def area(self):
        return self.rectangle.area()

    def area2(self):
        return self.rectangle.area2(self)

    def get_width(self):
        return self.width

    def get_height(self):
        return self.height
```

위임의 유용성은 상황에 따라 다르고 얼마나 많은 경험을 갖고 있는가에 좌우되므로 
위임은 고도로 표준화된 패턴에서 사용해야 한다.
