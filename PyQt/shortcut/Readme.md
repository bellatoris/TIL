# Shortcut

문자열 리터널 내에 사용된 `&` 표는 그 바로 뒤에 있는 문자가 단축키임을 
나타낸다. 

```python
find_button = QPushButton(self.tr('&Find'))
```

위의 코드는 `Find` 버튼을 생성하는데, `Alt` + `F` 를 누름으로써 이 버튼을 
활성화할 수 있게 된다. 

또한 `&` 는 특정 컨트롤에 포커스를 주기 위한 용도로도 사용될 수 있다.
