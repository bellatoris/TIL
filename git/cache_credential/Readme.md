# Cache git id and password

매번 id 와 password 를 치는게 귀찮다면, id 와 password 를 caching 해 두면 된다.

```sh
git config --global credential.helper cache
```

[여기](https://stackoverflow.com/questions/5343068/is-there-a-way-to-skip-password-typing-when-using-https-on-github) 를 참고하자.
[여기](https://git-scm.com/book/ko/v2/Git-%EB%8F%84%EA%B5%AC-Credential-%EC%A0%80%EC%9E%A5%EC%86%8C) 도 참고하자.
