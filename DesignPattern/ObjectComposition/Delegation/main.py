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


def main():
    window = Window(3, 5)
    print(window.area())
    print(window.area2())


if __name__ == '__main__':
    main()
