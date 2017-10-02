# Multi Processing - Start

Python 의 multiprocessing 은 새로운 process 를 만드는 방법 (start method) 
이 세가지가 존재한다.

1. *spawn*
    * Parent process 가 fresh python process 를 생성해서 시작시킨다. 
    Child process 는 `run()` method 실행 하기 위한 resources 만 parent 
    process 로 부터 받는다. 새로히 process 를 생성하고, parent 로 부터 
    resource 를 전달 받아야 하기 때문에 밑에서 설명할 *fork* 나 
    *forksever* 에 비해서 느리다. Window 와 Unix 에서 사용가능 하고, 
    Window 의 default 이다.
2. *fork*
    * Parent process 가 `os.fork()` 를 이용해서 현재 python process 를 
    fork 한다. 즉, child process 는 시작시에 parent process 와 동일하다. 
    Parent process 의 모든 resources 를 child process 가 들고 있게 된다. 
    Unix 에서만 사용 가능하고 Unix 의 default 이다.
3. *forkserver* 
    * *forkserver* 가 돌고 있고, parent process 들은 *forkserver* 에게 
    새로운 process 가 필요하다고 요청한다. *forkserver* 는 자신을 
    fork 해서 새로운 process 를 생성하고, 이렇게 생성된 child process 
    들은 parent process 로 부터 필요한 resources 를 받는다. 
    Unix 에서 사용 가능하다.

Unix 에서 *spawn* 과 *forkserver* 를 사용할 경우 *semaphore tracker* 
process 도 같이 실행된다. *semaphore tracker* 는 processes 에 의해 
생성된 named semaphore 를 tracking 한다. 모든 process 들이 종료 될 때 
semaphore tracker 는 남은 semaphore 를 unlink 한다. Process 들이 signal 
에 의해 kill 될 때, "leaked" semaphore 가 생길 수 있기 때문이다.

`get_context()` 를 이용해서 start method 를 선택할 수 있다.

```python
import multiprocessing as mp


def foo(q):
    q.put('hello')


if __name__ == '__main__':
    ctx = mp.get_context('spawn')
    q = ctx.Queue()
    p = ctx.Process(target=foo, args=(q,))
    p.start()
    print(q.get())
    p.join()
```

### spawn
*spawn* method 를 선택한 경우, child process 가 필요한 resources 에 객체가 
존재하는 경우 serialize/deserialize 를 통해 객체의 전달이 이루어진다. 
*fork* 를 사용하는 경우 child process 가 parent 와 동일한 resources 를 모두 
들고 있으므로, 이러한 문제가 없지만 Window 에서는 *fork* 방식을 사용 할 수 
없으므로, serialization 을 염두해 두어야만 한다.
