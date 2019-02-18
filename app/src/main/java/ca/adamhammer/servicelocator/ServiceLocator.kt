package ca.adamhammer.servicelocator

/**
 * This is a ServiceLocator
 *
 * By default it can load any Service with a no-args constructor
 *
 * Kotlin
 * val yourService = get(yourService::class.java)
 *
 * Java
 * YourService service = get(yourService.class)
 *
 * If it needs additional configuration the initializer can be set
 * Setting the initializer will clear and re-initialize all services
 * See CustomServiceLocatorTest for Example of custom configuration
 */
@Suppress("UNCHECKED_CAST")
object ServiceLocator {
    private val serviceMap = HashMap<Any, Any?>()

    var initializer: ServiceInitializer = DefaultServiceInitializer
    set(value) {
        //Setting the initializer will restart the ServiceLocator (Restart all Services)
        field = value
        serviceMap.clear()
    }


    /**
     * Gets a class, initializes if necessary
     */
    fun <T> get(_class: Class<T>): T {
        if (!serviceMap.containsKey(_class)) {
            serviceMap[_class] = initializer.getInstance(_class)
        }
        return serviceMap[_class] as T
    }
}

/**
 * A service initializer
 *
 * Gets an instance of _class when asked (Creates/Forwards)
 */
interface ServiceInitializer {
    fun <T> getInstance(_class: Class<T>): T
}

object DefaultServiceInitializer : ServiceInitializer {
    override fun <T>getInstance(_class: Class<T>): T = _class.newInstance()
}

/**
 * This is a MapServiceInitializer that takes a map
 */
open class MapServiceInitializer(private val serviceMap:Map<Class<*>, ()->Any>) : ServiceInitializer {
    override fun <T> getInstance(_class: Class<T>): T {
        val initFunction = serviceMap[_class]
        if (initFunction != null) {
            return initFunction() as T
        }

        return DefaultServiceInitializer.getInstance(_class)
    }
}

