# `var` declarations

### Scoping rules

`var` declarations 은 매우 이상한 scoping rule 을 가지고 있다.

```typescript
function f(shouldInitialize: boolean) {
    if (shouldInitialize) {
        var x = 10;
    }

    return x;
}

f(true); // returns '10'
f(false); // return 'undefined'
```

`var` declarations 는 containing block 에 상관없이, containing fuction, 
module, namespace, global scope 어디서든 접근이 가능하기 때문에 위와 같은 
문제가 발생하며, 여러가지 실수의 원인이 된다.

```typescript
funtion sumMatrix(matrix: number[][]) {
    var sum = 0;
    for (var i = 0; i < matrix.length; i++) {
        var currentRow = matrix[i];
        for (var i = 0; i < currentRow.length; i++) {
            sum += currentRow[i];
        }
    }

    return sum;
}
```

Nested for-loop 의 `i` 가 첫번째 for-loop 의 `i` 를 덮어 씌우는 문제가 
존재한다. 같은 function-scoped variable 이기 때문이다.

#### Variable capturing quirks

```typescript
for (var i = 0; i < 10; i++) {
    setTimeout(function() { console.log(i); }, 100 * i);
}
```

위 코드의 결과는 모두 10 이다. `setTimeout` 에 넘긴 모든 function 이
같은 scope 의 같은 `i` 를 참조하고 있기 때문이다.

work around

```typescript
for (var i = 0; i < 10; i++) {
    // capture the current state of 'i'
    // by invoking a function with its current value
    (function(i) {
        setTimeout(function() { console.log(i); }, 100 * i);
    })(i);
}
