package com.example.android.roomwordssample

//数据类data
/**
 * 数据data class 使用关键字 data 来标识这个是一个数据类
 *      1.data class 会生成hashCode、equals
 *      2.componentN 解构方法
 *      3.toString 方法
 *      4.copy 方法
 *   需要注意的是：数据类生成这个方法只会针对主构造器来生成方法。所以
 *   如果将属性类体当中的话，那么不会生成方法
 */
 data class User(
        val name: String,
        val age: Int
)


//sealed class 密封类

sealed class Test9
data class Perz(
        val sex: String,
        val age: Int
): Test9()

/**
 * 密封类的子类可以包含多个状态的多个实例。要声明一个密封类,需要在类名前面添加 sealed 修饰符。虽然密封类也可以有子类,
 * 但是所有子类都必须在与密封类自身相同的文件中声明。但是密封类子类的派生类没有这个限制.密封类自身是abstract的，本身是不能够进行
 * 实例化的。
 */
object Numner: Test9()

fun main() {
    // data class 的复制 内部实现的copy函数
    val jack = User("wdf", 23)
    val sondon = jack.copy(name = "wy")
    println(sondon.age)
    println(sondon.name)

    val (name, age) = sondon //内部调用componentN 进行数据类的解构
    println("jack name is $name and age is $age")

    /**
     * 当然可以重写解构函数....
     */
}