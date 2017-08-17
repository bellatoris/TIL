# `const` declarations

`let` 과 비슷하지만 한번 할당된 value 를 바꿀 수 없다. 그러나 
그들이 참조하고 있는 값이 *immutable* 한 것은 아니다.

```typescript
const numLivesForCat = 9;
const kitty = {
    name: "Aurora",
    numLives: numLivesForCat,
}

// Error
kitty = {
    name: "Danielle",
    numLives: numLivesForCat
};

// all "okay"
kitty.name = "Rory";
kitty.name = "Kitty";
kitty.name = "Cat";
kitty.numLives--;
```

`const` variable 의 internal state 는 변경 가능 하다.
