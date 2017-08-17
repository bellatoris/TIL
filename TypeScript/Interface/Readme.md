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

var user = { firstName: "Jane", lastName: "User" };

document.body.innerHTML = greeter(user);
```
