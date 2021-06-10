package com.lonbon.kotlin.lambda


fun main() {
    val x: (Boolean) -> String = { ifSuccess ->
        println("${ifSuccess}")
        "kotlin"
    }

    x(true)
}