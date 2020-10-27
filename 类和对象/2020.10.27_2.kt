package com.example.android.roomwordssample

/**
 * 类的继承：
 * 1. kotlin 中每一个的类都存在一个共同的超类Any。每一个类都是继承Any的。
 * 2. kotlin 中的类默认是final 关键字修饰的。默认kotlin中类不可以被继承。需要使用关键字open来修饰才能够被继承
 * 3. 继承一个类那么就将待继承的类名称放在类头的后面
 */
open class Person3(name:String, address:String) {
    constructor(name:String, address:String, hobby:String):this(name, address)
}

/**
 * 如果派生类存在主构造函数，那么一定要使用派生类参数就地来初始化基类
 */
class Boby(val name:String, val address:String, sex:Int) :Person3(name,address) {

}

/**
 * 如果派生类不存在主构造函数，那么相对应每一个派生类从构造器都是需要使用super关键字来初始化其基类的构造器（基类的构造器不一定是主构造器、
 * 还可能是从构造器）
 * super关键字可以不必要一定出现。从构造器可以通过委托到其他从构造器。
 */

class Body2 : Person3{

    constructor(name:String, address:String): super(name,address)

    constructor(name:String, address:String, hobby:String): super(name,address,hobby)

}

/**
 * 覆盖类中的方法
 */

open class Shape(name:String) {
    open fun draw() {
        println(" draw shape ")
    }

    fun fill() {
        println(" fill ")
    }
}

/**
 * 和类的继承一样，方法的覆盖也需要基类方法进行开放open
 */
open class Circle(name:String):Shape(name){
    /**
     * 使用 override 关键字修饰的方法本身还是open的。如果不需要继承的不能给子类实现的话，加上关键字final即可
     */
    final override fun draw() {
        println(" draw Circle ")
    }
    //'fill' hides member of supertype 'Shape' and needs 'override' modifier
    /**
     * 子类不可以定义和基类不open 同名的方法
     */
//    fun fill() {
//        println(" Circle Fill")
//    }
}

class Oval(val name:String, val type:String):Circle(name) {
//    override fun draw() {
//        println(" draw Oval ")
//    }
}

fun main() {
    Oval("ShapeCircle","type1").draw()
}
