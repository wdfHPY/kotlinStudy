package com.example.android.roomwordssample

//扩展函数和扩展属性
/*
*   Kotlin 能够扩展一个类的新功能而无需继承该类或者使用像装饰者这样的设计模式。 这通过叫做 扩展 的特殊声明完
    成。 例如,你可以为一个你不能修改的、来自第三方库中的类编写一个新的函数。 这个新增的函数就像那个原始类本来
    就有的函数一样,可以用普通的方法调用。 这种机制称为 扩展函数 。此外,也有 扩展属性 , 允许你为一个已经存在
    的类添加新的属性。
* */

//扩展函数.下面这个函数为不能修改的类编写swap函数。
/**
 *  声明一个扩展函数。需要存在一个 接受者类型 作为他的前缀。
 */
private fun MutableList<Int>.swap(int1:Int, int2:Int){
    val temp = this[int2]
    this[int2] = this[int1]
    this[int1] = temp
    /**
     * 在扩展函数当中，使用this关键字来代替被扩展的接受者对象。
     */
}

/**
 * 可以看到MutableList中元素的顺序产生了变化
 */

/**
 * 扩展不能真正的修改他们所扩展的类。通过定义一个扩展,你并没有在一个类中插入新成员, 仅仅是可以通过该类型的变量用点表达式
 * 去调用这个新函数。
 * 扩展函数的接受者类型仅仅只是由于调用者所在的表达式来决定的。并不受继承多态的影响，因为扩展函数是静态解析的。
 */

open class shape

class rectangle:shape()

private fun shape.getName() :String{
    return "This is shape extendtion function"
}

private fun rectangle.getName() :String{
    return "This is rectangle extendtion funtion"
}

fun printlnClassName(s: shape) {
    println(s.getName())
}


/**
 * 当扩展函数和类中函数的签名完全相同的时候
 * 此时对象调用函数时，总是会调用类中的成员函数而非扩展函数
 *
 */

class Earth(val name:String , val code:Int) {

   fun earth() {
       println("This is a class method")
   }
}

fun Earth.earth() {
    println("This is a extenstion method")
}

fun Earth.earth(name:String) {
    println("This is a other extenstion method")
}

/**
 * 可以为可空的接收者类型定义扩展。这样的扩展可以在对象变量上调用, 即使其值为 null,并且可以在函数体内检测 this == null ,这能让你在没有检测 null
 * 的时候调用 Kotlin 中的toString():检测发生在扩展函数的内部。
 */
fun Earth?.getEarth() :Earth{
    if(this == null) {
        return Earth("earth", 2)
    } else {
        return this
    }
}

/**
 * 扩展属性。扩展属性是不能够直接使用初始化器进行构造的。原因在于不是属于接收器类中的。不能够生成幕后属性。
 * 扩展属性仅仅能让转换成一个函数。
 */

val List<Person3>.lastPerson:Int
        get() = size - 1


fun main() {
//    val list = mutableListOf(1,2,3)
//    list.swap(0, 2)
//    list.forEach {
//        println(it)
//    }

//    printlnClassName(rectangle())//可以看出扩展函数实在编译时静态解析的。他会根据你调用者的表达式的来确定。

    val earth = Earth("Earth",2)
    earth.earth()//总是会调用Earth类中的函数而非扩展函数。
    earth.earth("take")//扩展函数相同名称但是名字不相同的话可以正常的运行。
    earth.getEarth().earth()
    /**
     * 扩展属性测试
     */
    val lists:List<Person3> = listOf(Person3("kt","cn"),Person3("java","cn"))
    println(lists.lastPerson)
}