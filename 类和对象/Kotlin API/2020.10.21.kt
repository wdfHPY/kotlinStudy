//standard.kt run、also、apply、with、let

import kotlin.random.Random

class Request(val baseUrl: String) {
    fun doPost():String {
        println("Request doPost $baseUrl")
        return ""
    }
}

class Student(val stuNo: String) {
    var stuName: String = ""
    var stuAge: Int = 0
    override fun toString(): String {
        return stuNo + "\t" + stuName + "\t" +stuAge
    }

}

fun main() {
    val stu = Student("kotlin")
    println("one  " + stu.stuName)
    val str = stu.run {
        stuName = "name"
        stuAge = 10
        "kotlin"
    }
    println("two  " + str)
}

fun getRandomInt(): Int {
    return Random.nextInt(100).also { custom ->
        println("getRandomInt() generated value $custom")
    }
}

val i = getRandomInt()