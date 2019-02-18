package ca.adamhammer.servicelocator;

public class ServiceLocatorJavaHelper {
    private ServiceLocatorJavaHelper() {}

    /**
     * This is a API helper for the service locator in Java
     * since it's preferable to use from Static import
     */
    public static <T> T get(Class<T> _class) {
        return ServiceLocator.INSTANCE.get(_class);
    }
}
