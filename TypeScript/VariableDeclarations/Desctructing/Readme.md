# Destructing

TypeScript 는 destructing 이 가능하다.

```typescript
let input = [1, 2];
let [first, second = input;
```

이미 정의되어 있는 변수에도 사용 가능하다.

```typescript
// swap variables
[first, second] = [second, first];
```

함수에도 사용 가능하다.

```typescript
function f([first, second]: [number, number]) {
    console.log(first);
    console.log(second);
}
f([1, 2]);
```

`Object` 도 가능하다.

```typescript
let o = {
    a: "foo",
    b: 12,
    c: "bar"
};
let { a, b } = o;
```

`c` 는 skip 된다.
