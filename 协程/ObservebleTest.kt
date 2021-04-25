package com.lonbon.kotlin

/**
 * 验证 list 的深拷贝
 */
fun main() {
    val list = listOf<String>("1", "2", "3")
    val list2 = list.toMutableList()
    val list5 = list
    println(list === list2) //toMutableList() is deep copy
    println(list === list5) //toMutableList() is deep copy

    val list3 = mutableListOf<String>("1","2","3")
    val list4 = list3.toList()
    println(list3 === list4) //toMutableList() is deep copy
}