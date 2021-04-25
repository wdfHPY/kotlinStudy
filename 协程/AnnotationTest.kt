package com.lonbon.kotlin

class AnnotationTest {

}

@Annotation(value = "Test")
fun main() {
    val list = mutableListOf("1", "2", "3", "4", "5")
    list.removeAt(3)
}