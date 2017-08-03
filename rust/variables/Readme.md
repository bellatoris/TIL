# Variables

mutable 하지 않은 변수에 두번 값을 할당하려하면 에러가 난다.

```rust
let x = 5;
x = 6; // error
```

mutable 하지 않은 변수는 절대 값이 바뀌지 않는다.

`const` 는 type 을 명시해 주어야만 한다. 또한 global scope 에서 정의 될 수 있다.
또한 constant expression 만 할당 될 수 있다. function call 의 result 나, 
runtime 에 computed 돼서 나온 값은 안된다.

**Shadowing**

같은 이름을 두번 정의하면, 이전에 정의한 변수는 shadowing 된다.

```rust
let x = 5;
let x = x + 1;
let x = x * 2;
```

같은 이름을 사용할 뿐, 다른 변수를 만드는 것이다.

```rust 
let spaces = "   ";
let spaces = spaces.len();

let mut spaces = "    ";
spaces = spaces.len(); // error
```
