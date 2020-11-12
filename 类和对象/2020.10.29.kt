package com.example.android.roomwordssample

/**
 * 导入其他package中的扩展函数。导入定义在顶层的扩展函数
 */
import com.example.android.roomwordssample.swap

/**
 * 扩展函数还可以定义在类的内部
 */

class Host(val name:String) {
    fun printHostName() { println(name) }

    override fun toString(): String {
        return "This is Host toString Function"
    }
}

/**
 * host为 扩展接受者 对象
 */
class Connection(val host: Host, val port:Int){
    fun printlnPort() {  println(port) }

    fun Host.printlnString() {
        printHostName()//  Host.println()
        println(":")
        printlnPort() //Connection.printlnPort()
        /**
         * 当 分发接受者 和 扩展接受者 方法的名称产生冲突时
         * 优先的是 扩展接受者
         */
        println(toString())
        println(this@Connection.toString())//如果要优先调用分发接受者的方法的话的，需要@来指定
    }

    fun connect() {
        host.printlnString()
    }

    override fun toString(): String {
        return "This is Connection toString Function"
    }
}

/**
 * 扩展接受者函数解析是静态的。分发接受者函数解析是虚拟的。
 * 关于静态和动态的话，区别就在于是否可以体现出多态性
 */

open class MyBase

class MyDerived:MyBase()

open class MyBaseCaller {

    open fun MyBase.printFunctionInfo() {
        println("Base extension function in BaseCaller")
    }
    open fun MyDerived.printFunctionInfo() {
        println("Derived extension function in BaseCaller")
    }
    fun call(b: MyBase) {
        b.printFunctionInfo()
    }
}

class MyDerivedCaller:MyBaseCaller() {
    override fun MyBase.printFunctionInfo() {
        println("Base extension function in MyDerivedCaller")
    }

    override fun MyDerived.printFunctionInfo() {
        println("MyDerived extension function in MyDerivedCaller")
    }
}




fun main(){
//    val personlist:MutableList<Int> = mutableListOf(1 , 2 , 3)
//    personlist.swap(0, 2)
//    personlist.forEach {
//        println(it)
//    }
    //类中扩展成员函数
    //Connection(Host("192.168.101.0"), 8080).connect()//分发接受者
    //Host("158.168.9.7").printlnString()

    /* -----------------------------------------------*/
    MyBaseCaller().call(MyBase())
    MyDerivedCaller().call(MyBase())
    //分发处理器 解析函数是动态的。也就是一定会调用分发者实例的函数。和表达式的结果存在关系

    MyDerivedCaller().call(MyDerived())
    //扩展处理器 解析函数是静态的。只会和调用者的表达式本身存在关联。和表达式的结果没有