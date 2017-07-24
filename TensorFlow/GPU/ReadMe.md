특정 GPU에 지정하고 싶을 때는 `Session` 을 열때 주의해주어야 한다.

```python
config = tf.ConfigProto(allow_soft_placement=True)
sess = tf.Session(config=config)
```

위와 같이 `config` 를 선언한 후 인자로 넘겨주어야 만 한다.

[여기를 참고](https://github.com/tensorflow/tensorflow/issues/2285)
