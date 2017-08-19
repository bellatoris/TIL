# Item 3

## Enforce the singleton property with a private constructor or an enum type

A *signleton* is simly a class that is instantiated exactly once.
**Maing a class a singleton can make it difficult to test its clients,** 
as it's impossible to substitute a mock implementation for a singleton 
unless it implements an interface that serves as its type.

Before release 1.5, there were two ways to implement singletons.

1. The member is a final field:

    ```java
    // Singleton with public final field
    public class Elvis {
        public static final Elvis INSTANCE = new Elvis();
        private Elvis() { ... }

        public void leaveTheBuilding() { ... }
    }
    ```

    A privileged client can invoke the private constructor reflectively 
    with the aid of the `AccessibleObject.setAccessible` method. If you 
    need to defend against this attack, modify the constructor to make it 
    throw an exception if it's asked to create a second instance.

    ```java
    import java.lang.reflect.Constructor;

    public class PrivateInvoker {
        public static void main(String[] args) throws Exception {
            Constructor<?> con = Private.class.getDeclaredConstructors()[0];
            con.setAccessible(true);
            Private p = (Private) con.newInstance();
        }
    }

    class Private {
        private Private() {
            System.out.println("Hello!");
        }
    }
    ```

2. Public member is a static factory method:

    ```java
    // Singleton with static factory
    public class Elvis {
        private static final Elvis INSTANCE = new Elvis();
        private Elvis() { ... }
        public static Elvis getInstance() { return INSTANCE; }

        public void leaveTheBuilding() { ... }
    }
    ```

    This approach gives you the flexibility to change your mind about 
    whether the class should be a singleton without chaing its API.

To make a singleton class *serializable*, it is not sufficient merely to 
add `implements Serializable` to its declaration. To maintain the singleton 
guarantee, you have to declare all instance fields `transient` and provide 
a `readResolve` method.

```java
// readResolve method to preserve singleton property
private Object readResolve() {
    // Return the one true Elvis and let the garbage collector
    // take care of the Elvis impersonator.
    return INSTANCE;
}
```

As of release 1.5, there is a third approach to implementing singletons. 
Simply make an enum type with one element:

```java
// Enum singleton - the preferred approach
public enum Elvis {
    INSTANCE;

    public void leaveTheBuilding() { ... }
}
```

This approach is functionally equivalent to the public field approach, except 
that it is more concise, provides the serialization machinery for free, and 
provides an ironclad guarantee against multiple instantiation, even in the 
face of sophisticated serialization or reflection attacks.

**A single-element enum type is the best way to implement a singleton.**
