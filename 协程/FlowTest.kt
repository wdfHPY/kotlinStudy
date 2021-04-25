package com.lonbon.kotlin

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import java.lang.RuntimeException
import kotlin.math.log
import kotlin.system.measureTimeMillis

fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100)
        emit(i)
    }
}

fun main() = runBlocking<Unit> {
/*    withTimeoutOrNull(250) {
        simple().collect { value ->
            println(value)
        }
    }
    println("Collect Down")*/ //Flow和的协程一样，都是可以取消的。
    flowTest22()
}

//流的构建器，除了使用flow{}来进行流的构建之外。
val flow2 = flowOf(1, 2, 3) //使用flowof函数构建出一个流。发射固定参数集合的流

//使用flowof()快速进行流的构建。
fun flowOfTest() {
    GlobalScope.launch {
        flow2.collect {
            println(it)
        }
    }
}

//使用集合、Sequence的扩展函数可以转化成 Flow
val flow3 = listOf<Int>(1, 2, 3, 4, 5).asFlow()
val flow4 = listOf<Int>(5, 6, 7, 8).asSequence().asFlow()

fun flowTest2() {
    GlobalScope.launch {
        flow4.collect {
            println(it)
        }
    }
}

//code flow的变换。Flow集合的变换

suspend fun transform(str: String): Int {
    return str.toInt()
}

suspend fun transform2(int: Int): String {
    delay(1000)
    return int.toString()
}

val list2 = listOf<String>("1", "2", "3", "4")//转换前 Collection
var list2Result = listOf<Int>() //转化之后的 Collection

val flow5 = listOf<Int>(1, 2, 3).asFlow() //cold Flow Before transform
var flow5Result = listOf<String>().asFlow() //cold Flow Before transform
suspend fun flowTest3() {

    list2Result = list2.map {
        transform(it)
    }

    println(list2Result.toString())

    flow5.map {
        transform2(it)
    }.collect {
        delay(200)
        println(it)
    }

    flow5.filter {
        it < 3
    }.collect {
        delay(200)
        println(it)
    }
    println("Down")
}

/**
 * 存在一种更加通用的transform 转换函数，在Flow的transform 函数中可以发射任意个数的元素 emit。
 * 而像上面的Flow 的map和 filter 的话本质上都是调用transform 来完成集合的转换。
 */
val flow6 = listOf<Int>(1, 2, 3, 4, 5).asFlow()
suspend fun flowTest6() {
    flow6.transform { i ->
        emit(i)
        emit(transform2(i + 2))
    }.collect { response ->
        println(response)
    }
}

//take 函数
val flow7 = listOf<Int>(1, 2, 4, 5, 6).asFlow()

val flow8 = flow {
    emit(1)
    emit(2)
    emit(3)
    emit(4)
}

//如何监听到流的取消操作呢？ 因为下面的take 限长操作符会取消掉Flow 的emit。协程的取消可以通过try{} catch 来完成
suspend fun flowTest7() {
    flow8.take(2).collect {
        println(it)
    }
}

//Flow 的末端操作符

val flow9 = listOf<Int>(1, 2, 3, 4, 6, 7, 8).asFlow()
suspend fun flowTest8() {
    //collect仅仅是简单的末端操作
    println(flow9.toList()) //toList() 是末端操作
    println(flow9.toSet()) //toSet() 是末端操作
    println(flow9.first())
    //println(flow9.single()) //single() 是末端操作，当Flow中存在超过一个元素的话，此时Flow就会产生错误。

    val sum = flow9.map {
        it * it //遍历流中的元素，然后作出乘方
    }.reduce { a, b ->
        a + b
    }
    println(sum)
}

val flow10 = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14).asFlow()

suspend fun flowTest9() {
    flow10.buffer().filter {
        println("Flow Element Filer $it")
        it % 2 == 0
    }.map {
        println("Flow Element Map $it")
        "string $it"
    }.collect {
        println("result $it")
    }
}
// 运行的结果： Flow Element Filer 1
//Flow Element Filer 2
//Flow Element Map 2
//result string 2
//Flow Element Filer 3
//Flow Element Filer 4
//Flow Element Map 4
//result string 4
//Flow Element Filer 5
//可以完全证明是Flow的变化操作和Sequence 相同，都是连续执行的。

//流的上下文 context
val simple2: Flow<Int> = flow {
    println("${Thread.currentThread().name}")
    for (i in 1..5) {
        delay(1000)
        emit(i)
    }
}

fun flowTest10() = runBlocking {
    withContext(Dispatchers.IO) {
        val time = measureTimeMillis {
            simple2.buffer().collect {
                delay(3000)
                println("${Thread.currentThread().name} \t ${it}")
            }
        }
        println("Cost time is $time")
    }
}

//流的发射值的合并。当存在这种情况下，flow的收集器的处理速度慢于emit 发射的速度。而collector 也是不需要对每一个发射的值进行处理，而是 collect 最新的值。

val flow11 = flow {
    for (i in 1..5) {
        delay(100)
        emit(i)
    }
}

suspend fun flowTest12() {
    /**
     * conflate() 合并操作符，可以用在合并 emit 的值。collector 的速度跟不上emit的速度。
     * 但是不需要对 emit 的值每个值都进行操作，而仅仅对 最新 emit 的值进行操作。换句话说，存在emit 发射的值进行合并了。
     */
    flow11.conflate().collect {
        delay(300)
        println("处理的值为： $it")
    }
}

//conflate() 函数主要通过删除 emit 的值来完成最新值的处理 还存在其他的值来仅仅处理Flow中的最新值

