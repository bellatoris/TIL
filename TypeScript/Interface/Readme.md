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

Index signature 에는 string 과 number 만 사용 가능하다. JavaScript 에서 
number 로 indexing 해도 string 으로 converting 해서 indexing 한다. 
그러므로 numeric indexer 의 return type 은 string indexer 의 return type 의 
subtype 이어야 한다.

String index 의 `obj.property` 는 `obj['property']` 와 동일하다. 그렇기 때문에 
모든 property 들을 index signature 의 return type 과 동일하도록 강제한다. 

```typescript
interface NumberDictionary {
    [index: string]: number;
    length: number ;    // ok, length is a number
    name: string;       // error, the type of 'name' is not a subtype of the indexer
}
```

### Extending Interfaces

Class 와 마찬가지로 interface 는 서로 extends 가능하다.

```typescript
interface Shape {
    color: string;
}
interface Square extends Shape {
    sideLength: number;
}

let square = <Square>{};
square.color = "blue";
square.sideLength = 10;
```

### Hybird Types

JavaScript 가 dynamic 하고 flexible 하기 때문에 여러 type 의 combination 인 
object 가 있을 수 있다. 

```typescript
interface Counter {
    (start: number): string;
    interval: number;
    reset(): void;
}

function getCounter(): Counter {
    let counter = <Counter>function (start: number) { };
    counter.interval = 123;
    counter.reset = function() { };
    return counter;
}

let c = getCounter();
c(10);
c.reset();
c.interval = 5.0;
```

### Interfaces Extending Classes

Interface 가 class 를 extends 하면 class 의 member 만 inherit 한다. 
또한 class 의 private, protected member 를 모두 inherit 하기 때문에, 
해당 interface 를 구현할 수 있는건 그 class 나 그 class 의 subclass 뿐이다. 
이 기능은 상속 계층이 클때, 특성 property 를 가진 subclass 에서만 동작하도록 
하고 싶을 때 유용하다.

```typescript
class Control {
    private state: any;
}

interface SelectableControl extends Control {
    select(): void;
}

class Button extends Control {
    select() { }
}

class TextBox extends Control {
    select() { }
}

class Image {
    select() { }
}

class Location {
    select() { }
}
```

`SelectableControl` 은 `Control` 의 모든 memeber 를 들고 있다. `state` 가 
private member 이기 때문에 `Control` 의 subclass 만이 `SelectableControl` 을 
implement 할 수 있다. `Control` class 안에서 private member 인 `state` 를 
`SelectableControl` 의 instance 를 통해서 접근 할 수 있다. 
