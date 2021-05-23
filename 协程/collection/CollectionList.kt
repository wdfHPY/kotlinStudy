package com.lonbon.kotlin.collection

import kotlin.math.sign

fun main() {
    collectionListWrite()
}

/**
 * List 获取元素 - 单个元素
 */
fun collectionListGetElement() {
    val list = listOf("one", "two", "three", "four")
    //list 中可以方便的通过[] 和 get()函数来访问列表中的元素
    println(list[1])
    println(list.get(2))
    //传入错误的索引的时候，此时list会产生相对应的异常。
    //println(list[-1])
    //防止程序产生一定的崩溃的话，那么使用getOrNull来代替get()函数，当不存在相对应的索引的值的时候，会产生null.
    println(list.getOrNull(-1))
    //getOrNull -> 不正确的索引值会返回lambda表达式的值。
    println(list.getOrElse(-1) {
        56
    })
}

fun collectionPartElement() {
    val list = mutableListOf("one", "two", "three", "four")
//    val element = list.subList(0,1)
//    println(element)
    list.add(0, "kotlin")
    println(list)
}

fun collectionFind() {
    //indexOf()可以快速定位元素的位置。如果不存在相对应的函数的话，那么就会返回为-1
    val listString = listOf("one", "two", "three", "four", "two")
    println(listString.indexOf("two")) //indexOf 是从list 的开始和谓词匹配的元素
    println(listString.indexOf("kedin"))
    println(listString.lastIndexOf("two")) //lastIndexOf是从list最后一个和谓词匹配的元素
    //indexOfFirst的话传入的是一个lambda表达式。
    println(listString.indexOfFirst { it.length < 6 })
    println(listString.indexOfLast { it.length > 1 })

    //indexOf 和 find first last 等Api 还是存在的区别，区别在于后者会返回其元素本身，前者更偏向于元素在
    //list的定位
    println(listString.find {
        it.startsWith("o")
    })
}

/**
 * list 的二分法搜索
 */
fun collectionBinarySearch() {
    val listString = listOf("one", "two", "three", "four", "two", "one").sorted()
    println(listString)
    println(listString.binarySearch("one")) //
    println(listString.binarySearch("o1ne")) //如果二分法没有搜索到的话，那么返回的值可以肯定的是负数
    println(listString.binarySearch("four")) //如果二分法没有搜索到的话，那么返回的值可以肯定的是负数
    //二分法还可以指定索引区域。fromIndex 指定开始的Index, toIndex指定结束的Index
    println(listString.binarySearch("two", 0, 5)) //二分法搜索额时候需要注意index
    //不要超出界限，否则就会出现异常。

    //如果List不存在顺序的话，那么需要提供Comparator来binarySearch
    val list2 = listOf(
        Produce("WebStorm", 988.0),
        Produce("AppCode", 99.0),
        Produce("DotTrace", 129.0),
        Produce("ReSharper", 149.0)
    )
    //针对上面数据类型不是Comparable类型，需要自定义排序规则。

    println(list2.sortedWith(compareBy<Produce> {
        it.price
    }.thenBy {
        it.name
    }))
    println("result: ${
        list2.binarySearch(Produce("WebStorm", 988.0), compareBy<Produce> {
            it.price
        }.thenBy {
            it.name
        }
        )
    }")
    println(list2.binarySearch {
        priceComparison(it, 2.75)
    })
}

data class Produce(
    val name: String,
    val price: Double
)

fun priceComparison(product: Produce, price: Double) = sign(product.price - price).toInt()

fun signFunctionTest() {
    println(sign(-1.0))
    println(sign(1.0))
}

/**
 * list的操作包括list的插入， 删除， 更新， 排序
 */
fun collectionListWrite() {
    val list = mutableListOf(1, 2, 3, 4, 5, 6)

    //list的新增 add()和addAll()函数
    list.add(9)
    list.addAll(1, listOf(1, 41, 1))
    println(list)

    //list的更新
    list[1] = 0
    println(list)

    //list fill -> 将集合整个fill为该元素
    list.fill(9)
    println(list)

    //list的删除
    list.remove(9) //remove只是会删除掉单个元素。

    list.removeAll(listOf(9)) //remove 删除全部。

    //list的排序和只读列表的排序差不多。但是函数的名称产生了一定的变化，由之前的sorted -> sort去掉了ed
    //shuffle() 代替 shuffled()
    //reverse() 代替 reversed()。

    println(list)
}

