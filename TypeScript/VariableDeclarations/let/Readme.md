# `let` declarations

### Block-scoping
Block-scoped varialbe 은 `for`-loop 이나 nearest containing block 
밖에서는 보이지 않는다.

```typescript
function f(input: boolean) {
    let a = 100;

    if (input) {
        // Still okay to reference 'a'
        let b = a + 1;
        return b;
    }

    // Error: 'b' doesn't exist here
    return b;
}
```

`catch` clause 에서 declared 된 variable 도 마찬가지이다.

```typescript
try {
    throw "oh no!";
}
catch (e) {
    console.log("Oh well.");
}

// Error: 'e' doesn't exist here
console.log(e);
```

`let` 은 같은 scope 내에서 re-declaration 하는게 불가능 하다. 
더 nested 된 block 에서 같은 이름으로 declaration 하면 이전의 변수는 
*shadowing* 된다.

### Block-scoped variable capturing
Scope 가 실행될 때마다 변수의 "environment" 가 만들어진다. 
Scope 내의 모든 것이 실행을 마친 후에도 해당 environment 와 capture 된 
변수는 존재할 수 있다.

```typescript
function theCityThatAlwaysSleeps() {
    let getCity;

    if (true) {
        let city = "Seattle";
        getCity = function() {
            return city;
        }
    }

    return getCity();
}
```

`city` 를 environment 안에서 capture 했기 때문에, 
`if` block 이 끝난 후에도 `getCity` 에 접근이 가능하다.

`let` declarations 은 loop 안에서 정의될 경우, *iteration* 마다 
새로운 scope 를 생성한다.

```typescript
for (let i = 0; i < 10; i++) {
    setTimeout(function() { console.log(i); }, 100 * i);
}
```
