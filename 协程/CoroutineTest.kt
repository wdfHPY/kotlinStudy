package com.lonbon.kotlin

import kotlinx.coroutines.*
import kotlin.concurrent.thread

fun main() {
    coroutineTest11()
}

/**
 * 创建出协程。现在存在两个疑问
 *     1. 代码上协程指的是什么？
 *     2. 什么时候需要使用协程？
 * 关于第一个的问题， 代码上协程指的就是launch的闭包中包含的代码，也就是这些代码被发送其他线程上执行了。
 *   这些就是协程的本体， 可以简称为协程体。
 * 第二个问题：在需要切换线程或者指定线程时就可以使用协程了。
 */


fun coroutineTest0() = runBlocking {
    launch(Dispatchers.IO) {
        //切换到IO线程中
    }
}

//kotlin 官方文档中并没有切换线程或者指定线程。
fun coroutineTest1() {
    GlobalScope.launch { //创建出第一个协程，协程一般使用协程上下文和协程构建器来创建。
        delay(1000)
        println("World!")
    }
    println("Hello")
    Thread.sleep(2000)
    /**
     * 可以分析下，如果去除掉Thread.sleep()的话，由于launch{}构建的协程是不阻塞线程的，所以打印完println()之后，会直接结束掉Jvm。
     * 那么 创建出协程不可能运行了。这里Thread.sleep()来阻塞线程来使Jvm 保活。
     */
}

fun coroutineTest2() {
    thread {
//        delay(1000) //delay()函数的作用在于挂起协程，不能阻塞线程。
    }
    // 从上面的执行的结果来看。.launch协程的构建器不会阻塞线程。换句话说， .launch是一个非阻塞协程构建器。
    // 那么是否存在构建器使创建出的协程是阻塞的呢？阻塞线程使其中的协程运行完成。答案是存在的 runBlocking {}
}

/*
* 使用阻塞的协程构建器来创建出一个协程
* */
fun coroutineTest3() {
    GlobalScope.launch { //创建出第一个协程，协程一般使用协程上下文和协程构建器来创建。
        delay(1000)
        println("World!")
    }
    println("Hello")
}

/**
 * 使用阻塞的协程来等待协程运行完成
 */
fun coroutineTest4() {
    GlobalScope.launch {
        delay(1000)
        println("World")
    }
    println("Hello")
    runBlocking {
        delay(2000) //使用阻塞的协程来使协程进行阻塞，目的在于进行Jvm的保活
    }
}

/**
 * 阻塞协程构建器作为顶层主协程。runBlocking {} 会等待其协程完成之后才会结束Jvm 的运行。
 */
fun coroutineTest5(): Unit = runBlocking<Unit> {
    GlobalScope.launch {
        delay(1000)
        println("World")
    }
    println("Hello")
    delay(2000) //延迟2s以确认Jvm 保活。
}

/**
 * 使用延迟来等待协程的完成的这个做法不是一个很好的选择，可以使用join() 来等待一个 一个后台协程的执行
 * join() 方式是未阻塞的方式来等待协程。
 */
fun coroutineTest6() = runBlocking {
    val job = GlobalScope.launch {
        delay(2000)
        println("World")
    }
    println("Hello")
    job.join() //等待所启动的协程直到执行结束。
}

/**
 * 保持很多协程的引用是很危险的。
 * 可以使用结构性并发的来完成之前的功能。 runBlocking 在其作用域中启动的协程执行完毕之前都不会结束。
 *
 * 这里使用作用域来确保结构性并发。上面job 并不是runBlocking {}的作用域创建出的子协程。如果不做延迟和join()的话，
 * 那么runBlocking {} 创建出的协程在println("Hello")之后便结束了。
 * 可能这时会存在一个疑问： 根据runBlocking {}的定义：其会阻塞线程直到其子协程全部执行完成才结束。但是请注意GlobalScope.launch{}
 * 并不是runBlocking{} 的子协程。 这里的子协程并不是位置上的包含， 而是指的是 Scope 创建出的协程才能称为子协程。
 *
 * coroutineTest7中 协程体 println("Hello")和子协程之间是并发的结构。
 */
fun coroutineTest7() = runBlocking {
    //这里 launch{} 即为runBlocking 的子协程。下面的launch{} 就相当于 this.launch{}
    this.launch {
        println("World")
    }
    println("Hello")
}

/**
 * 所以本质上来说， kotlin 官方文档中所说结构性并发体现在代码里面即是Scope。这就结构性的并发。
 */

/**
 * 从runBlocking{} 的作用域来看。
 * 1.job1和job2并不是结构化的并发的。这一点需要了解清楚。
 * 2.需要了解delay()函数的含义：delay函数的含义是  【Delays coroutine for a given time without blocking a thread and resumes it after a specified time.】
 * 延迟对于一个给定的时间的协程。并且delay并没有阻塞一个线程,在给定的时候之后，协程会恢复。
 *
 * 下面协程的执行流程：
 * 1. 开启runBlocking的名为job1的子协程，执行但是被挂起200L。
 * 2. 开启一个新协程：（名称为job2）.
 * 3. 开启job2协程的子协程的名为job3。那么开始执行这个的子协程。但是很遗憾被挂起500L
 * 4. job2被挂起。100L 之后有重新恢复。打印： println("Task from coroutine scope").但是这里job2协程并没有结束。因为其子协程job3 并没有结束。
 * 5. job1 协程恢复。执行 job1协程。println("Task from runBlocking")
 * 6. job3 子协程恢复。执行job3 子协程。println("Task from nested launch")。此时job2 协程结束。
 * 7. println("Coroutine scope is over")
 */
fun coroutineTest8() = runBlocking { // this: CoroutineScope
    val job1 = launch {
        delay(200L)
        println("Task from runBlocking")
    }
    println("outer ${this}")
    val job2 = coroutineScope { // Creates a coroutine scope
        println("nested ${this}")
        val job3 = launch {
            delay(500L)
            println("Task from nested launch")
        }

        delay(100L)
        println("Task from coroutine scope") // This line will be printed before the nested launch
    }
    println("Coroutine scope is over") // This line is not printed until the nested launch completes

}

/**
 * 代码提取到函数当中
 * 将launch 内部的代码提取到函数当中。这个函数称为挂起函数，使用的关键字suspend来进行修饰。
 * 在这种挂起函数中，可以使用delay函数。
 * */
fun coroutineTest9() = runBlocking {
    launch {
        this.doWorld()
    }
    println("Hello")
}

/**
 * 如果需要挂起函数需要和外部的作用域并发的话，可以使用协程作用域CoroutineScope的扩展函数。
 */
suspend fun CoroutineScope.doWorld() {
    println(this)
    delay(1000)
    println("World")
}

fun coroutineTest10() {
    repeat(10_000) {
        thread {
            Thread.sleep(5000L)
            println(".")
        }
    }
}

fun coroutineTest11() = runBlocking {
    GlobalScope.launch {
        repeat(1000) { i ->
            println("I'm sleep $i")
            delay(500)
        }
    }
    delay(1300)
}


