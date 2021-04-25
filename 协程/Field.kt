package com.lonbon.kotlin

import java.io.File

class Field {
    val req = 1 // 使用初始化器来初始化属性,默认不显示 setter 和 getter

    val req2 get() = run {//通过 getter来声明一个属性。
        println("This is the getter")
    }

    companion object {
        val x = "x"
    }
}


fun main() {
    Field().req
    println(Field().req2)
    Field.x
}