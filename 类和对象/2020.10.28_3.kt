package com.example.android.roomwordssample

/**
 * kotlin 中存在四种可见性的修饰符。分别是private\public\internal\protected四种可见性修饰符
 * 顶层声明：直接定义在 package 内
 *         1.函数、属性和类、对象和接口可以在顶层声明,即直接在包内。
 *         2.不指定任何可见性修饰符，此时默认的可见性修饰符为public。
 *         3.指定可见性修饰符private，那么本包内是可见的。
 *         4.指定可见性为public，那么模块之内是可见的。
 *         5.protected可见性修饰符不可以修饰在顶层声明  /* Modifier 'protected' is not applicable inside 'file' */
 * 非顶层声明:(类和接口)
 *          1.private 可见性表示只能在此类中可见。仅仅只在类的内部可见。
 *          2.protected 可见性在private的基础上 + 子类可见
 *          3.internal 首先需要一点，对类是可见的，然后模块内都是可见的。
 *          4.public 首先需要一点，对类是可见的，然后其他位置可见。
 * */

private fun tell() {

}

internal val name:String
    get() = "he"

public interface Kt {
    fun unicode()
}

/**
 * 非顶层声明
 */
open class Outer {
    private val a = "a"
    internal val b = "b"
    protected open val c = "c"
    val d = "d"
    protected class Nested {
        public val e = "e"
    }
}


class Subclass:Outer() {
    /**
     * b、c、d、Nested、e对于SubClass可见
     */
    val s = Nested().e
    override val c: String
        get() = super.c
}

class UnrelatedClass(val o:Outer) {
    fun mu() {
//        o.a、o.c 不可见
        //o.b、o.d 可见
        //Nested、e 不可见
    }
}
`Activity 沉浸式状态栏`
https://juejin.im/post/6844903490402123789#heading-3