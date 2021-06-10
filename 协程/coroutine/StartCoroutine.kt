package com.lonbon.kotlin.coroutine

import kotlinx.coroutines.delay
import kotlin.coroutines.*

fun main() {
    launchCoroutineTest()
}

fun createTest() {
    /**
     * 创建出Coroutine协程对象中存在suspend {} 协程体和 协程执行完成的回调对象。
     */
    val coroutine = createACoroutine()

    /**
     * resume本质上函数调用的是resumeWith()。
     * resumeWith -> 从协程中返回的结果封装成Result<String>
     */
    coroutine.resume(Unit)
}

fun createACoroutine() = suspend {
    "kotlin"
}.createCoroutine(object : Continuation<String> {
    override val context: CoroutineContext
        get() = EmptyCoroutineContext

    override fun resumeWith(result: Result<String>) {
        println("result:${result}")
    }

})

fun startTest() {
    startCoroutine()
}


fun startCoroutine() = suspend {
    "kotlin22"
}.startCoroutine(object : Continuation<String> {
    override val context: CoroutineContext
        get() = EmptyCoroutineContext

    override fun resumeWith(result: Result<String>) {
        println("result: $result")
    }

})

/*
*   带 Receiver 的协程体
*   createCoroutine()
*   startCoroutine()
*   上面的两个Api进行分别的作用在于创建协程和启动协程。
* */


/**

public fun <T> (suspend () -> T).createCoroutine(
completion: Continuation<T>
): Continuation<Unit> =
SafeContinuation(createCoroutineUnintercepted(completion).intercepted(), COROUTINE_SUSPENDED)


public fun <R, T> (suspend R.() -> T).createCoroutine(
receiver: R,
completion: Continuation<T>
): Continuation<Unit> =
SafeContinuation(createCoroutineUnintercepted(receiver, completion).intercepted(), COROUTINE_SUSPENDED)

上面两个函数都是创建协程的函数。但是下面的创建的协程Receiver可以看到是R，而上面的接收者是不存在的。
lambda表达式并且带Receiver在kotlin中没有相对应的语法可以完成。所以需要使用其他的方式来实现。
 */
fun <R, T> launchCoroutine(receiver: R, block: suspend R.() -> T) {
    block.startCoroutine(receiver, object : Continuation<T> {
        override val context: CoroutineContext = EmptyCoroutineContext

        override fun resumeWith(result: Result<T>) {
            println("result: $result")
        }

    })
}

/**
 * 使用class封装function的形式来完成接收Receiver的作用
 */
class ProduceScope<T> {
    suspend fun produce(value: T) {
        value
    }
}

fun launchCoroutineTest() {
    launchCoroutine(ProduceScope<Int>()) {
        println("In Coroutine")
        println(produce(1024))
        delay(1000)
        println(produce(2048))
    }
}

