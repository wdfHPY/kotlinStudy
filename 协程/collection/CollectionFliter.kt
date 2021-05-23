package com.lonbon.kotlin.collection

fun main() {
    collectionElementAt()
}

fun collectionFilter() {
    /*
    *   针对 List 和 Set，filter 过滤的结果是一个 List。针对 Map 来说，Map 的过滤结果仍然是一个 Map
    * */
    val list = listOf("There", "had", "iojs", "xexs")
    val afterFilter = list.filter {
        it.length > 3
    }

    val mapx = mapOf("x" to 1, "kotlin" to 2, "scala" to 3)
    val result = mapx.filter { (key, value) ->
        key.startsWith("x") && value < 2
    }
    println(afterFilter.joinToString())

    println(result)

    /**
     * 按照元素的类型来过滤集合
     *  filterIsInstance.调用 filterIsInstance() 返回的是一个一个 List.Map 上不可调用 filterIsInstance。
     */

    val kil = listOf("x", 89.6, 1, 234.3F, "y")

    val fil = kil.filterIsInstance<String>()

    println(fil)

    /**
     * 过滤掉所有null元素。filterNotNull()。一旦调用在的List<T?>上调用filterNotNull之后，那么返回的就是List<T>
     */
}

/**
 * 集合的划分。按照给定的谓词来将集合划分为两个部分。第一个部分是满足划分的谓词的，第二个部分是没有划分谓词的
 */
fun collectionPartition() {
    val list = listOf("list", "kotlin", "set", "hashMap")
    //partition 会返回一个Pair<List<T>, List<T>>
    val (first, second) = list.partition {
        it.length > 3
    }
    //返回的 first 是符合partition的谓词。而second 是不符合partition的谓词
    println(first)
    println(second)
}

/*
* 集合的分組：按照 key 来进行分组，分组的结果是一个Map。map的key是分组的元素
* 而value是一个集合。集合包含的内容是原集合所有满足条件的元素。
*
* */
fun collectionGrouping() {
    val numbers = listOf("one", "two", "three", "four", "five", "five")
    println(numbers.groupBy {
        it.first().toUpperCase()
    })
    println(numbers.groupBy(keySelector = {
        it.first().toUpperCase()
    }, valueTransform = {
        it.length
    }))
    /**
     * 使用 groupingBy() 会返回Grouping对象。
     */
    println(numbers.groupingBy {
        it.first().toUpperCase()
    }.eachCount())
}

/*
*   取集合的一部分。
* 1. slice() -> 片 通过传入的Collection Index 来返回一个集合
*    slice() 的参数可以是区间，也可以是Iterator对象。
* 都是元素作为Index。将这些元素作为Index，取出相对应Index对应的元素。
*
* 2. take() 和 drop()
* */

fun collectionPart() {
    val list = listOf("kotlin", "Hello", "World", "cancel", "do while", "While")
    println(list.slice(1..2 step 2))
    println(list.slice(listOf(1, 1, 1)))

    //take函数可以从头获取集合的一部分。
    println(list.take(2))
    //take函数如果超出Collection的最大值的话，那么还是会返回集合。
    println(list.take(20))

    //从集合的后面取对应个数的元素。
    println(list.takeLast(2))

    //丢弃list开头的第一个。
    println(list.drop(1))

    //丢弃list的结尾的第几个
    println(list.dropLast(2))

    //带谓词的take. -> takeWhile() -> 从list中取出元素直到遇上不匹配的谓词。
    //如果首个谓词就不匹配的话，那么集合为空。
    println(list.takeWhile {
        it.startsWith("k")
    })

    //takeWhile() -> 是从集合的开头的来匹配的。 而takeLastWhile()是从集合的结尾来取出元素。
    println(list.takeLastWhile {
        it.startsWith("W")
    })

    //从头drop元素直到改元素不符合谓词为止
    println(list.dropWhile {
        it.startsWith("k")
    })

    //从尾drop元素直到改元素不符合谓词为止。
    println(list.dropLastWhile {
        it.startsWith("W")
    })
}

/**
 * 将集合划分成给定大小的块。chunked。将集合进行chunked分块操作。
 * 返回的是List<List>
 * chunked后面可以跟随lambda 表达式来chunked之后的集合进行
 * 相应的计算
 */
fun collectionChunked() {
    val list = (1..13).toList()
    println(list.chunked(3).joinToString())
    println(list.chunked(2) {
        //这些List是临时的List
        it.count()
    })
}

/**
 * step 表示可以窗口起点之间的间距。默认的间距为1。
 * 底层chunked划分的块的函数调用就是window函数。只不过start 和 step 相等。
 * window函数同样也可以包括对其使用的lambda表达式。
 */
fun collectionWindow() {
    val list = listOf(1, 2, 3, 4, 5, 6, 7, 8)
    println(list.windowed(3, step = 3, partialWindows = true).joinToString())

    //如果需要创建一个两个元素的窗口的话，那么存在其他的方法。zipWithNext() zipWithNext的起始点 step 永远是1。所以这个是不会产生partition的。
    //zipWithNext返回的List中的元素时Pair。
    println(list.zipWithNext())
}

fun collect() {
    val numbers = listOf("one", "two", "three", "four", "five")
    println(numbers.zipWithNext())
    println(numbers.zipWithNext() { s1, s2 -> s1.length > s2.length })
}

/**
 * 取出特定位置的元素
 * 一般针对List这个存在序列的集合。一般可以使用get()或者[]来访问元素。
 * 针对于set这种无序的集合，可以使用elementAt()来确定元素的位置。
 */
fun collectionElementAt() {
    val set = setOf("hello", "google", "cancel", "location", "index")
    println(set.elementAt(1))
    /**
     * elementAtOrNull -> 当传入的index超出了元素的个数 - 1的时候会抛出null异常。
     */
    println(set.elementAtOrNull(1))
    /**
     * 当对应的索引值为null的时候，会返回后面lambda所对应的值。
     */
    println(set.elementAtOrElse(10) {
        98
    })

    //返回第一个元素。不存在抛出异常
    println(set.first())

    //返回第一个元素。并且需要符合谓词特征 不存在抛出异常 -> firstOrNull异常抛出的问题。
    println(set.first {
        it.startsWith("c")
    })

    //返回最后一个元素。不存在抛出异常
    println(set.last())

    //随机取元素集合的元素
    println(set.random())
}