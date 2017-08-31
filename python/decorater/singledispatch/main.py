def singledispatch(func):
    class NewFunc:
        def __init__(self, func):
            self.__func = func
            self.__func_dict = dict()

        def register(self, type_):
            """ Decorater factory """
            def decorate(func):
                self.__func_dict[type_] = func

            return decorate

        def __call__(self, obj):
            if self.__func_dict.get(type(obj)):
                return self.__func_dict[type(obj)](obj)
            else:
                return self.__func(obj)

    return NewFunc(func)

@singledispatch
def hi(i):
    print('hi')

@hi.register(str)
def _(text):
    print(text)

hi(1)
hi('hello')