val flow12 = flow {
    for (i in 1..5) {
        delay(100)
        emit(i)
    }
}

suspend fun flowTest13() {
    //collectLatest 也是确保收集处理最新的值。他和conflate的使用方式不相同。当存在新的之后， 会取消掉之前存在的 Collector来处理最新的值。
    flow12.collectLatest {
        println("Collect $it")
        delay(99)
        println("Down $it")
    }
}

//flow 的组合 zip  -> 组合两个流中相关值

val flowNumber = listOf(1, 2, 3, 4).asFlow()
val flowStrings = listOf("w", "d").asFlow()

suspend fun flowTest14() {
    flowNumber.zip(flowStrings) { a, b ->
        "$a -> $b"
    }.collect {
        println(it)
    }
}


//combine()操作符 -> 最新值产生变化之后，会进行重新计算combine

val flowNumbers2 = listOf(1, 2, 3, 4).asFlow().onEach {
    delay(300)
}

val flowStrings2 = listOf("one", "two", "three").asFlow().onEach {
    delay(400)
}

val startTime = System.currentTimeMillis()

suspend fun flowTest15() {
    flowNumbers2.combine(flowStrings2) { number, str ->
        "$number -> $str"
    }.collect {
        println("$it at ${System.currentTimeMillis() - startTime}")
    }
}

//流的展平 -> 流的展平是为了Flow<Flow<X>> -> 当流中存在流对象时，此时就需要进行流的展平成一个流了。

fun requestFlow(i: Int) = flow {
    emit("$i before delay")
    delay(1000)
    emit("$i after delay")
}

//使用  flatMapConcat -> 展平流和外部流的顺序是相同的
suspend fun flowTest16() {
    listOf(1, 2, 3).asFlow().onEach {
        delay(100)
    }.flatMapConcat { requestFlow(it) }.collect {
        println(it)
    }
}
//使用 flatMapConcat操作符存在缺点。就是生成的流需要等待，优点在会保存外部流的顺序
//使用 flatMapMerge 操作符相对于 flatMapConcat 来说,并发的收集传入的流。

suspend fun flowTest17() {
    listOf(1, 2, 3).asFlow().onEach {
        delay(100)
    }.flatMapMerge { requestFlow(it) }.collect {
        println(it)
    }
}

//Flow的展平也是可以使用最新值的 flatMapLatest ,发现新值之后立刻取消先前流的收集。
suspend fun flowTest18() {
    listOf(1, 2, 3).asFlow().onEach {
        delay(100)
    }.flatMapLatest { requestFlow(it) }.collect {
        println(it)
    }
}


//流的异常处理
//收集器可以使用try/catch 来完成

val flow16 = listOf(1, 2, 3, 4, 5).asFlow()

suspend fun flowTest19() {
    //collect产生了一个异常
    //collect的异常可以使用 try/catch 方法来捕捉Flow过程中抛出的异常
    try {
        flow16.collect {
            //使用Flow的check函数，会检查Flow的值是否符合条件
            check(it <= 2) {
                //不符合会产生一个不合法状态的异常抛出
                "Collection Exception $it"
            }
            println("$it")
        }
    } catch (T: Throwable) {
    }
}

//如果在流的过滤的过程中出现异常的话

fun simple3(): Flow<String> = flow {
    for (i in 1..3) {
        emit(i)
    }
}.map { value ->
    check(value <= 2) {
        "Cash on $value"
    }
    "$value"
}

//同样 在过滤的过程中check出的异常的话也是可以使用collect 时的try/catch 来捕捉到。
suspend fun flowTest20() {
    try {
        simple3().collect {
            println(it)
        }
    } catch (T: Throwable) {
    }
}

val flow17 = flow<Int> {
    for (i in 1..19) {
        emit(i)
    }
}

/**
 * catch 操作符仅仅对上游流出现的异常进行处理。
 * catch 是无法针对其下游流进行处理的。如果在 catch 之后产生了一个异常。那么此异常就会逃逸掉。
 */
suspend fun flowTest21() {
    flow17.catch {
        emit(-1) //可以在流中发射值
        println("xxxx") //可以在catch 中打印一些提示信息
        throw Exception("This is catch a exception") //可以catch 中直接抛出异常。
    }.map {
        check(it >= 15)
        it.toString()
    }.collect {
        println(it)
    }
}

fun simple11(): Flow<String> =
    flow {
        for (i in 1..3) {
            println("Emitting $i")
            emit(i) // emit next value
        }
    }
        .map { value ->
            check(value <= 1) { "Crashed on $value" }
            "string $value"
        }

fun main11() = runBlocking<Unit> {
    simple11()
        .catch { e -> emit("Caught $e") } // emit on exception
        .collect { value -> println(value) }
}

fun simple22(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i)
    }
}

/**
 * main22 就是一个典型的声明式捕捉异常。
 */
fun main22() = runBlocking<Unit> {
    simple()
        .onEach { value ->
            check(value <= 1) { "Collected $value" }
            println(value)
        }
        .catch { e -> println("Caught $e") }
        .launchIn(this)//使用launchIn(Scope)来不阻塞的方式启动流的收集
}

/**
 * 流的收集完成 onCompletion() 声明式来来告诉流收集完成时 应该做什么
 */

val flowTest21 = flow {
    emit(1)
    throw RuntimeException("抛出异常")
}

suspend fun flowTest22() {
    flowTest21.onCompletion {
        println("I'm Down")
    }.catch {
        println("Catch Exception")
    }.collect {
        println(it)
    }
}

