# Neovim-qt 

설치하는게 매우 까다롭다. Qt homepage 에 가서 Qt 5.9 를 받은 후에 
`neovim-qt/` dir 에 존재하는 `CMakeLists.txt` 에 다음과 같은 커맨드를 
추가해야만 한다.

```cmake
set(CMAKE_PREFIX_PATH "/home/doogiemin/Qt/5.9.2/gcc_64/lib/cmake")
```
