# Retrieve Module

`pkgutil` 을 이용해서 현재 directory 의 module 과 package 를 retrieval 가능하다. 
또한 retrieval 한 module 을 import 하는 것도 가능하다.

```python
import pkguil
import email

package = email

for importer, modname, ispkg in pkguil.iter_modules(package.__path__):
    if not ispkg:
        m = importer.find_module(modname).load_module(modname)
        # then m is the module, m.myfunc()
```

`importlib` 을 사용하는 것도 가능하다.

```python
from os import listdir, getcwd
from os.path import isfile, join

import importlib
import inspect

mypath = os.getcwd()
onlyfiles = [f for f in listdir(mypath) if isfile(join(mypath, f))]

for f in onlyfiles:
    module = importlib.import_module(f)
    for name, obj in inspect.getmembers(module, inspect.isclass):
        print(obj)
```
