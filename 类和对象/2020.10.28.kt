package com.example.android.roomwordssample

class property {
    val name:String = "Str"
    //val 修饰的属性是不可变的， var修饰的变量是可变的。val 属性不具有setter方法，var属性存在setter、getter
    /**
     *  声明属性完整的方式
     *  1.var <propertyName>[: <PropertyType>] [= <property_initializer>]
            [<getter>]
            [<setter>]
        声明属性时，属性类型可以通过类型推断给自行判断出来，属性初始化器、getter()、setter()方法都是可选的
     */
    /**
     * kotlin中属性必须是需要初始化的。所以property_initializer 和 getter不可以都默认。
     */
//    val key:String 错误 。property_initializer 和 getter都默认了
    val key:Int = 1//property_initializer
    val value
        get() = 2//使用getter 从kotlin 1.1之后，可以从getter访问器来推断属性类型

    var isFull:String
        get() = "cmd"
        /*private*/ set(value) {    //setter getter 可以加上可见性的修饰。
            if(value.equals("cmd")) println("cmd2") else println("cmd3")
        }

}

/**
 * 幕后字段和幕后属性 https://juejin.im/post/6844903673353486343
 */

/**
 * 延迟初始化属性与变量
 */

class MyTest {
    lateinit var subject:Person3//可以提供一个空的属性构造器。延迟初始化初始化变量不能提供自定义setter和getter方法并且不为原生的类型。

    fun setup() {
        subject = Person3("json", "anhui")
    }


}

fun main() {
    val p = property()
    println(p.isFull)
    p.isFull = "cmd"
    println(p.isFull)
}