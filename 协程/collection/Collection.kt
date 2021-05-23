package com.lonbon.kotlin.collection

/*
*   kotlin的集合可以分为两种。
*       1. 只读集合：只允许集合进行读取操作。无法写入，删除等等对集合元素产生变化的操作。
*       2. 可变集合：可以写入的集合，可以对集合元素进行一些操作的集合类型。
*   - 只读集合是 型变 的。而可变集合是不可型变的。（型变：子类型可以代替父类型的一种机制）
*   - 更具体一点：只读集合中 List Set 和 Map.value 均是可型变的。Map.key 不可型变。
*   - 可变集合都是 不可型变的。
* */


fun main() {
    collectionTest6()
}

fun collectionTest1() {
    //Set 在定义时是不存在顺序型的。但是默认情况下，Set的默认实现是LinkedHashSet，此实现会保留元素插入的顺序。
    //所以在LinkedHashSet的上也可以返回可预测的预测效果。
    val set1 = setOf(1, 2, 3, 4)
    val set2 = setOf(4, 3, 2, 1)
    println(set1.first() == set2.last())
}

fun collectionTest2() {
    val map = mapOf(1 to "str1", 2 to "str2", 3 to "str3")
    println("All keys:${map.keys}") //map的key是唯一的，所以map的key通常都是使用Set
    println("All values:${map.values}") //map的value是Collection类型。
    //由于Collection 的 key 和 value 都是使用集合的形式。所以可以使用 in 关键字来元素判断是否存在于集合之内。
    if (1 in map.keys) {
        println("Key And Value:${map[1]}")
    }
}

/**
 * 构建集合方式 - 通过元素来构建。
 */
fun collectionTest3() {

}

fun collectionTest5() {
    val originalList =
        listOf("Even", "if", "the", "wind", "blows", "up", ",", "I", "have", "a", "way", "home")

    val result = originalList.filter { //filter 函数过滤元素时不会影响到原 Collection.底层将元素添加到新的List中了。
        it.length > 3
    }

    //在某些集合操作当中，存在一些目标参数。比如 filterTo, 如果目标参数是可变集合的话，那么结果会加到可变的集合当中去。

    val result2 = mutableListOf("Nurture", "radio")

    originalList.filterTo(result2) {
        it.length > 3
    }

    val result3 = originalList.filterIndexedTo(result2) { index, _ ->
        index == 0
    }

    println(originalList)

    println(result)//直接返回了一个结果。

    /**
     * 将集合的结果附加到目标参数中。依然不影响到原Collection。
     * 本质上 filter底层调用的就是 filterTo
     */
    println(result2)

    println(result3)
}


fun collectionTest6() {
    val list = mutableListOf("one", "two", "three", "four")
    val list2 = list.sorted() //深拷贝了一个新的集合。其中集合元素是排序好的。

    println(list)
    println(list2)
    println(list.sort()) //就地对集合产生变化。
}