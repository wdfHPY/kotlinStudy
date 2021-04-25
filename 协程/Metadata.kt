package com.lonbon.kotlin

import kotlin.reflect.KProperty

object Metadata {
    /**
     * members Desc: 包括所有的属性和方法的属性。当然也是包括父类的属性。但是member中是不包括
     * 构造函数的。
     */
    fun <A: Any> toMap(a: A): Map<String, Any?> {
        return a::class.members.map { method ->
            val p = method as KProperty
            p.name to p.call(a)
        }.toMap()
    }
}