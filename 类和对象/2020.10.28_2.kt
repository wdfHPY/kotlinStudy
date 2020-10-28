package com.example.android.roomwordssample

/**
 * 默认情况下，Work 下的抽象(方法、属性)、非抽象(方法、属性)默认都是open的
 */
interface Work{
    val personName:String  //可以定义抽象的属性
    val personAge:Int
        get() = 1  //接口也可以定义非抽象的属性，但是接口不存在的构造函数和属性初始化器。那么就需要提供属性访问器。
    fun work()

    fun detailWorkFor() {
        println("This is a detail Work Function")
    }
}

/**
 * 实现接口时，只会覆盖 未提供属性访问器的属性和未提供具体实现的方法。
 */
class WorkPerson(override val personName: String) :Work {
    override fun work() {
        super.detailWorkFor()
    }

    fun println() {
        println(super.personAge)
    }
}

/**
 * 接口派生接口
 */
interface Study : Work {
    val name:String
    fun study()
    fun havefun()
}

/**
 * 当接口派生于接口时，此时会实现接口往上所有未实现的方法和属性的
 */
class doStudy(override val name: String) :Study {

    //提供具体实现
    override val personName: String
        get() = "offer detail implements"


    override fun study() {
        TODO("Not yet implemented")
    }

    override fun havefun() {
        TODO("Not yet implemented")
    }

    override fun work() {
        TODO("Not yet implemented")
    }

}

fun main() {
    WorkPerson("kt").println()
    WorkPerson("kt").work()
}

