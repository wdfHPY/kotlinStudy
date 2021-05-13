package com.lonbon.kotlin.exceptions

import kotlinx.coroutines.*
import java.io.IOException
import java.lang.Exception
import java.lang.NullPointerException


fun main() {
    coroutineException9()
}

/**
 * Java中可以通过Thread提供的Api.UncaughtExceptionHandler 可以处理由于异常终止的线程。
 *
 * kotlin的协程构建器针对于异常是存在不同的机制的。第一种：自动传播异常。第二种：向用户暴露异常。
 * 第一种异常处理机制和上面所说的Thread处理的机制差不多。launch  actor 构建器就属于这一点。这种的异常通过 try catch
 * 处理不了。属于unCatchException。此时需要定义CoroutineExceptionHandle设置给协程上下文。这种做法和线程类似。
 *
 * 第二种异常处理：向用户去暴露异常。此种异常是可以通过try catch 操作符进行处理的。这种异常是依赖用户自己去消费的。像
 * async produce 协程构建器属于向用户暴露异常。
 */
fun coroutineException1() = runBlocking {
    try {
        val job = GlobalScope.launch { // root coroutine with launch
            println("Throwing exception from launch")
            throw IndexOutOfBoundsException() // Will be printed to the console by Thread.defaultUncaughtExceptionHandler
        }
        job.join()
    } catch (e: Exception) {
        //自动传播的异常通过try catch 无法处理。
        println("cause exception")
    }

    println("Joined failed job")
    val deferred = GlobalScope.async { // root coroutine with async
        println("Throwing exception from async")
        throw ArithmeticException() // Nothing is printed, relying on user to call await
    }

    /**
     * 下面属于向用户暴露的异常。
     * 可以通过try catch 的方式来完成异常的捕捉。
     */
    try {
        deferred.await()
    } catch (e: Exception) {
        println("Unreached")
    }
}

fun coroutineException2() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("${exception}") //可以看到，异常被捕捉了。
    }

    val job = GlobalScope.launch(handler) { // root coroutine with launch
        println("Throwing exception from launch")
        throw IndexOutOfBoundsException() // Will be printed to the console by Thread.defaultUncaughtExceptionHandler
    }

    val deferred = GlobalScope.async(handler) { // root coroutine with async
        println("Throwing exception from async")
        throw ArithmeticException() // Nothing is printed, relying on user to call await
    }
    deferred.await()

    joinAll(job, deferred)

    println("Down")
}


fun coroutineException3() = runBlocking {
    val job = launch(newSingleThreadContext("x")) {
        val child = launch(newSingleThreadContext("y")) {
            try {
                delay(2000)
            } catch (e: Exception) {
                println("${e}") //取消的异常是可以捕捉的到的，但是handler不会去处理它。
            } finally {
                println("Child is cancelled")
            }
        }
        yield()
        println("Cancelling child")
        child.cancel()
        child.join()
        yield()
        println("Parent is not cancelled")
    }
    job.join()
}

val unCatchExceptionHandle = CoroutineExceptionHandler { _, exception ->
    println(exception)
}

fun coroutineException4() = runBlocking {
    val job = GlobalScope.launch(unCatchExceptionHandle) {
        launch {
            try {
                delay(Long.MAX_VALUE)
            } catch (e: Exception) {
                withContext(NonCancellable) {
                    println("Children are cancelled, but exception is not handled until all children terminate")
                    delay(100)
                    println("The first child finished its non cancellable block")
                }
            }
        }

        launch {
            delay(10)
            println("throw a exception but is not a CancellationException")
            /**
             * 除了CancellationException之外。其他的异常都会导致父协程的结束。
             * 虽然，在父协程（顶级协程上设置了 CoroutineException）了，但是仍然是恢复不到协程状态的，父协程仍然
             * 会被结束。
             */
            throw NullPointerException("This is a NullPointException")
        }
    }
    job.join()
}


val unCatchExceptionHandle2 = CoroutineExceptionHandler { _, exception ->
    println(exception.suppressed.contentDeepToString())
}


