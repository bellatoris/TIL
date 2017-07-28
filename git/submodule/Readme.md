# Submodule

repository 안에 다른 repository 를 부르고 싶은 경우

```
cd /path/to/project
git submodule add ssh://path.to.repo/project2
```

와 같이 submodule add 를 사용하면 된다.

submodule 을 pull 받거나, update 하고 싶은 경우

```
git submodule update --recursive
```
하면 된다.
