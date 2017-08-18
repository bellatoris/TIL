# Item 2

## Consider a builder when faced with many constructor parameters

Static factories and constructors share a limitation: they do not scale 
well to large numbers of optional parameters.

```java
// Telescoping constructor pattern - does not scale well!
public class NutritionFacts {
    private final int servingSize;      // required
    private final int servings;         // required
    private final int calories;         // optional
    private final int fat;              // optional
    private final int sodium;           // optional
    private final int carbonhydrate;    // optional

    public NutritionFacts(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }

    public NutritionFacts(int servingSize, int servings,
            int calories) {
        this(servingSize, servings, calories, 0);
    }

    public NutritionFacts(int servingSize, int servings,
            int calories, int fat) {
        this(servingSize, servings, calories, fat, 0);
    }

    public NutritionFacts(int servingSize, int servings,
            int calories, int fat, int sodium) {
        this(servingSize, servings, calories, fat, sodium, 0);
    }

    public NutritionFacts(int servingSize, int servings,
            int calories, int fat, int sodium, int carbohydrate) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }
}
```

**The telescoping constructor pattern works, but it is hard to write 
client code when there are many parameters, and harder still to read it.**

A second alternative is the *JaveBeans* pattern, in which you call a 
parameterless constructor to create the object and then call setter methods 
to set each parameter of interest.

```java
// JavaBeans Pattern - allows inconsistency, mandated mutability
public class NutritionFacts {
    // Parameters initialized to default values (if any)
    private int servingSize = -1; // Required; no default value
    private int servings = -1;
    private int calories = 0;
    private int fat = 0;
    private int sodium = 0;
    private int carbohydrate = 0;

    public NutritionFacts() { }

    // Setters
    public void setServingSize(int val)     { servingSize = val; }
    public void setServings(int val)        { servings = val; }
    public void setCalories(int val)        { calories = val; }
    public void setFat(int val)             { fat = val; }
    public void setSodium(int val)          { sodium = val; }
    public void setCarbohydrate(int val)    { carbohydrate = val; }
}
```

However, the JavaBeans pattern has serioud disadvantages.

1. Because construction is split across multiple calls, **a JavaBean may be 
in an inconsistent state partway through its construction.**

2. **The JavaBeans pattern precludes the possbility of making a class 
immutable**, and requires added effort to ensure thread safety.

A third alternative is the *Builder* pattern, in which client class calls a 
constructor with all of required parameters and gets a *builder object* 
instead of making the desired object directly. Then the client class setter-
like methods on the builder object to set each optional parameter of interset. 
Finally, the client calls a parameterless `build` method to generate the 
object, which is immutable.

```java
// Builder Pattern
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public static class Builder {
        // Required parameters
        private final int servingSize;
        private final int servings;

        // Optional parameters - initialized to default values
        private int calories      = 0;
        private int fat           = 0;
        private int carbohydrate  = 0;
        private int sodium        = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings    = servings;
        }

        public Builder calories(int val)
            { calories = val;      return this; }
        public Builder fat(int val)
            { fat = val;           return this; }
        public Builder carbohydrate(int val)
            { carbohydrate = val;  return this; }
        public Builder sodium(int val)
            { sodium = val;        return this; }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }

    private NutritionFacts(Builder builder) {
        servingSize  = builder.servingSize;
        servings  = builder.serving;
        calories = builder.calories;
        fat = builder.fat;
        sodium = builder.sodium;
        carbonhydrate = builder.carbonhydrate;
    }
}
```

`NutritionFacts` is immutable, and that all parameter default values are in 
single location.

```java
// Client code
NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8).calories(100)
                                                            .sodium(35)
                                                            .carbohydrate(27)
                                                            .build();
```

This client code is easy to write and **to read**. **The Builder pattern 
simulated named optional parameters** as found in Python.

### Summary
**The Builder pattern is a good choice when designing classes whose 
constructors or static factories would have more than a handful of 
parameters,** especially if most of those parameters are optional.
