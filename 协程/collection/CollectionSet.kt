package com.lonbon.kotlin.collection

/**
 * Set的相关操作。
 */
fun main() {
    collectionSubtract()
}

/**
 * 进行Set的并集操作。使用函数union -> 来进行set的并集操作。
 */
fun collectionUnion() {
    val set1 = setOf("one", "two", "three")
    val set2 = setOf("four", "five")
    println(set1 union set2)
    println(set2 union set1)
}

/**
 * 求出集合的交集操作。使用函数intersect()求出set之间的并集操作
 */
fun collectionIntersect() {
    val set1 = setOf("own", "kotlin", "uiu", "0p0")
    val set2 = setOf("own", "kotlin", "uiu", "0p0")
    println(set1 intersect set2)
}

/**
 * 通过subtract 函数求出两个set之间的差集。
 */
fun collectionSubtract() {
    val set1 = setOf("own", "kotlin", "uiu", "0p0")
    val set2 = setOf("own", "kotlin")
    println(set1 subtract set2)
}