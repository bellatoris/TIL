# Basic Types

### Boolean
```typescript
let isDone: boolean = false;
```

### Number
JavaScript 와 마찬가지로 모든 number 는 floating point value 이다. 
TypeScript 의 floating point numbers 는 `number` type 이며, hexadecimal 
과 decimal, octal, binary 모두 `number` type 이다.

```typescript
let decimal: number = 6;
let hex: number = 0xf00d;
let binary: number = 0b1010;
let octal: number = 0o744;
```

### String
JavaScript 에서 사용하는 textual data 의 type 은 `string` 이다. 
JavaScript 와 마찬가지로 `"` 와 `'` 둘다 사용 가능하다.

```typescript
let color: string = "blue";
color = 'red';
```

또한 *template strings* 를 사용하여 여러 line 으로 span 가능하며, 
embedded expression 을 사용 할 수 있다. 이러한 string 은 \` 으로 
감싸져 있으며, embedded expression 은 `${ expr }` 꼴을 가지고 있다.

```typescript
let fullName: string = `Bob Bobbington`;
let age: number = 37;
let sentence: string = `Hello, my name is ${ fullName }.

I'll be ${ age + 1} years old next monnt.`;
```

### Array
Array 를 선언하는 방법은 두 가지가 존재한다.

1. Type 뒤에 `[]` 를 붙이면 array 로 선언 하는 것이다.

    ```typescript
    let list: number[] = [1, 2, 3];
    ```

2. Generic array type 을 사용하는 것이다. `Array<elemType>`

    ```typescript
    let list: Array<number> = [1, 2, 3];
    ```

### Tuple
Tuple 은 길이가 정해진 array 를 표현하기 위해 사용한다.

```typescript
let x: [string, number];
x = ["hello", 10];
```

알려진 index 안의 element 를 접근할 때는 correct type 이 retrieved 된다. 

```typescript
console.log(x[0].substr(1)); // OK
console.log(x[1].substr(1)); // Error, 'number' does not have 'substr'
```

알려진 index 밖의 element 를 접근할 때는 union type 이 사용된다.

```typescript
x[3] = "wolrd"; // OK, 'string' can be assigned to 'string | number'
console.log(x[5].toString()); // OK, 'string' and 'number' both have 'toString'
x[6] = true; // Error, 'boolean' isn't 'string | number'
```

### Enum
Enum 은 numeric values 의 set 에 좀더 friendly name 을 줄 때 사용한다.

```typescript
enum Color {Red, Green, Blue}
let c: Color = Color.Green;
```

기본적으로 enum 의 numbering 은 0 부터 시작하지만 setting 가능 하다.

```typescript
enum Color {Red = 1, Green, Blue}
let c: Color = Color.Green;

enum Color {Red = 1, Green = 2, Blue = 4}
let c: Color = Color.Green;
```

Numeric value 를 이용해서 enum 의 name 을 알아낼 수도 있다.

```typescript
enum Color {Red = 1, Green, Blue}
let colorName: string = Color[2];

alert(colorName);
```

### Any
Application 을 작성할 당시에는 type 을 알 수 없는 variable 을 사용하고 
싶을 수 있다. 이 때 `any` type 을 사용하여, compile-time check 를 통과 
시킬 수 있다.

```typescript
let notSure: any = 4;
notSure = "maybe a string instead;
notSure = false; // okay, definitely a boolean
```

다른 언어와 다르게 `Object` 는 바꿀 수도, method 를 call 할 수도 없다.

```typescript
let notSure: any = 4;
notSure.ifItExists(); // okay, ifItExists might exist at runtime
notSure.toFixed(); // okay, toFixed exists (but the compiler doesn't check)

let prettySure: Object = 4;
prettySure.toFixed(); // Error: Property 'toFixed' doesn't exist on type 'Object'
```

### Void
`void` 는 `any` 와는 반대로 어떤 type 도 오지 않을 때 쓰인다. 
어떠한 값도 return 하지 않는 함수의 return type 으로 쓰인다.

```typescript
function warnUser(): void {
    alert("This is my warning message");
}
```

`void` type 의 변수에는 `undefined` 와 `null` 만 올 수 있다.

```typescript
let unusable: void = undefined;
```

### Null and Undefined
TypeScript 에서 `null` 과 `undefined` 는 `null` 과 `undefined` 라는 type 
을 각각 가지고 있다.

```typescript
// Not much else we can assign to these variables!
let u: undefined = undefined;
let n: null = null;
```

기본적으로 `null` 과 `undefined` 는 모든 type 의 subtype 이다. 
그러나 `--strictNullChecks` flag 를 사용하면 `null` 과 `undefined` 는 오직 
`void` 와 각각의 type 에만 assign 가능해 진다.

### Never
`never` type 은 절대 발생하지 않는 value 의 type 이다. 예를 들어 `never` 
type 은 언제난 exception 을 발생시키는 function 의 return type 이다. 
Variables also acquire the type never when narrowed by any type 
guards that can never be true.

`never` type 은 모든 type 의 subtype 이지만 어떠한 type 도 `never` type 의
subtype 이 아니다. 즉 `any` 도 `never` 에는 assign 이 불가능 하다.

```typescript
// Function returning never must have unreachable end point
function error(message: string): never {
    throw new Error(message);
}

// Inferred return type is never
function fail() {
    return error("Something failed");
}

// Function returning never must have unreachable end point
function infiniteLoop(): never {
    while (true) {
    }
}
```

### Type assertions
*Type assertions* 별다른 checking 이나 data 의 reconstruting 없이 
type cast 하는 것과 비슷하다. 즉 내가 value 의 type 을 compiler 보다 
더 잘 알고 있을 때, compiler 에게 나를 믿어 달라고 말하는 것이다.

Type assertions 은 두 가지 form 이 존재한다.

1. "angle-bracket" syntax

    ```typescript
    let someValue: any = "this is a string";
    let strLength: number = (<string>someValue).length;
    ```

2. `as` syntax

    ```typescript
    let someValue: any = "this is a string";
    let strLength: number = (someValue as string).length;
    ```
