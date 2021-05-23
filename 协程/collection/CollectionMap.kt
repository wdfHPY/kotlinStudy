package com.lonbon.kotlin.collection

fun main() {
    mapDeleteFunction()
}

/**
 * Map 获取值，通过get()函数可以获取值。get()函数需要一个key参数
 */
fun collectionMapGetValue() {
    val map1 = mapOf("str" to 1, "kotlin" to 2, "smart" to 3)
    //通过get或者[]的参数来获取的map的值。如果键值不存在的话，那么就会返回值为null
    println(map1["str1"])

    //getValue同样也是获取的map的值。只不过在寻找不到键的时候和get()的行为存在不同的表现。
    println(map1.getValue("str"))
    //println(map1.getValue("str1"))//当键不存在的时候，此时会直接抛出异常。
    println(map1.getOrDefault("str1", 89)) //当键不存在的时候，默认值为89
    println(map1.getOrElse("str1") {
        87
    })
    println(map1.keys) //获取map 键的集合
    println(map1.values) //获取map 值的集合
}

/**
 * map的过滤主要存在三个函数。第一个函数filter{}，lambda 接受两个参数,第一个参数代表key，第二个参数代表着value。
 * map的filter lambda 参数是Pair
 */
fun mapFilter() {
    val map1 = mapOf("str" to 1, "kotlin" to 2, "smart" to 3)
    val map2 = map1.filter { (key, value) ->
        key.length > 2 && value == 1
    }
    //如果需要针对map 的value或者的key 进行过滤的时候，此时可以使用其他的函数
    val map3 = map1.filterKeys {
        it.length > 3
    }

    val map4 = map1.filterValues {
        it > 2
    }
    println(map2)
    println(map3)
    println(map4)
}

/**
 * map和list以及set一样。都是可以使用 + - 操作符的
 */
fun mapPlusAndMinus() {
    val map1 = mapOf("one" to 1, "two" to 2, "three" to 3)
    val map2 = map1 + Pair("one", 10) //当map + 右侧的Pair的Key包含在左侧map中的话，此时value值会被替换
    val map3 = map2 + Pair("four", 4)
    println(map2)
    println(map3)
    //map的删除。map的删除 - 左侧可以是list 或者 set 或者单个键值
    val map4 = map1 - "one"
    val map5 = map1 - listOf("two", "four")

    println(map4)
    println(map5)
}

/**
 * map对值进行更新.使用put()函数可以向map中添加元素。
 * 使用putAll()函数可以向map中添加多个元素。
 */
fun collectionUpdateValue() {
    val map1 = mutableMapOf("one" to 1, "two" to 2, "three" to 3)
    map1.put("four", 4) //set()也可以向map添加值。
    map1.putAll(setOf(Pair("x", 4), Pair("y", 5)))
    println(map1)
    //put()和的putAll向Map中添加元素，如果元素存在的话，那么就进行更新值
    map1.put("one", 10)
    map1.putAll(setOf(Pair("one", 12), Pair("two", 22)))
    println(map1)
}

fun mapDeleteFunction() {
    val map1 = mutableMapOf("one" to 1, "two" to 2, "three" to 3)
    map1.remove("two")//仅通过Key来删除map中元素。
    println(map1)
    val map2 = mutableMapOf("one" to 1, "two" to 2, "three" to 3)
    //当map.remove同时传入key 和 value 的时候，此时Key 和 value都需要匹配才可以删除。
    map2.remove("two", 1)
    println(map2)
    //map 可以通过Key来删除掉map中的元素。
    map2.keys.remove("one")
    println(map2)
    //map 可以通过value来删除map中的元素
    val map3 = mutableMapOf("one" to 1, "two" to 2, "three" to 2)
    //因为map 的value可以重复的，那么删除第一个元素就是删除掉第一个value匹配的元素。
    map3.values.remove(2)
    println(map3)

}