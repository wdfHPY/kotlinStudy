package com.lonbon.kotlin.currency

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

@Volatile
var counter = 0

fun main() {
    resourceTest3()
}

fun resourceTest1() = runBlocking {
    withContext(Dispatchers.Default) {
        coroutineUseResource {
            counter2.incrementAndGet()
        }
    }
    println("result is ${counter2}")
}

suspend fun coroutineUseResource(action: () -> Unit) {
    val coroutineNumbers = 100 //开启100个协程
    val actionNumbers = 1000 //每一个协程对共享的变量操作1000此
    val time = measureTimeMillis {
        coroutineScope {
            repeat(coroutineNumbers) {
                launch {
                    repeat(actionNumbers) {
                        action()
                    }
                }
            }
        }
    }
    println(("Completed ${coroutineNumbers * actionNumbers} actions in $time "))

}


/*
*   1. 解决多线程使用共享的数据的问题。可以使用线程安全的数据结构。这是最快解决多线程并发共享量的问题。
    很显然  Int 值就不是一个线程安全的数据结构。
    2. Int 的线程安全的数据结构是 AtomicInteger。使用递增或者递减也可以使用相对应的方法。
*  */

val counter2 = AtomicInteger()

/*
*   1. 限制共享变量的访问在单线程之中。无论是粗粒度还是细粒度来说，本质上都是限制共享变量的访问在单线程的环境
*   当中的。
* */

val singleContext = newSingleThreadContext("Single") //粗粒度去限制线程的访问。限制在单个线程中。

fun resourceTest2() = runBlocking {
    withContext(Dispatchers.Default) {
        coroutineUseResource2 {
            counter++
        }
    }
    println("result is ${counter}")
}

suspend fun coroutineUseResource2(action: () -> Unit) {
    val coroutineNumbers = 100 //开启100个协程
    val actionNumbers = 1000 //每一个协程对共享的变量操作1000此
    val time = measureTimeMillis {
        coroutineScope {
            repeat(coroutineNumbers) {
                withContext(singleContext) { //-> 以细粒度的限制线程的访问。虽然调用出调度器是多线程调度，但是执行具体操作时还是需要
                    //切换到单个线程环境的。
                    launch {
                        repeat(actionNumbers) {
                            action()
                        }
                    }
                }
            }
        }
    }
    println(("Completed ${coroutineNumbers * actionNumbers} actions in $time "))

}

/*
*   上面的做法在于限制线程的粒度。Java中可以使用锁来互斥的访问共享的变量。在kotlin中使用Mutex()的锁来互斥访问。
* */

val mutex = Mutex()

fun resourceTest3() = runBlocking {
    withContext(Dispatchers.Default) {
        coroutineUseResource3 {
/*            mutex.withLock {
                counter++
            }*/
        }

    }
    println("result is ${counter}")
}

fun CoroutineScope.coroutineUseResource3(action: () -> Unit) {
    val coroutineNumbers = 100 //开启100个协程
    val actionNumbers = 1000 //每一个协程对共享的变量操作1000此
    val time = measureTimeMillis {
        repeat(coroutineNumbers) {
            launch {
                repeat(actionNumbers) {
                    action()
                }
            }

        }
    }
    println(("Completed ${coroutineNumbers * actionNumbers} actions in $time "))
}
