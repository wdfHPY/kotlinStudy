package com.lonbon.kotlin

import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.concurrent.thread

/**
 * 协程的取消和超时
 */
fun main() {
    coroutine2Test7()
}

fun coroutine2Test1() = try {
    runBlocking {
        //协程应该是可以被取消的。launch 构建器创建协程时会返回一个Job。
        val job = launch {
            repeat(1000) { i ->
                println("I m repeat $i")
                delay(500) //延迟0.5s
            }
        }
        delay(1300L)
        println("Cancel This Job!")
        job.cancel()

        job.join()
        println("Job had Canceled")
    }
} catch (e: Exception) {
    println("捕捉到cancel()函数抛出的异常")
}

/*
*    协程的取消存在前提。协程的取消时协作式的，协作式的取消可以理解为在代码上需要去配合取消。
*       协程的取消是通过抛出异常来解决的。
* */
//函数并不是可取消
fun coroutine2Test2() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.IO) {//可以看到,协程被切换到IO线程了。
        var nextPrintTime = startTime
        var i = 0
        while (i < 10) {
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // 等待⼀段时间
    println("main: I'm tired of waiting!")
    job.cancel() // 取消⼀个作业并且等待它结束 println("main: Now I can quit.")
    println("main: Now I can quit.")
}
/*
*   从上面的运行结果来看，job在调用完成cancel()函数之后仍然继续执行了剩下的协程。
*   因为这里并没有做协程取消的支持。协程的取消是需要代码支持的。
*     1) 可以通过ensureAlive()和 isAlive()来判断是否协程取消的状态。
*     2) coroutine2Test1中为什么能够完成job 的取消。因为在库函数delay中已经做了cancel的代码支持了。delay，withContext
*   函数都是的可以使用cancel()来抛出异常从而退出协程。
* */

//使协程支持可取消。 -> 使用显式的执行判断Job的运行状态协作取消
fun coroutine2Test3() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.IO) {//可以看到,协程被切换到IO线程了。
        var nextPrintTime = startTime
        var i = 0
        while (i < 10 && isActive) {
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // 等待⼀段时间
    println("main: I'm tired of waiting!")
    job.cancel() // 取消⼀个作业并且等待它结束 println("main: Now I can quit.")
    println("main: Now I can quit.")
}

/*
*  协程的取消和线程的Interupter的原理类似，都是通过抛出异常的方式。
* */
fun coroutine2Test4() = runBlocking {
    val startTime = System.currentTimeMillis()
    try {
        val job = launch(Dispatchers.IO) {//可以看到,协程被切换到IO线程了。
            var nextPrintTime = startTime
            var i = 0
            while (i < 10) {
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job: I'm sleeping ${i++} ...")
                    delay(500)
                }
            }
        }
        delay(1300L) // 等待⼀段时间
        println("main: I'm tired of waiting!")
        job.cancel() // 取消⼀个作业并且等待它结束 println("main: Now I can quit.")
        println("main: Now I can quit.")
    } finally {
        delay(1000)
    }
}

/**
 * 当然，存在一些必要的任务是不可以取消的，比如关闭文件资源，取消一个作业的话，此时这种任务是不可以取消的。
 * 换句话说的，这种情况下，协程对于cancel 抛出的异常是不做任何的处理的。
 *  withContext(NonCancellable) {} 使用指定执行上下文为不可取消的上下文。此时会使之前对cancel 产生反应的
 *  函数不产生任何的作用。
 */
fun coroutine2Test5() = runBlocking {
    val job = launch {
        withContext(NonCancellable) {
            repeat(1000) { i ->
                println("I m repeat $i")
                delay(500) //延迟0.5s  由于在NonCancellable的上下文中，所以外接cancel()产生的
                //Exception 不会导致协程的取消。
            }
        }
    }
    delay(1300L)
    println("Cancel This Job!")
    job.cancel()
    println("Job had Canceled")
}

/**
 * 那么，如果存在一种情况，部分的协程体是可以对cancel 的异常相应的.而另外一部分不可以对 Exception 作出相应。
 *   -> 将不可取消的代码放置在finally 代码块中执行。
 */
fun coroutine2Test6() = runBlocking {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("I m repeat $i")
                delay(500) //延迟0.5s  由于在NonCancellable的上下文中，所以外接cancel()产生的
                //Exception 不会导致协程的取消。
            }
        } finally {
            //这个代码块中的协程体是一定不会被取消的。
            withContext(NonCancellable) {
                println("This will not be cancelled!")
            }
        }
    }
    delay(1300L)
    println("Cancel This Job!")
    job.cancel()
    println("Job had Canceled")
}

/**
 * 协程的超时 --> 可以看到 经过超时后，程序抛出了TimeoutCancellationException。
 * 这个TimeoutCancellationException 是 CancellationException的子类。
 */
fun coroutine2Test7() = runBlocking {
    var i = 0
    try {
        withTimeout(1100) { //如果在1100ms 之后 任务没有完成，那么此时就会抛出异常。
            repeat(100) {
                println(i++)
                delay(500)
            }
        }
    } catch (e: Exception) {
        println("e :$e")
    } finally {
        //当然， 对于的超时之后资源的释放等等也是很重要的。
        println("do some things")
    }
}

/**
 * 使用withTimeoutOrNull 当代码块超时的时候，不会抛出TimeCancellation的异常。
 * 但是这个时候返回值为 null 。
 */
fun coroutine2Test8() = runBlocking {
    var i = 0
    val result = withTimeoutOrNull(1100) { //如果在1100ms 之后 任务没有完成，那么此时就会抛出异常。
        repeat(100) {
            println(i++)
            delay(500)
        }
    }
    println(result)

}

