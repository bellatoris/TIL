# Slow Escape in Vim

Tmux 를 사용하고 있을 때 vim 을 키면 scrolling 이나 다른것들이 모두 느려지는 것을 
발견 하였고, 검색결과 딱히 해결법은 없으며 Insert mode 에서 Normal mode 로 가는 것이 
느려지는 것은 

```
set-option -s escape-time 10
```

위와 같은 option 을 ~/.tmux.conf 에 추가하면 해결 할 수 있다는 것을 알게 되었다. 
느려지는 이유는 bash 나 tmux 에서 escape 다음에 다른 글자가 올 수 있으므로, 조금 
기다리기 때문이다. [참고](https://www.johnhawthorn.com/2012/09/vi-escape-delays/)
