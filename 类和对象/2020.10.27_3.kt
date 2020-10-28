package com.example.android.roomwordssample

open class Rectangle(name:String){
    /*private*/ val fillColor:String
     get() = "blue"

    /*private*/ fun doFillRectangle() {
        println("This is base fillRectangle")
    }
}

/**
 * 使用super关键字可以访问超类中的函数和属性访问器。
 * 这个super关键字可以访问的函数和属性是否可以重写不存在任何的关联,只需要存在继承关系并且可见性修饰符不为private时就可以访问到。
 */
class FilledRectangle(name:String) :Rectangle(name){
    val filledRectangleColor = super.fillColor

    fun doFilledRectangleColorFun() {
        super.doFillRectangle()
        println("My Code!")
    }
    inner class InnerFilledRectangle() {
        fun innerFilledRectangle() {
            println("inner class")
            super@FilledRectangle.doFillRectangle()
        }
        val innerObject:String
            get() = super@FilledRectangle.fillColor
    }
}

open class Rectangle2{
    open fun draw() {
        println("This is Rectangle2's draw")
    }
}

interface Polygon {
    fun draw() {
        println("This is Polygon's draw")
    }
}

/**
 * 继承、实现 中存在函数名称冲突时super关键字需要加上限定字
 */
class MyClass:Rectangle2(), Polygon {
    override fun draw() {
        super<Rectangle2>.draw()
    }
}

fun main() {
//    println(FilledRectangle("XName").filledRectangleColor)
//    println(FilledRectangle("YName").InnerFilledRectangle().innerObject)
//    FilledRectangle("YName").InnerFilledRectangle().innerFilledRectangle()
      MyClass().draw()
}


