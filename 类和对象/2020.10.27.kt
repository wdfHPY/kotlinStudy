package com.example.android.roomwordssample

/**
 * Kotlin 中使用关键字 class 声明类 。kotlin类中包括四个部分
 * 1. class 关键字
 * 2. Test 类名
 * 3. 类头 （可选的类型参数参数) + 主构造函数）
 * 4. 类体 不存在类体的话可以省略大括号
 */
class Test {
}

/**
 * kotlin 可以存在多个构造函数。包括一个主构造函数和多个次构造函数。
 * 类名之后的构造器为main constructor
 * 构造函数 `constructor(name: String)` name、sex为主构造器的参数。
 */
class Person constructor(name: String, sex: Int) {
    fun Println() {
        println("Hello Person")
    }
}

/**
 * 如果不存在可选类型参数或者注解，那么main constructor 关键字 constructor 可以省略
 *  构造一个private 私有的构造器时 private constructor 关键字constructor 不可以省略
 */
class Animal private constructor(name: String, sex: Int) {
    fun Println() {
        println("Hello Animal")
    }
}

/**
 * main constructor could't contains any codes。
 * default main constructor access permission is public。
 * access main constructor param
 * way1: access in initializer code block
 * way2：access in field initializer
 */
class Student (name: String, sex: Int) {

    /**
     * initializer code block.
     * initializer code block there can be multiple
     * init 代码块可以存在多个，多个按照出现在类中的顺序依次执行
     */

    val nameField = name

    init {
        println("Student sex is $sex")
    }

    init {
        println("Student name is $name")
    }


    fun Println() {
        println("Hello $nameField")
    }

    /**
     * 访问主构造函数的参数存在两种方式
     * 1. init代码块
     * 2. 属性参数构造器
     */
}

/**
 * 声明对象加上构造函数的简略写法
 * 可以将类定义属性和主构造函数结合起来。
 */
class Bird constructor(val kind:String, val age: Int) {
    fun Println() {
        println("Bird kind is $kind, and age is $age")
    }
}

/**
 * 由于属性构造器可以访问主构造器的参数
 * 下面类的写法就和上面简略写法一样。就相当定义的一个和主构造器参数同名的属性
 */
class Bird2 constructor(kind: String, age: Int) {
    val kind:String = kind
    val age:Int = age
    fun  Println() {
        println("Bird kind is $kind, and age is $age")
    }
}

fun main() {
    Student("James" , 1).Println()
    Bird("fly", 2).Println()
    Bird2("fly", 2).Println()
}

