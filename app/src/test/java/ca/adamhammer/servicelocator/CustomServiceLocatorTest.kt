package ca.adamhammer.servicelocator

import ca.adamhammer.servicelocator.ServiceLocator.get
import org.junit.Before
import org.junit.Test

class CustomServiceLocatorTest {
    @Before
    fun setup() {
        //This initializes by class name
        ServiceLocator.initializer = object : ServiceInitializer {
            val serviceMap = mapOf<Class<*>, ()->Any>(
                iA::class.java to { provideClassA() },
                iB::class.java to {  cB("Class B")  }
            )

            //Can have functions to generate classes
            fun provideClassA() : cA {
                return cA("Class A")
            }

            override fun <T> getInstance(_class: Class<T>): T {
                val initFunction = serviceMap[_class]
                if (initFunction != null) {
                    return initFunction() as T
                }

                return DefaultServiceInitializer.getInstance(_class)
            }
        }
    }

    @Test
    fun basicServiceTest() {
        get(iA::class.java).invokeA()
    }

    @Test
    fun basicServiceTestReverseDependencies() {
        get(cC::class.java).invokeC()
        get(iB::class.java).invokeB()
        get(iA::class.java).invokeA()
    }


    @Test
    fun basicServiceTestReverseDependencies2() {
        get(iB::class.java).invokeB()
        get(iA::class.java).invokeA()
    }



    interface iA {
        fun invokeA(): Any
    }

    interface iB {
        fun invokeB(): Any
    }

    /**
     * Classes, A depends on B which depends on C
     */
    class cA (val name : String): iA {
        init {
            System.out.println("$name initialized")
        }

        override fun invokeA() {
            System.out.println("Invoking for $name")
            get(iB::class.java).invokeB()
        }
    }

    class cB (val name : String): iB {
        init {
            System.out.println("$name initialized")
        }

        override fun invokeB() {
            System.out.println("Invoking for $name")
            get(cC::class.java).invokeC()
        }
    }

    class cC  {
        init {
            System.out.println("Concrete class C initialized no-args, auto")
        }


        fun invokeC() {
            System.out.println("Invoking for Concrete no-args class")
        }
    }



}

