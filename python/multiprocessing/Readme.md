# Multi Processing - Process-based Parallelism

Python 의 경우 GIL (Global Interpreter Lock) 때문에, multi threading 시에 thread 들이 
실제로 여러 core 위에서 실행되는 것은 아니다. 그렇기 때문에 실제로 여러 core 를 사용해서 
프로그램의 병렬성을 높이고 싶은 경우에는 multi processing 을 해야만 한다.
