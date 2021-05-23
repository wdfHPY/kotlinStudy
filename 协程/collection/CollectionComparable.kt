package com.lonbon.kotlin.collection

/**
 * 排序 Comparable.
 * 1. 存在自然排序。针对一个类需要自然排序的话，那么需要实现的Comparable接口。并且实现Comparable接口。
 * 2.
 */
class CollectionComparable {
}

fun main() {
/*    val v1 = Version(1, 2)
    val v2 = Version(2, 1)
    println(v1 > v2)*/
    collectionPolymerization()
}

/**
 * 定义自然排序的话 -> 正值表示大
 * 负值表示小
 * 0 代表两个对象相等
 */
data class Version(
    val majorVersion: Int,
    val subVersion: Int
) : Comparable<Version> {
    /**
     * 实现Comparable来定义自然顺序。
     */
    override fun compareTo(other: Version): Int {
        if (this.majorVersion != other.majorVersion) {
            return this.majorVersion - this.majorVersion
        } else if (this.subVersion != other.subVersion) {
            return this.subVersion - other.subVersion
        } else {
            return 0
        }
    }
}

/*
*   定义非自然顺序。
*   当不可以为类来定义自然顺序的时候，此时需要定义一个非自然的顺序。
* */
fun notNatureComparable() {

    /**
     * 定义一个比较器。Comparator()并且实现compareTo()方法。
     */
    val comparator = Comparator<String> { t, t2 ->
        t.length - t2.length
    }

    val list = listOf("aaa", "bb", "c")

    //可以调用kotlin的sortedWith方法。传入了一个Comparator即可。
    println(list.sortedWith(comparator).joinToString())

    //如果想快速定义一个的Comparator对象的话，可以使用 compareBy方法。该方法可以快速的定义出一个Comparator对象。
    //定义comparator对象时，需要标注其类型。
    val comparator2 = compareBy<String> {
        it.length
    }
}


fun collectionSort() {
    val list = listOf(1, 2, 3, 4, 5)
    val listVersion = listOf(
        Version(1, 1),
        Version(1, 2),
        Version(2, 3),
        Version(2, 1)
    )
    println(list.sorted())
    println(list.sortedDescending())
    println(listVersion.sortedDescending())
    println(listVersion.sortedDescending())
    /**
     * sorted()和sortedDescending() 两个函数。可以针对存在自然排序的集合进行排序。
     * sorted() 是自然排序的正序。sortedDescending()是自然排序的倒序。
     */

    /**
     * 自定义排序规则。通常存在两个函数sortedBy()
     */
    val listNumber = listOf("one", "two", "three", "four", "five")
    //sortedBy的底层还是调用sortedWith(comparableBy{})
    println(listNumber.sortedBy {
        it.length
    }.joinToString())

    println(listNumber.sortedByDescending {
        it.length
    }.joinToString())

    //sortWith可以使用自己的提供的Comparator
}

/**
 * 集合的倒序。kotlin
 *  1. reversed() 创建一个集合的副本，其中的元素的是接受者排序的逆序。
 *  2. asReversed()
 *  3. reverse() 将集合逆转。
 */
fun collectionReversed() {
    val list = mutableListOf(1, 2, 3, 4, 5, 6)
    val reverse = list.reversed()
    val reverse2 = list.asReversed()
    list.add(7)
    println(reverse) //由于是创建的副本的，所以那么原Collection的更改不会影响到新的倒序的Collection
    println(reverse2) //asRevered和reversed不相同。这个函数是针对引用来。原Collection的变化可以导致新的Collection变化。
    println(list.joinToString())
}

/**
 * shuffled洗牌算法。
 */
fun collectionShuffled() {
    val list = listOf(1, 2, 3, 4, 5)
    //list.shuffled()同样是创建集合的副本然后执行洗牌
    println(list.shuffled())
}

/*
*   集合的聚合操作： 按照某一种规则将集合聚合成一个值。
* */
fun collectionPolymerization() {
    val list = listOf(1, 543, 6, 89)
    val min = list.min() //最小值
    val max = list.max()
    val average = list.average()
    val sum = list.sum()
    println(min)
    println(max)
    println(average)
    println(sum)
    //更高级的sum求和函数sumBy() .在集合的基础上调用上it函数
    println(list.sumBy {
        it * 2
    })

    println(list.sumByDouble {
        it.toDouble() / 2
    })

    val numbers = listOf<Int>(1, 2, 3, 4)
    val sum2 =
        numbers.reduce { sum, element -> sum + element } // s = 5 T = 2、10、4 -> 5 + 2 -> 7 + 10 -> 17 + 4 = 21

    val sum3 = numbers.fold(0) { sum, ele ->
        sum + ele * 2 // 0 + 5 + 2 + 10 + 4 ->
    }

    /**
     * reduce的初始值是第一个元素。在第一个元素的基础上，一直向后进行迭代调用值来调用相对应的方法。
     * 在对元素进行reduce的时候，此时如果集合为空的集合，那么此时会产生异常。
     * 而 fold的初始化的不是第一个元素，而是另外提供的一个初始化值。包括第一个值内一次和初始化做一定的操作。
     * 当list为空的时候，此时不会产生异常，会返回初始值。
     */
    println(sum2)
    println(sum3)

    //fold 和 reduce 默认都是从集合的 left -> right。如何需要从集合的右侧开始进行规约。 foldRight() 或者 reduceRight()
    //通过查看源码发现。参数的顺序产生变化。第二个参数变成累加值。
    val sum4 = numbers.foldRight(0) { sum, ele ->
        ele + sum * 2 // 0 + 5 + 2 + 10 + 4 ->
    }

    //如果需要加上针对Index的判断的话，那么也存在函数
    val sum5 = numbers.foldIndexed(0) { index, sum, ele ->
        if (index % 2 == 0)
            sum + ele
        else
            sum + ele * 2
        //0 + 1 + 4 + 3 + 8
    }

    println(sum4)

    println(sum5)

}