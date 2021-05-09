package com.lonbon.kotlin

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

/**
 * Channel 是实现SharedFlow和StateFlow Api。
 * Channel 是 hot data source.但是 Flow is cold data source.
 *
 */
fun main() = runBlocking {
    channelTest9()
}

fun channelTest1() = runBlocking {
    val channel = produceSquares2()
    launch {
        for (i in 1..5) {
            delay(1000)
            channel.send(i * i) //Channel 使用 Send来非阻塞的 发送元素。
        }
    }
    repeat(5) {
        println("Receiver ${channel.receive()}") //Channel 使用 Receive 来接受元素
    }
    println("Down")
}

fun channelTest2() = runBlocking {
    val channel = Channel<Int>()
    launch {
        for (x in 1..5) {
            channel.send(x * x)
        }
        channel.close() //使用close()函数来的关闭一个channel。 表明不存在更多的元素来进入channel。
    }

    for (i in channel) {
        println(i)
    }
    println("down")
}

fun produceSquares2(): Channel<Int> = Channel()

/**
 * 构建生产者通道。
 * 下面就是将生产者抽象成为一个函数。生产者函数和其他的函数存在不同点的是：一个是作为 hot data source，另外的一种便是作为
 * cold data source。
 *
 * cold data source -> 在函数调用call 之前。 function will not do anything.也就是在函数调用之前不会做任何事。
 * 在 函数调用call 之后。function will not do anything. 在函数调用之后，函数不会做任何事。数据源只在函数的调用中是
 * active的。   cold data source 引用丢失也无妨，因为其不可能在后台还处于激活的状态。
 *
 * hot data source -> channel 便是一个很典型的hot data source.
 * 定义为： 在函数调用时，热的数据源可能已经在后台激活， 当函数调用完成之后，数据源也是处于激活的状态。（很显然，channel可以在
 * 调用的函数外进行send 和 receiver）。 hot data source 的引用不可以随意的lost。因为丢失之后可能会产生资源的leek。
 */
fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
    for (x in 1..5) send(x * x)
    sendOther(9)
}

suspend fun ProducerScope<Int>.sendOther(i: Int) {
    send(i)
}

fun main2() = runBlocking {
    val squares = produceSquares()
    for (i in squares) {
        println(i)
    }
    println(squares.isEmpty)
    squares.consumeEach {
        println(it)
    }
    println(squares.isEmpty)
    println("Done!")
}

/*
* 管道的概念：管道是一种模式，并不是和channel所独有的。
* 管道是在流中开始生产无限个元素的模式。
*  下面channelTest4就是一个通道。Channel 中存在1,2,3,4,5,
* */
fun CoroutineScope.produceNumbersByReceiverChannel(): ReceiveChannel<Int> = produce {
    var i = 1
    while (true) {
        send(i++)
    }
}

/**
 * 对管道中的元素做一些元素处理。
 */
fun CoroutineScope.squareNumber(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
    for (i in numbers) {
        send(i * i)
    }
}

fun channelTest4() = runBlocking {
    val numbers = produceNumbersByReceiverChannel()

    /**
     * 上面的numbers 便是热数据源的引用。如何引用的丢失的话，那么
     * 管道会在后台一直向管道中扔数据。此时就造成了资源的泄露。
     */
    val squareNumber = squareNumber(numbers)
    repeat(100) {
        println(squareNumber.receive())
    }
    println("Down")
    coroutineContext.cancelChildren() //取消掉作用域中所有的子任务。
}

fun channelTest5() = runBlocking {
    var cur = numbersFrom(2)
    repeat(10) {
        val prime = cur.receive()
        println(prime)
        cur = filter(cur, prime)
    }
    coroutineContext.cancelChildren() // cancel all children to let main finish
}

fun CoroutineScope.numbersFrom(start: Int) = produce<Int> {
    var x = start
    while (true) send(x++) // infinite stream of integers from start
    //过滤之前的流的序列为 2 3 4 5 6 7 8
}

