package com.lonbon.kotlin

import kotlinx.coroutines.*


fun main() {
    coroutine2Test11()
}

/*
*   协程上下文： CoroutineContext，这个是一些元素的集合。
*   其中就包括指定协程运行在哪一个线程的协程调度器。CoroutineDispatcher
* */

fun coroutineContext1() = runBlocking {
    launch { //运行在父协程的上下文当中
        println("this is ${Thread.currentThread().name} thread!")
    }

    launch(Dispatchers.Default) {//使用默认的协程调度器
        println("DefaultContext: this is ${Thread.currentThread().name} thread")
    }

    launch(Dispatchers.Unconfined) {//使用协程运行在不受限制的上下文中
        println("UnconfinedContext: this is ${Thread.currentThread().name} thread")
    }

    /**
     * 创建一个线程来运行协程。
     */
    launch(newSingleThreadContext("MyOwnThread")) {//使用协程运行在不受限制的上下文当中
        println("newSingleThreadContext: this is ${Thread.currentThread().name} thread")
    }

    /**
     * 全局的协程的启动的时，采用Dispatcher是 Default。launch(Dispatcher.Default) {}和 GlobleScope
     * 采用的调度器是相同的调度器。
     */
    GlobalScope.launch {
        println("GlobalScope.launch: this is ${Thread.currentThread().name} thread")
    }
}

fun coroutineContext2() = runBlocking {
    launch(Dispatchers.Unconfined) {
        println("UnconfinedContext: this is ${Thread.currentThread().name} thread")
        changeThread()
        println("UnconfinedContext: this is ${Thread.currentThread().name} thread")
    }
}

suspend fun changeThread() {
    withContext(Dispatchers.IO) {
        println("UnconfinedContext: this is ${Thread.currentThread().name} thread")
    }
    delay(1000)
}

fun coroutineContext3() = runBlocking {
    withContext(Dispatchers.Default) {
        println("1${Thread.currentThread().name}")
        delay(1000)
        println("3${Thread.currentThread().name}")
    }
    println("Done")
}


/**
 * 开启了三个协程，而这三个协程都是被限制在main 线程当中了。
 * 使用Jvm参数
 *      -Dkotlinx.coroutines.debug 或者 -ea 来启动协程时 -> 可以看到协程的命名。
 */
fun coroutineContext4() = runBlocking<Unit> {
    val a = async {
        println("${Thread.currentThread().name} I'm computing part of the answer")
        6
    }
    val b = async {
        println("${Thread.currentThread().name} I'm computing another part of the answer")
        7
    }
    println("${Thread.currentThread().name} The answer is ${a.await() * b.await()}")
}

/**
 *下面进行切换了线程。需要区分一点下： withContext仅仅可以使用来去切换上下文。而不能去创建一个协程。
 * 创建一个协程只能通过协程构建器来创建。withContext 仅仅能够去切换线程而已。
 * 从打印的结构来看， 切换线程时，仅仅只是开辟了一个协程。
 */
fun coroutineContext5() = newSingleThreadContext("Context1").use { ctx1 ->
    newSingleThreadContext("Context2").use { ctx2 ->
        runBlocking(context = ctx1) {
            println(Thread.currentThread().name)
            withContext(ctx2) {
                println(Thread.currentThread().name)
            }
            println(Thread.currentThread().name)
        }
    }
}

fun coroutineContext6() = runBlocking {
    println(coroutineContext[Job]) //Job是上下文的一部分。可以使用coroutineContext[Job]去打印出协程的作业。
}

/*
*  子协程： 当一个协程在另外一个协程中启动时，它会继承启动协程的coroutineContext。因为作业仅仅是协程上下文的一部分
*  所以父协程的作业和子协程的作用存在一定的联系。新启动的协程的作业是父协程作业的子作业， 也就是存在这样的上下级
*  关系。当父协程的作业取消的时候，子协程的作业也会被递归的取消。
*
*  但是，存在一个例外。那就是GlobalScope.launch 启动的协程。GlobalScope.launch 启动的协程不存在父协程，他和
*  当然Scope是独立的。不存在结构化的关系，那么自然也就不存在父协程了。
* */
fun coroutineContext7() = runBlocking {
    val request = launch {

        GlobalScope.launch { //开启一个全局协程
            delay(1000)
            println("This is a global coroutine")
        }
        launch { //开启一个子协程
            delay(1000)
            println("This is a sub coroutine")
        }

    }
    delay(500)
    //request.cancel() //会递归的结束父协程的作业的所以子作业。
    delay(1000) //之前没有加上这一句。发现不存在任何的打印。原因在于Jvm没有保活，在GlobalScope
    //println()的时候，此时Jvm已经的结束了。
}

/**
 * 父协程总是等待其中所有子协程运行完成。并且不需要显式的调用join()函数。
 * 这句话这么说可能有一些问题，因为可能会理解为阻塞来异步等待子协程。其实这里表达式父协程的生存周期，可以理解为
 * 父协程的生存周期总是在子协程执行完成之后。
 *
 * 下面例子可以清晰的看到没有做任何的延时或者是join(),Jvm还存活。因为需要等待父协程结束。
 */
fun coroutineContext8() = runBlocking {
    val request = launch {
        repeat(3) { i ->
            launch {
                delay((i + 1) * 1000L)
                println("Down $i")
            }
        }
        println("Down request coroutine")
    }
    println("Parent Coroutine")
}

/**
 * 可以给协程定义CoroutineName,定义好之后很方便的就可以访问到协程的名称
 * 将协程的Name定义为： aSync
 * 将协程的Name定义为： bSync
 * 方便之后对Coroutine的测试。
 */
fun coroutineContext9() = runBlocking {
    val a = async(CoroutineName("aSync")) {
        delay(1000)
        println("${Thread.currentThread().name}")
        2
    }

    val b = async(CoroutineName("bSync")) {
        delay(2000)
        println("${Thread.currentThread().name}")
        4
    }

    println("a / b = ${a.await() / b.await()}")
}

/*
* 上下文元素的组合
* */
fun coroutine2Test10() = runBlocking {
    val a = async(CoroutineName("aSync") + Dispatchers.Default) {
        //上下文中组合协程名称以及分发器为Default
        println("I'm working in thread ${Thread.currentThread().name}")
    }
}

/**
 *
 */

fun coroutine2Test11() = runBlocking {
    val hotDataSource = withContext(Dispatchers.IO) {
        delay(4000)
        doStringAndReturn()
    }
    coroutineContext.cancelChildren()
    println(hotDataSource)
}

suspend fun doStringAndReturn() :String {
    delay(2000)
    return "This is final result"
}

