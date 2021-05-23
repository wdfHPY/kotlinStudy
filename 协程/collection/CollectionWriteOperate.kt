package com.lonbon.kotlin.collection

/**
 * Collection 的写操作。
 */
fun main() {
    collectionRemove()
}

fun collectionWrite() {
    val number = mutableListOf("ktor", "kotlin", "bug")
    number.add("next")
    number.add(1, "tab")
    number.addAll(listOf("IDEA", "eclipse"))
    //使用 + += 运算符可以完成集合的写操作。
    val list = number + "Google"
    number += listOf("I", "Kill", "Adobe")
    println(number)
    println(list)
}

fun collectionRemove() {
    val numbers = mutableListOf(1, 7, 3, 5, 5, 6, 7)
    numbers.remove(1)
    println(numbers)
    numbers.remove(50)
    println(numbers)
    numbers.removeAll {
        it > 3
    }
    println(numbers)
    val numbers2 = mutableListOf(1, 7, 3, 5, 5, 6, 7)
    numbers2.retainAll {
        it < 6
    }
    println(numbers2)
    //使用 - -= 运算符可以完成集合的写操作。
}