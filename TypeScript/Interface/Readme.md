# Interface

## Duck Typing
TypeScript 는 두 type 의 내부 구조가 compatiable 하면 type 또한 compatible 
하다. 그렇기에 Interface 를 정의하고 나서 Interface 를 implements 할 때, 
`implements` clause 없이도 가능하다.

```typescript
interface Person {
    firstName: string;
    lastName: string;
}

function greeter(person: Person) {
    return "Hello, " + person.firstName + " " + person.lastName;
}

let user = { firstName: "Jane", lastName: "User" };

document.body.innerHTML = greeter(user);
```

오직 shape 만 type 을 check 하는데 영향을 끼친다.

## Optional Properties

모든 property 가 필요하지 않을 수 있다. 그럴 때는 optional property 를 
사용하면 된다. Property 의 이름 앞에 `?` 를 붙이면 해당 property 는 
optional property 가 된다.

```typescript
interface SquareConfig {
    color?: string;
    width?: number;
}

function createSquare(config: SquareConfig): {color: string; area: number} {
    let newSquare = {color: "white", area: 100};
    if (config.color) {
        newSquare.color = config.color;
    }
    if (config.width) {
        newSquare.area = config.width * config.width;
    }
    return newSquare;
}

let mySquare = createSquare({color: "black"});
```

### Readonly Properties
생성이후에 property 의 값이 불변하게 하고 싶으면 property 의 이름앞에 
`readonly` 를 붙이면 된다.

```typescript
interface Point {
    readonly x: number;
    readonly y: number;
}

let p1: Point = { x: 10, y: 20 };
p1.x = 5; // error!
```

### Function Types
Interface 는 function types 을 기술 할 수도 있다.

```typescript
interface SearchFunc {
    (source: string, subString: string): boolean;
}
// Once defined, we can use this function type interface like 
// we would other interfaces. Here, we show how you can create 
// a variable of a function type and assign it a function value 
// of the same type.

let mySearch: SearchFunc;
mySearch = function(source: string, subString: string) {
    let result = source.search(subString);
    return result > -1;
}
// For function types to correctly type-check, the names of the 
// parameters do not need to match. We could have, for example, 
// written the above example like this:

let mySearch: SearchFunc;
mySearch = function(src: string, sub: string): boolean {
    let result = src.search(sub);
    return result > -1;
}
```

### Indexable Types
Interface 는 `a[10]` 이나 `ageMap["daniel"]` 같은 indexable type 을 
기술 할 수도 있다.

```typescript
interface StringArray {
    [index: number]: string;
}

let myArray: StringArray;
myArray = ["Bob", "Fred"];

let myStr: string = myArray[0];
```
