package com.lonbon.kotlin

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.math.BigInteger
import java.util.*

val myFlow1 = flow {
    repeat(20) {
        delay(10000)
        emit(it)
    }
}


val myFlow2 = flow {
    repeat(20) {
        delay(100)
        emit("case: $it")
    }
}


fun main() {
    doCoroutineTest()
}

/**
 * launch 函数时定义在CoroutineScope 上的。定义成CoroutineScope的扩展函数的话，那么此时调用函数会直接返回，剩下的代码
 * 和caller的代码进行并发执行。
 */
fun withOutCoroutineScope() = runBlocking {
    launch {
        myFlow1.collect {
            println(it)
        }
    }

    launch {
        myFlow2.collect {
            println(it)
        }
    }

    println("launch complete")
}

/**
 * suspend function 仍然是顺序执行的。挂起 suspend 本身来说和并发一点关系都没有，那么kotlin 中难道不支持并发？ 不 ，kotlin 中是支持并发的。
 * 但是并发和 suspend 之间不存在任何的联系。 kotlin 中的并发是通过 CoroutineScope 结构化并发来支持的，而 suspend 仅仅是提供挂起/恢复的功能。
 * suspend 的本质： suspend的本质是一个约定。在 kotlin 中，suspend 关键字也是仅作为 key word 来标志某一个函数是挂起函数而已。真正实现挂起功能
 * 是withContext + Dispatcher 来实现的。
 * suspend 是在 block Thread 上定义出的概念。Block Thread 会 因为 CPU / IO的耗时操作来 阻塞 Caller 线程。这样的话，Caller
 * 者线程完全没有办法处理任何的任务了。 suspend 概念就诞生在Block Thread 之上， suspend 被约定为：不会因为 CPU/IO 类型的耗时操作来阻塞调用者
 * 的线程。而调用suspend function 函数的地方称为 suspend point 挂起点。 挂起函数的返回也是返回到这个挂起点的，所以这也就解释了 suspend function
 * 本质上还是顺序执行的，他仅仅是结束了回调写法。将代码的逻辑从异步调用转化为同步调用而已。
 */
suspend fun obersveFunction() {
    withContext(Dispatchers.Default) {
        delay(10000)
        println("No CoroutineScope")
    }
}

/**
 * kotlin 的并发 是不是类似 Thread Future callBack的并发原语。他是基于新型的控制流， Thread future callBack
 * 本质上是基于 go like 的控制流，go like 的控制流本质上是 goto 中的一种。goto 控制流和普通的Structure  Flow 相比较
 * 来说存在很大弊端。1. go like 破坏了逻辑的局部推理。 2. 无法定位产生问题的位置。 ....
 * 所以 Thread future callback 并发 显得难以控制和管理。因为本质上控制流就存在弊端。
 *
 * 而kotlin 中的结构化并发抛弃了 go like 的控制流。他将并发的控制限制在一个结构之内（在kotlin 中这个结构就称为Scope。）
 * 一个结构化并发体并不会影响到另外一个结构化并发体的内部。在结构的内部，可以方便的对并发进行控制。一个结构体中逻辑是Black Box，是一个
 * 黑盒，那么就可以进行逻辑局部推理，可以方便对异常进行处理。这一切都是基于结构化好处。
 */

/**
 * scopeFunction() 就是一个suspend Function。他的scopeFunction目前就不涉及到任何的并发。在doCoroutineTest()中調用了scopeFunction。他执行的顺序仍然是
 * 先执行scopeFunction()执行println("scopeFunction!!")，然后执行println("suspend Function Execute!")这句话。
 * 所以suspend函数在代码是顺序执行的。 单 suspend 修饰的函数默认运行在主线程。并且 suspend 函数 和的 call suspend 函数执行是顺序执行的。
 * 下面scopeFunction和scopeFunction2 证明了挂起函数的确的实现了 在不阻塞call suspend function 线程下执行一段代码。
 *
 * 通过打印 Thread 的 state 可以发现调用suspend 的时候，被调用的线程处于 TIMED_WAITING 的状态并不是阻塞的状态。
 */
suspend fun scopeFunction(thread:Thread): Int {
    println("threadName Alive :${thread.state}")
    println("threadName Name :${thread.name}")
    return withContext(Dispatchers.IO) {
        delay(10000)
        println("scopeFunction!! ${Thread.currentThread().name}")
        println("threadName Alive: ${thread.state}")
        9
    }
}

suspend fun scopeFunction2(thread: Thread): Int {
    println(thread.name)
    delay(10000)
    println(thread.isAlive)
    return 9
}

fun doCoroutineTest() = runBlocking {
    println("suspend Function Execute! ${scopeFunction(Thread.currentThread())}")
}

fun doCoroutineTest2() = runBlocking {
    println("suspend Function Execute! ${coroutineSuspend()}")
}


/**
 * 虽然看到suspend 的挂起和恢复不会阻塞掉调用者的线程，那么如何实现并发？ kotlin的并发是通过CoroutineScope来完成的。
 * 单纯的suspend 不可能实现代码和代码的并发，但是suspend是实现Coroutine的基础。
 *
 * 使用launch{} 协程构建器构建一个Coroutine。构建出Coroutine之后会立刻返回。
 * 下面就是这个约定：
 * every function that is declared as extension on CoroutineScope returns immediately,
 * but performs its actions concurrently with the rest of the program.在 CoroutineScope 上声明为扩展的每个函数都会立即返回，
 * 但是它的操作会与程序的其余部分并发执行。
 *
 * 所以下面的 函数签名是错误的： `suspend fun CoroutineScope.obfuscate(data: Data)`
 *
 * 这个函数能做什么？它是一个暂停函数，所以我们知道，根据定义，它可以暂停协同程序的执行。但它也被定义为 CoroutineScope
 * 的一个扩展，因此它可以启动一个新的协同程序来做一些与程序的其余部分同时工作的事情。那它为什么要暂停执行呢？
 * 其操作的哪些部分是同时执行的？如果不阅读它的文档和/或源代码，就无法判断。
 *
 * 另一方面，如果我们选择将它声明为一个挂起函数: suspend fun obfuscate(data: Data)  然后，按照惯例，我们知道它在不阻塞调用者的情况下执行其混淆操作，并在完成后返回给调用者。
 *
 * 或者，如果我们选择将其声明为 CoroutineScope 扩展:
 * fun CoroutineScope.obfuscate(data: Data) 然后，根据约定，我们知道它会立即返回，而不会阻塞调用方，并开始与程序的其余部分并发地执行其模糊处理。
 */

suspend fun CoroutineScope.coroutineSuspend() = this.launch {
    delay(10000)
    println("do Suspend")
}

suspend fun orderExecute(funct: suspend () -> Unit) {
    funct.invoke()
    print("只有在挂起函数返回后执行")
}

fun CoroutineScope.suspendNotBlockThread() = launch {
    println("Before Call Suspend Function")
//    scopeFunction()
    println("After Call Suspend Function")
}

suspend fun findBigPrime(): BigInteger =
    withContext(Dispatchers.Default) {
        BigInteger.probablePrime(4096, Random())
    }

suspend fun main3() {
    findBigPrime()
}
