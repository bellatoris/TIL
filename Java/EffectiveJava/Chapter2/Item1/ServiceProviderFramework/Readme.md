# Service Provider Frameworks
A service provider framework is a system in which multiple 
service providers implement a service, and the system makes the 
implementations available to its clients, decoupling them from the 
implementations. 

* Service interface: providers implement
* Provider registration API: system uses to register implementations
* Service access API: clients use to obtain an instance of the service
* Service provider interfcae: providers implement to create instances of 
their service implementation.

The service access API typically allows but does not require the client to 
specify some criteria for choosing a provider. In the absence of such 
specification, the API returns an instance of a default implementation.

```java
// Service provider framework sketch

// Service interface
public interface Service {
    ... // Service-specific methods go there
}

// Service provider interface
public interface Provider {
    Service newService();
}

// Noninstantiable class for service registration and access
public class Services {
    private Services() { }  // Prevents instantiation (Item 4)

    // Maps service names to services
    private static final Map<String, Provider> providers = 
        new ConcurrentHashMap<String, Provider>();
    public static final String DEFAULT_PROVIDER_NAME = "<def>";

    // Provider registration API
    public static void registerDefaultProvider(Provider p) {
        registerProvider(DEFAULT_PROVIDER_NAME, p);
    }
    public static void registerProvider(String name, Provider p) {
        providers.put(name, p);
    }

    // Service access API
    public static Service newInstance() {
        return newInstance(DEFAULT_PROVIDER_NAME);
    }
    public static Service newInstance(String name) {
        Provider p = provider.get(name);
        if (p == null)
            throw new IllegalArgumentException(
                "No provier registered with name: " + name);
        return p.newService();
    }
}
```
