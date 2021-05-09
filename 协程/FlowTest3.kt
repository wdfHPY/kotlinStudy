package com.lonbon.kotlin

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture

fun main() {
    flow3Test()
}

fun flow3Test() = runBlocking {
    val stateFlow: MutableStateFlow<Int> = MutableStateFlow(0)
    launch {
        stateFlow.collect {
            println(it)
        }
    }

    (1..4).forEach {
        delay(100)
        stateFlow.value = it
    }
}

/*
fun fooAsync(p: Params): CompletableFuture<Value> =
    CompletableFuture.supplyAsync { bar(p) } */
