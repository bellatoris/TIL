# Class

## Difference between the static and instance sides of classes

Class 는 static side 의 type 과 instance side 의 type 을 가지고 있다. 
Construct 를 가지고 있는 interface 를 implement 한 class 를 만들려고 하면 
에러가 발생한다. Class 가 interface 를 implement 할 때 instance side 만 
check 할 뿐, static side 는 체크하지 않는다. Constructor 는 static side 
에 있기 때문에 check 시에 포함되지 않는다.

```typescript
interface ClockConstructor {
    new (hour: number, minute: number): ClockInterface;
}
interface ClockInterface {
    tick();
}

function createClock(ctor: ClockConstructor,
                     hour: number,
                     minute: number): ClockInterface {
    return new ctor(hour, minute);
}

class DigitalClock implements ClockInterface {
    constructor(h: number, m: number) { }
    tick() {
        console.log("beep beep");
    }
}
class AnalogClock implements ClockInterface {
    constructor(h: number, m: number) { }
    tick() {
        console.log("tick tock");
    }
}

let digital = createClock(DigitalClock, 12, 17);
let analog = createClock(AnalogClock, 7, 32);
```