/*
*   如果多个子协程都抛出了异常的话，那么默认的情况下。父协程会处理第一个协程，后来的协程都会被抑制。
*   之后所有的异常便是 -> 异常聚合。
* */
fun coroutineException5() = runBlocking {
    val job = GlobalScope.launch(unCatchExceptionHandle2) {
        launch {
            try {
                delay(Long.MAX_VALUE)
            } catch (e: Exception) {
                throw NumberFormatException()
            }
        }

        launch {
            delay(100)
            throw NullPointerException() //第一个抛出的异常为 NullPointException, 之后抛出的异常都会被
            //聚合到首个异常。之后收到的异常都会被抑制。
        }
    }
    job.join()
}

/*
* CancellationException异常是在协程是透明的。
* */
fun coroutineException6() = runBlocking {
    val job = GlobalScope.launch {
        launch {
            try {
                delay(Long.MAX_VALUE)
            } catch (e: CancellationException) {
                println("job1 ${e}")
            }
        }
        launch {
            try {
                delay(Long.MAX_VALUE)
            } catch (e: CancellationException) {
                println("job2 ${e}")
            }
        }
        delay(2000)
    }
    job.cancel()
}

fun coroutineException7() = runBlocking {
    val job = GlobalScope.launch(unCatchExceptionHandle) {
        val inner = launch {
            launch {
                launch {
                    launch {
                        throw IOException()
                    }
                }
            }
        }
        try {
            inner.join()
        } catch (e: CancellationException) {
            println("Rethrowing CancellationException with original cause")
//            throw e // 取消异常被重新抛出，但原始 IOException 得到了处理
        }
    }
    job.join()
}

/*
*   SupervisorJob 单向传播任务。
*   一般任务都是双向的，子协程一旦产生异常，那么就会取消父协程。父协程一旦取消，那么就会取消所有子Job。
*   SupervisorJob 和上面的任务不同， 此种Job的传播仅仅只是单向的。此种任务仅仅只能从上向下的执行。
*   SupervisorJob 的异常只能从上层向下层进行传输。子Job任务的异常并不会结束父协程。也不会影响到同级别的任务、
* */

fun coroutineException8() = runBlocking {
    val job = SupervisorJob() //监督任务Job. -> 然后的将此Job放到上下文中
    with(CoroutineScope(coroutineContext + job)) { //任务不是默认Job()类型了，而是单向的Supervisor
        //使用CoroutineExceptionHandler 处理监督任务异常。这里监督任务的异常和不同任务的异常处理时存在不同。监督任务
        //需要使用try catch 来处理。而不同Job是直接抛给父协程的。
        val firstChild = launch(CoroutineExceptionHandler { _, _ -> }) {
            println("The firstChild is failed")
            throw IOException("Im Failed")
        }

        val secondChild = launch {
            firstChild.join()
            println("The first child is cancelled: ${firstChild.isCancelled}, but the second one is still active")
            try {
                delay(Long.MAX_VALUE)
            } catch (e: Exception) {
                println("The second child is cancelled because the supervisor was cancelled")
            }
        }
        firstChild.join()
        println("Cancelling the supervisor")
        job.cancel() //监督任务的取消会导致所有的子协程取消掉。
        secondChild.join()
    }
}

fun coroutineException9() = runBlocking {
    try {
        supervisorScope {
            val child = launch {
                try {
                    println("The child is sleeping")
                    delay(Long.MAX_VALUE)
                } finally {
                    println("The child is cancelled")
                }
            }
            yield()
            println("Throwing an exception from the scope")
            throw IOException() //父异常的取消会将作用域内所有子Job 给取消掉。
        }
    } catch (e: Exception) {
        println(e)
    }

}

/**
 * 由于监督任务，监督Job的特殊性。子任务的异常不会传播给父任务中。
 * 所以其针对每一个子任务都是需要的try catch的。 使用try catch 或者handler都是可以处理的。
 */
fun coroutineException10() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }
    supervisorScope {
        try {
            val child = launch {
                println("The child throws an exception")
                throw AssertionError()
            }
            println("The scope is completing")
        } catch (e: Exception) {

        }
    }
    println("The scope is completed")
}