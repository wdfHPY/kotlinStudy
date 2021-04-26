package com.lonbon.kotlin

import kotlinx.coroutines.*


fun main() = runBlocking { coroutine4Test4() }


/*
*   组合挂起函数：
*   顺序调用并不能来并发执行多个挂起函数
*   通过结果可以看出。【result is: 567, Cost Time is 2019】
*   两个挂起函数之间并没有产生并发的效果。
* */
fun coroutine3Test1() = runBlocking {
    val startTimeMs = System.currentTimeMillis()
    val a = doSomeThingA()
    val b = doSomeThingB()
    println("result is: ${a + b}, Cost Time is ${System.currentTimeMillis() - startTimeMs}")
}

/**
 * 下面的问题就在于如何使用协程来进行并发呢？
 * 首先需要记住一点的就是。 协程的并发一定会是显式的。
 * 1. 使用async来启动协程的并发。
 * 2. 使用await来返回协程的值。
 * 结果为： 【result is 567, Cost Time is 1025】 -> 可以看到采用并发之后，效率高了两倍。
 */
fun coroutine3Test3() = runBlocking {
    val startTimeMs = System.currentTimeMillis()
    val a = async { doSomeThingA() }
    val b = async { doSomeThingB() }
    println("result is ${a.await() + b.await()}, Cost Time is ${System.currentTimeMillis() - startTimeMs}")
}

/**
 * 如果将 async 的启动参数设置为 LAZY 之后，那么只有在并发协程Job.Deferred之后await 或者协程start()才会去运行协程体。
 * 如果加上 LAZY 启动并且没有使用start()函数的话，那么此时会按照默认的启动顺序来启动。
 */
fun coroutine3Test2() = runBlocking {
    val startTimeMs = System.currentTimeMillis()
    val a = async(start = CoroutineStart.LAZY) { doSomeThingA() }
    val b = async(start = CoroutineStart.LAZY) { doSomeThingB() }
    /*  a.start()
      b.start()*/
    println("result is ${a.await() + b.await()}, Cost Time is ${System.currentTimeMillis() - startTimeMs}")
}

/**
 * 使用Async来进行结构性并发。kotlin 中async并发是支持结构性并发的。本身， async 就是Scope 的扩展函数。
 * 通过scope的控制流可以很轻松进行结构性并发。
 *
 * 目前a和b 都在一个作用域中，一旦作用域产生了Exception的话，那么整个作用域中协程都将取消。使用Scope来结构化并发
 * a 和 b。
 *
 * 使用 Async 的结构化并发可以便捷的管理并发的控制流程。封装的在scope里面来记性结构性并发。
 */

suspend fun coroutine4Test3(): Int = coroutineScope {
    val a = async(start = CoroutineStart.LAZY) { doSomeThingA() }
    val b = async(start = CoroutineStart.LAZY) { doSomeThingB() }
    a.await() + b.await()
}

/**
 * 多并发抛出了异常。封装在scope作用域所有协程都会被取消。
 * 其父协程也取消掉了。
 */
fun coroutine4Test4() = runBlocking<Unit> {
    try {
        failedConcurrentSum()
    } catch (e: ArithmeticException) {
        println("Computation failed with ArithmeticException")
    }
}

/**
 * 使用Scope 来封装 one 和 two 协程的并发。使 one 和  two的控制流程现在在
 * 这个作用域当中。
 */
suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async<Int> {
        try {
            delay(Long.MAX_VALUE) // Emulates very long computation
            42
        } finally {
            println("First child was cancelled")
        }
    }
    val two = async<Int> {
        println("Second child throws an exception")
        throw ArithmeticException()
    }
    one.await() + two.await()
}


/**
 * 挂起函数A
 */
suspend fun doSomeThingA(): Int {
    delay(1000)
    return 9
}

/*
*  挂起函数B
* */
suspend fun doSomeThingB(): Int {
    delay(1000)
    return 558
}