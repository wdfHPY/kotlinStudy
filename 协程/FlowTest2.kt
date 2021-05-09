package com.lonbon.kotlin

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


/*
*   为了非阻塞的返回多个值。使用流flow来完成非阻塞的返回。
*   take()对流进行限长。take的个数如果小于flow中的个数的话
* */

fun main() {
    flow44()
}

/**
 * 使用take来进行流的限长。流中的数据如果小于限长的长度则不会产生任何的影响。
 */
val flow1 = flowOf(1, 2, 3, 4, 5, 6)

fun flow2Simple6() = runBlocking {
    flow1.take(10).collect {
        println(it)
    }
}


val flow22 = flow {
    delay(100)
    println("开始发射")
    emit(20)
    emit(20)
    emit(20)
}

fun flow2Simple7() = runBlocking {
    flow22.collect {} //collect 收集是末端操作。
    flow22.first() //first 是末端操作
    //flow22.single() //single 是末端操作。 single要求流中只能存在一个元素。否则会出现IllegalArgumentException.
    flow22.reduce { a, b -> //流的规约。 将flow 中元素规约成单个值。reduce也是末端操作。
        a + b
    }
}

/*
*  flow的执行方式是按照顺序执行的方式。flow的执行方式和eSequence的执行方式一样。
*   从下面的程序可以清楚地了解到flow的执行和Sequence的执行方式一样，都是针对元素进行
*   顺序执行。
* */

fun flow33() = runBlocking {
    (1..5).asFlow().filter {
        println("${it}")
        it % 2 == 0
    }.map {
        println("${it}")
        it.toString()
    }.collect {
        println("${it}")
    }
}

/**
 * 使用flowOn()操作符来切换flow emit发射的协程的上下文。默认流的上下文是在上下文保存中进行发射的。
 */
val frow44 = flow {
    emit("1")
    emit("2")
    emit("3")
    emit("4")
    emit("5")
    println("${Thread.currentThread().name}")
}.flowOn(Dispatchers.IO + CoroutineName("FlowOn CoroutineContext"))

fun flow44() = runBlocking {
    launch(Dispatchers.Default + CoroutineName("collect CoroutineContext")) {
        println("${Thread.currentThread().name}")
        frow44.toList()
    }
}