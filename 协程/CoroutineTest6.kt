package com.lonbon.kotlin

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

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

fun doCoroutineTest() = runBlocking {
    scopeFunction()
    println("The Caller Code Execute")
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
fun CoroutineScope.scopeFunction() = launch {
    delay(10000)
    print("In CoroutineScope")
}
