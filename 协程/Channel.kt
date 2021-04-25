package com.lonbon.kotlin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*fun main() = runBlocking {
    val channel = Channel<Int>()
    launch {
        for (i in 1..5) {
            channel.send(i * i) //Channel 使用 Send来非阻塞的 发送元素。
        }
    }
    repeat(5) {
        println("Receiver ${channel.receive()}") //Channel 使用 Receive 来接受元素
    }
    println("Down")
}*/

fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
    for (x in 1..5) send(x * x)
}

fun main() = runBlocking {
    val squares = produceSquares()
    squares.consumeEach { println(it) }
    println("Done!")
}