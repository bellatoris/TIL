# Item 4

## Enforce noninstantiability with a private constructor

Occasionally you'll want to write a class that is just a grouping of 
static methods and static fields.

* They can be used to group related methods on primitive values of arrays.
    * E.g., `java.lang.Math` or `java.util.Arrays`
* They can be used to group static methods, including factory methods 
[Item1](../Item1/Readme.md), for objects that implement a particular 
interface.
    * E.g., `java.util.Collections`
* They can be used to group methods on a final class, instead of 
extending the class.

Such *utility classes* were not designed to be instantiated. **Attempting to 
enforce noninstantiability by making a class abstract does not work.** The 
class can be subclasses and the subclass instantiated. So **a class can be 
made noninstantiable by including a private constructor.**

```java
// Noninstantiable utility class
public class UtilityClass {
    // Suppress default construcotr for noninstantiability
    private UtilityClass() {
        throw new AssertionError();
    }
    ... // Remainder omitted
}
```

This idiom also prevents the class from being subclasses. All constructors must 
invoke a superclass construcotr, explicitly or implicitly, and a subclass 
would have no accessible superclass constructor to invoke.
