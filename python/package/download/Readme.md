# Download Package

Python package 를 인터넷이 사용 불가능한 곳에서 설치하고 싶을 때, 그냥 해당 package 
의 `.whl` 파일을 다운받아서 설치할 때 package 의 dependency 들을 인터넷에서 
받으려고 해 실패하는 경우가 있다. 구글링 결과 [다음과](https://stackoverflow.com/questions/36725843/installing-python-packages-without-internet-and-using-source-code-as-tar-gz-and) 
같은 글을 발견 하였다.

```sh
mkdir keystone-deps
pip download python-keystoneclient -d "/home/aviuser/keystone-deps"
tar cvfz keystone-deps.tgz keystone-deps
```

위와 같은 커맨드를 통해 `.whl` 파일을 다운 받았으면 원하는 곳에서 그냥 설치하면 된다.

```sh
tar xvfz keystone-deps.tgz
cd keystone-deps
pip install python_keystoneclient-2.3.1-py2.py3-none-any.whl -f ./ --no-index
```

`-f` 와 `--no-index` 옵션은 무슨 옵션인지 모르겠다.
