# Item 1 

## Consider static factory methods instead of constructors

```java
public static Boolean valueOf(boolean b) {
    return b ? Boolean.TRUE : Boolean.FALSE;
}
```

### Advantage of static factory methods
1. **Unlike constructors, they have names.** Parameters to a constructor do 
not describe the object being returned, a static factory with a well-chosen 
name is easier to use and the resulting client code easier to read.

2. **Unlike constructors, they are not required to create a new object each 
time they're invoked.** This allows immutable classes 
[Item 15](../../Chatper3/Item15/Readme.md) to use preconcstructed instances, 
or to cache instances as they're constructed, and dispense them repeatedly to 
avoid creating unnecessary duplicate objects.  
The ability of static factory methods to return the same object from repeated 
invocations allows classes to maintain strict control over what instances 
exists at any time.

3. **Unlike constructors, they can return an object of any subtype of their 
return type.** This gives you great flexibility in choosing the class of the 
returned object. One application of this flexibility is that an API can return 
objects without making their class public. Not only can the class of an 
object returned by a public static factory method be nonpublic, but the 
class can vary from invocation to invocation depending on the values of the 
parameters to the static factory.

4. **They reduce the very bosity of creating parameterized type instances.**

```java
Map<String, List<String>> m = 
    new HashMap<String, List<String>>();

// In HashMap Implementation
public static <K, V> HashMap<K, V> newInstance() {
    return new HashMap<K, V>();
}

// We can use like this
Map<String, List<String>> m = HashMap.newInstance();
```

In today, however, Java perform this sort of type inference on constructor.

### Disadvantage of static factory methods

1. **Classes without public or protected constructors cannot be subclassed.** 
The same is true for nonpublic classes returned by public static factories. 
For example, it is impossible to subclass any of the convenience implementation 
clases in the Collections Framework.

2. **They are not readily distinguishable from other static methods.**

### Summary
Static factory methods and public constructors both have their uses, and it 
pays to understand their relative merits. Often static factories are preferable, 
so avoid the reflex to provide public constructors without first considering 
static factories.
