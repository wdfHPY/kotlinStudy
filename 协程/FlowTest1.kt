package com.lonbon.kotlin

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


/*
*   为了非阻塞的返回多个值。使用流flow来完成非阻塞的返回。
* */

fun main() {
    flowSimple6()
}

/**
 * 1. kotlin 中使用flow{}构建块来创建一个流。
 * 2. 函数不需要加上suspend关键字。
 * 3. 流可以使用emit()来向流中来发射一个数据。
 * 4. 流使用collect()来收集值。
 */
fun flowSimple1() = flow {
    for (i in 1..3) {
        delay(1000)
        emit(i) //向flow 中发射一个值
    }
}


fun flowTest1() = runBlocking {
    flowSimple1().collect {
        println(it)
    }
}

/*
*   为什么返回flow的函数不是挂起的操作。因为流是冷的，流和 Sequence 一样,是冷的，只有在collect{}也就是流被收集的
*   时候才会等待。没有被收集的时候会被快速的返回。所以flow的函数并不是suspend。
* */

fun flowTestSimple2() = flow {
    println("Flow Start")
    for (i in 1..3) {
        delay(1000)
        emit(i) //向flow 中发射一个值
    }
}

fun flowSimple2() = runBlocking {
    println("The Flow is Code")
    val flowTest = flowTestSimple2() // -> 流在这一步直接就返回。因为流是冷的的、
    println("Calling Collection")
    //当流被收集collect的时候才会运行构建器的代码
    flowTest.collect {
        println("$it") //在Collect的时候，构建器中的Flow Start执行
    }
    flowTest.collect {
        println("$it") //在Collect的时候，构建器中的Flow Start执行
    }
}


fun flowTestSimple3() = flow {
    println("Flow Start")
    while (true) {
        emit(9)
    }
}

/*
*   虽然流和Job一样，都是在运行过程中进行取消的。但是Job和流的collect取消方式还是存在差别的。
*   1. Job的取消方式主要通过cancel去结束。
*   2. 而流的收集在collect在collect之后并不会的产生Job。所以肯定不能使用cancel的方式去结束任务。
*   流的取消可以都是通过超时而取消的。
* */
fun flowSimple3() = runBlocking {
    withTimeoutOrNull(250) {
        flowTestSimple3().collect {
            println(it)
        }
    }


    withTimeout(250L) {
        while (isActive) {
            println("jobTimedOut")
        }
    }
}

fun flowTestSimple4() = flowOf(
    1, 3, 4
)

fun flowTestSimple5() = (1..3).asFlow()

/**
 * 除了flow{}构建器之外。还存在其他创建流方法。使用flowOf{}创建流。此流构建器一般都是用来发射固定值的。
 * 还可以使用asFlow()来将Collection转化为流。
 *
 * flowOf()流构建器和asFlow都是使用自动的去发射固定值的。
 *
 * .asFlow 和 flowOf流构建器都是自动emit值。
 */
fun flowSimple4() = runBlocking {
    flowTestSimple5().collect {
        println("$it")
    }
}

/**
 * 过渡流操作符。可以针对流来进行流的转换。一般这一类过度流操作符作用于上游流，生效于下游流。所谓的上游流便是
 * 过渡操作符之前的流，下游流便是过滤操作符之后的流。
 *
 * 过渡流操作符主要使用在上游流。生效的效果就使用在下游流当中了。
 */
fun flowTestSimple6() = flowOf(1, 2, 3, 4)

suspend fun transformFunction(int: Int): String {
    delay(1000)
    return "response： ${int}"
}

/**
 * 这里过渡操作符map和filter不能产生在emit值。那么过渡操作符就相当于不能更改其中的值了。
 */
fun flowSimple5() = runBlocking {
    flowTestSimple6().map {
        //map 针对上游流。而这里的上游流就是flowOf(1,2,3,4)
        transformFunction(it)
    }.collect {
        //产生的变化针对下游流。
        println(it)
    }
}

/**
 * filter和 Map都是只能处理一些简单的转换。还存在更高级的转换函数transform
 */

fun flowSimple6() = runBlocking {
    flowTestSimple6().transform { req ->
        emit(transformFunction(req))
        //map 针对上游流。而这里的上游流就是flowOf(1,2,3,4)
        emit(req)
    }.collect {
        //产生的变化针对下游流。
        println(it)
    }
}