/**
 * 过滤掉大部分的 numbers。
 * 第一次传入的参数是2的时候，此时过滤之后的Channel 值为 3 5 7 9 11 ...
 * 第二次传入的参数是3的时候，此时过滤之后的channel 值为 5 7 11 ...
 * 第三次传入的参数是5的时候，此时过滤之后的channel 值为 7 11...
 */
fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce<Int> {
    for (x in numbers)
        if (x % prime != 0)
            send(x)
}

/*
*   Channel的扇出。多协程接受相同的通道的时候。
*   产生了一个管道。
* */
fun CoroutineScope.produceNumberForCoroutine(): ReceiveChannel<Int> = produce {
    var i = 0
    while (true) {
        send(i++)
        delay(100)
    }
}

/**
 * 多个协程扇出元素时不产生任何的
 */
fun CoroutineScope.launchCoroutine(id: Int, channel: ReceiveChannel<Int>) = launch {
    for (i in channel) {
        println("id is $id, message is ${i}")
    }
}

fun channelTest6() = runBlocking {
    val channel = produceNumberForCoroutine()
    repeat(10) {
        launchCoroutine(it, channel)
    }
    delay(1000)
    channel.cancel()// 取消生产者协程将关闭它的通道。会终止所有处理器协程在此通道上的迭代。
}

/**
 * 多协程扇入元素
 */
suspend fun produceElem(channel: SendChannel<String>, str: String, delayTime: Long) {
    while (true) {
        delay(delayTime)
        channel.send(str)
    }
}

fun channelTest7() = runBlocking {
    val channel = Channel<String>()
    launch { produceElem(channel, "Foo", 200L) }
    launch { produceElem(channel, "Xoo", 500L) }
    repeat(6) { // 接收前六个
        println(channel.receive())
    }
    coroutineContext.cancelChildren() // 取消所有⼦协程来让主协程结束
}

/*
*   Channel 在默认的情况下不存在缓冲区。channel 的缓冲区默认大小设置为0
* */
suspend fun sendElemToChannel(channel: Channel<String>) {
    channel.send("Send")
    println("Send Complete")
}

suspend fun receiveElemChanenl(channel: Channel<String>) {
    println(channel.receive())
}

/**
 * channel 默认的缓冲区大小是0。所以此时在channel不存在元素消费的话，消费的
 * 协程会被挂起。
 */
fun channelTest8() = runBlocking {
    /**
     * channel 创建器和 produce 都是可以指定 缓冲区的capacity。
     * 默认的状态下，缓冲区的大小是0。
     * send 函数先调用的时候，send function 会被挂起直到receive Function被调用。相反，如果是receive Function先调用的话，那么其仍然会被挂起知道send function调用。
     * 缓冲区的大小确定了send Function 和 receive Function 是否挂起。
     */
    val channel = Channel<String>(2)

/*    launch {
        receiveElemChanenl(channel)
    }*/

    repeat(10) {
        //仅仅两个协程能够执行，他们的发送协程都会被阻塞。
        println("${it}")
        launch {
            sendElemToChannel(channel)
        }
    }
    delay(10000)
    coroutineContext.cancelChildren()
    println("Down")
}

/*
*   channel 对所有协程的发送和接收元素操作都是公平的。
*   遵循者先进先出的原则。
* */

data class Ball(
    var hit: Int
)

fun channelTest9() = runBlocking {
    val channel = Channel<Ball>()
    launch {
        player("player1", channel)
    }
    launch {
        player("player2", channel)
    }
    channel.send(Ball(0))
    delay(1000)
    coroutineContext.cancelChildren()
}


suspend fun player(string: String, channel: Channel<Ball>) {
    for (ball in channel) {
        ball.hit++
        println("player is ${string} ${ball}")
        delay(300)
        channel.send(ball)
        /**
         * channel 是所有协程都是公平的。player2还在receive。所以player1的receive是在player2的receive之后的。
         * 保证先进先出的优先级。
         * */
    }
}