import weakref

a_set = {0, 1}
wref = weakref.ref(a_set)
print(wref)     # <weakref at 0x100637598; to 'set' at 0x100636748>

print(wref())   # {0, 1}

print(wref() is None)   # False

a_set = {1, 2, 3}
print(wref() is None)   # True


import weakref


class Cheese:
    def __init__(self, kind):
        self.kind = kind

    def __repr__(self):
        return 'Cheese(%r)' % self.kind


stock = weakref.WeakValueDictionary()
catalog = [Cheese('Red Leicester'), Cheese('Tilsit'),
           Cheese('Brie'), Cheese('Parmesan')]

for cheese in catalog:
    stock[cheese.kind] = cheese

print(sorted(stock.keys()))     # ['Brie', 'Parmesan', 'Red Leicester', 'Tilsit']

del catalog

print(sorted(stock.keys()))     # ['Parmesan']

del cheese

print(sorted(stock.keys()))     # []
