package ca.adamhammer.servicelocator

import ca.adamhammer.servicelocator.ServiceLocator.get
import org.junit.Test

class ServiceLocatorTest {
    @Test
    fun basicServiceTest() {
        //This initializes by class name
        ServiceLocator.initializer = DefaultServiceInitializer
        val classA = get(ClassA::class.java)
        Thread.sleep(2000)
        classA.dependsOnB()
    }


    @Test(expected = InstantiationException::class)
    fun getUnavailableServiceWithArgConstructor() {
        //We can't get classC, because it has an Argument constructor
        //
        val classC : ClassC = get(ClassC::class.java)
    }


    class ClassA {
        init {
            System.out.println("Class A Initializing")
        }

        fun dependsOnB() {
            System.out.println("Calling something in A that depends on B")
            get(ClassB::class.java).doSomething()
        }
    }

    class ClassB {
        init {
            System.out.println("Class B Initializing")
        }


        fun doSomething() {
            System.out.println("Class B is doing something")
        }
    }

    class ClassC (val argConstructor : String)
}

