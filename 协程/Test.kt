package com.lonbon.kotlin;

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun median(x: Int, y: Int, z: Int) {
    val arr = arrayOf(x, y, z)
    val size = arr.size
    arr.sort()
    println(arr[size / 2])
}

/*
fun main(array: Array<String>) {
    median(3, 2, 5)
}
*/


data class Cat(
    val id: String,
    var image: String,
    var details: String
)

fun test() {

    val oldList = listOf(
        Cat("id1", "img1", "details1"),
        Cat("id2", "img2", "details1"),
        Cat("id3", "img3", "details1")
    )
    println(oldList)

    val newList = mutableListOf<Cat>()
    oldList.apply {
        onEach { cat ->
            newList.add(cat.copy().apply {
                image = "web_$image"
                details = "detail_xyz$details"
            })
        }
    }

    println(newList)
    println(oldList)
}

/*fun text() {
    val oldList = listOf("1" , "2", "3")
    println(oldList)

    val newList = oldList.toMutableList()
    newList.add("4")
    println(oldList)
    println(newList)
}*/

private var mJob: Job? = null

fun repeatCoroutine() = runBlocking {
    repeat(10) {
        if (mJob?.isActive == true)
            mJob?.cancel()
        else
            mJob = launch(CoroutineName("Coroutine - $it")) {
                delay(200)
                println("job ${this.coroutineContext} done")
            }
    }
}

/*fun main22() {
    combineFlow()
}*/

fun emitStringElem(): Flow<String> = flow {
    repeat(5) {
        delay(10)
        emit("elem_$it")
    }
}

fun emitIntElem(): Flow<Int> = flow {
    repeat(10) {
        delay(10)
        emit(it)
    }
}


fun collectFlow() = runBlocking {
    launch {
        emitIntElem().collect {
            println("From int Flow: item is: $it")
        }
    }
    launch {
        emitStringElem().collect {
            println("From string Flow: item is: $it")
        }
    }
}

fun margeFlow() = runBlocking {
    merge(
        emitIntElem().map {
            it.toString()
        }, emitStringElem()
    ).collect {
        println(it)
    }
}

fun combineFlow() = runBlocking {
    combine(emitIntElem(), emitStringElem()) { int: Int, str: String ->
        "$int combine $str"
    }.collect {
        println(it)
    }
}

data class User(
    val id: Int,
    val name: String
)

suspend fun getIdList(): List<Int> {
    delay(1000)
    return listOf(2, 4, 1, 6, 5, 3)
}

val users = listOf(
    User(1, "Alex"),
    User(2, "Tom"),
    User(3, "Jerry"),
    User(4, "Scort"),
    User(5, "linda"),
    User(6, "panda")
)

suspend fun queryUserById(id: Int): User {
    delay(1000)
    return users.first {
        it.id == id
    }
}

private val userFlow = MutableSharedFlow<List<User>>(1)

fun main() = runBlocking {
    launch {
        userFlow.emit(getIdList().map { id ->
            async { queryUserById(id) }
        }.awaitAll())
    }


    launch {
        userFlow.collect {
            println(it)
        }
    }
    Unit
}