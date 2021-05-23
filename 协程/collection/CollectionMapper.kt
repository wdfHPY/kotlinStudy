package com.lonbon.kotlin.collection

import java.util.*


fun main() {
    collectionJoin()
}

/**
 * Collection 映射 Mapper 操作。
 * 在一个集合的基础上，通过 map 操作，创建出一个新的集合。该集合中的元素顺序和映射之前的元素的顺序一致
 */
fun collectionMapper() {
    val originalList = listOf<Int>(14, 3, 1, 4, 4)

    val originString = mutableListOf("kotlin", "Groovy", "Scala")

    val originMap = mapOf("KEY1" to 1, "Key2" to 2, "Key3" to 2)

    originalList.map {
        it * 2 + 1
    }

    originalList.mapTo(originString) {
        (it + 1).toString()
    }

    originalList.mapNotNullTo(originString) {
        if (it == 3) null else it.toString()
    }

    // map和 filter 一样，都是可以附加目标参数的。 底层原理相同， 都是调用mapTo
    println(originalList)

    // mapTo 和 Filter 附加参数时。目标参数需要是可变的。
    println(originString)

    //对Map的映射操作存在两种，一种是针对keys, 其他一种针对values

    val targetString = mutableListOf<String>()

    val result2 = originMap.mapKeys {
        it.key.toUpperCase(Locale.getDefault()) //通过 .key 来对Key集合做映射操作。
    }

    val result = originMap.mapValues {
        it.value + 1 //通过 .value 来对Value集合做映射操作。
    }

    println(targetString)

    println(result)

    println(result2)

    println(originMap)
}


/*
* 合拢 和 拆分 操作。
* */
fun collectionZipper() {

    //zip 合拢之后的依旧是一个的List，只不过 List 的元素的 Pair 类型的

    val listStrings = listOf("str1", "str2", "str3", "str4")

    val listInts = listOf(1, 2, 3, 4, 5, 6)

    val zipedList = listStrings zip listInts

    val zipedList2 = listInts zip listStrings

    // 接受者作为 zip 之后 Map 的 key

    println(zipedList)

    println(zipedList2)

    // unzip() -> Pair 拆解成两个 List

    val firstList = zipedList.unzip().first

    val secondList = zipedList.unzip().second

    println(firstList)

    println(secondList)
}

/*
* 关联：和关联相关的Collection操作有很多。
* */
fun collectionAssociateWith() {
    // associateWith()。调用 associateWith() 的话，接受者将成为 Map 的键，与之相关联的值成为 Map 的 value
    val list = listOf("str1", "str2", "str3", "str4", "str5", "str9", "str100")

    val mutableMap = mutableMapOf("str100" to 100, "str133" to 133)

    val map1 = list.associateWith {
        it.length - 1
    }

    // 原有的 associateWith会确保唯一性。
    list.associateWithTo(mutableMap) {
        it.length - 1
    }

    // associateWith() -> Map的 Pair (str1,3)
    println(map1)

    println(mutableMap)

    //associateBy() 和 associateWith()的定义类似。associateWith是通过集合来生成值。 associateBy()是通过集合来
    //生成Map的键
    val map2 = list.associateBy {
        it.length - 1
    }

    //相对于associateWith()只能关联出一个value.associateBy()不仅仅可以完成关联一个key.同时还可以关联一个value.
    val map3 = list.associateBy(keySelector = {
        it
    }, valueTransform = {
        it.length - 1
    })

    //associate() 创建一个Map。在associate的过程中， 会生成临时的Pair对象。这相对会影响性能。
    val map4 = list.associate {
        it.substring(1) to it.length
    }

    println(map2)

    println("map3 = ${map3}")
}

/**
 * 集合的打平。集合中嵌套集合打平成一个集合。
 * 适用于集合的打平一定是存在集合嵌套的。否则调用不了集合的flatten操作。
 */
fun collectionFlatten() {
    val complexSet = setOf(listOf("one", "two", "three"), listOf(1, 2, 3), listOf(2.1F, 78.89F))
    println(complexSet.flatten())

    val list = setOf("number", "This", "Unit", "Home", "kid")

    //flatMap。将集合元素映射集合之后在进行打平操作。flatMap的表现形式就是针对每一个元素进行map生成一个集合。然后对这个
    //集合打平
    println(list.flatMap {
        listOf(it.length + 1, it.length - 1)
    })
}

/*
*   集合的字符串显示： 存在两个的函数 joinToString() 和 joinTo()
* */
fun collectionJoin() {
    val list = listOf(1, 2, 3, 4, 5, 6, 7, 8)
    println(list.joinToString()) //joinToString() 从集合元素中构建成一个的字符串对象。
    println(list.toString())

    val str = StringBuffer("This is list of:") //joinTo()函数将集合转换成string并附加到Append对象上去。
    println(list.joinTo(str))

    //joinToString()可以附带参数。
    /**
     * separator -> 用来指定分隔符
     * prefix -> 指定前缀的
     * postfix -> 指定后缀
     * limit -> 指定显示几个元素，之后的元素使用 [truncated]显示的字符串所代替
     * transform lambda 表达式 -> 元素进行的转换
     */
    println(
        list.joinToString(
            separator = " * ",
            prefix = "Prefix",
            postfix = "Postfix",
            limit = 1,
            truncated = "xxxxx"
        )
    )
}