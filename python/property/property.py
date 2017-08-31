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
