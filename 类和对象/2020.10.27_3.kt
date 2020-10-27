package com.example.android.roomwordssample

open class Rectangle(name:String){
    val fillColor:String
     get() = "blue"

    fun doFillRectangle() {
        println("This is base fillRectangle")
    }
}

/**
 * 使用super关键字可以访问超类中的函数和属性访问器。
 * 这个super关键字可以访问的函数和属性是否可以重写不存在任何的关联
 */
class FilledRectangle(name:String) :Rectangle(name){
    val filledRectangleColor = super.fillColor

    fun doFilledRectangleColorFun() {
        super.doFillRectangle()
        println("My Code!")
    }
}

fun main() {
    println(FilledRectangle("XName").filledRectangleColor)
    FilledRectangle("YName").doFilledRectangleColorFun()
}

