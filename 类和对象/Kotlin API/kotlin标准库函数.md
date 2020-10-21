##### kotlin startard.kt标准库函数详解
###### run、with、let、also和apply
1. 分析run、with、let、also和apply不同点的话，那么就需要从源码的角度上才能够分析出各个函数之间的使用方式和区别。
- kotlin 官网中Scope Function的说明：
- The Kotlin standard library contains several functions whose sole purpose is to execute a block of code within the context of an object. When you call such a function on an object with a lambda expression provided, it forms a temporary scope. In this scope, you can access the object without its name. Such functions are called scope functions. There are five of them: let, run, with, apply, and also.
- kotlin 标准库包含几个函数，它们的唯一目的是在**对象的上下文**中执行代码块。当使用 lambda 表达式对一个对象调用这样的函数时，它会形成**一个临时范围(Scope Function)**。在此作用域中，可以访问没有名称的对象。这样的函数称为作用域函数。它们有五个: **let、 run、 with、 apply 和 also。**
- Basically, these functions do the same: execute a block of code on an object. What's different is how this object becomes available inside the block and what is the result of the whole expression.
- 基本上，这些函数的作用相同: 在一个**对象（上下文对象）**上执行一段代码。不同之处在于这个对象如何在块中表示，以及整个表达式的结果是什么。
- Scope Function**都是在一个对象上执行一段代码，依赖于对象的上下文**，不同之处就是调用对象在代码块中如何表示（this、it来指代调用对象）、表达式的结果（直接返回this,还是返回执行过代码块之后的对象）

###### 通过context object 来区分Scope Function
1. 区分作用域函数一个很重要特征可以通过Context Object（上下文对象）来区分用法。由于Scope Function 均是在一个对象的基础上执行一段代码。所以上下文对象和代码块之间的关系就可以区分使用方法。
   - 第一种：上下文对象作为代码块的接收器（receiver）。接收器可理解为，代码块传递给上下文对象了，上下文对象便作为代码块的接收器。
   - 第二种：上下文对象作为代码块的参数。相当与将上下文对象传递给代码块了，也可以这么说，代码块作为上下文对象的接收器。
   - **本质上：第一种区别就在代码块访问上下文对象的方式**

2. `T.let`、`T.run`
     ```kotlin
                //  T.run
                    /**
                * Calls the specified function [block] with `this` value as its receiver and returns its result.
                *
                * For detailed usage information see the documentation for [scope functions](https://kotlinlang.org/docs/reference/scope-functions.html#run).
                */
                @kotlin.internal.InlineOnly
                public inline fun <T, R> T.run(block: T.() -> R): R {
                    contract {
                        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
                    }
                    return block()
                }
    ```

    ```kotlin
                // T.let
                    /**
                * Calls the specified function [block] with `this` value as its argument and returns its result.
                *
                * For detailed usage information see the documentation for [scope functions](https://kotlinlang.org/docs/reference/scope-functions.html#let).
                */
                @kotlin.internal.InlineOnly
                public inline fun <T, R> T.let(block: (T) -> R): R {
                    contract {
                        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
                    }
                    return block(this)
                }
            
    ```
    - 从两个函数源码的参数可以清楚显示第一种区别：T.run的函数参数为：`block: T.() -> R`，T.let的函数参数为`block: (T) -> R`。可以清楚的看到，代码块作为一个`lambda`表达式参数传递进来。T.run的lambda表达式参数是作为上下文参数的属性来使用，T.let的lambda函数的参数是上下文对象。这样上下文传参方向就带来在`T.run`和`T.let`使用上下文对象的不同。
  - 
        ```kotlin
             class Request(val baseUrl: String) {
                fun doPost() {
                    println("Request doPost $baseUrl")
                }
            }

            fun main() {
                val req = Request("http://localhost:8080")
                req.run {
                    this.doPost()
                }
                req.let {
                    it.doPost()
                }
            }
        ```
-  Inside the lambda of a scope function, the context object is available by a short reference instead of its actual name.Each scope function uses one of two ways to access the context object: as a lambda receiver (this) or as a lambda argument (it). Both provide the same capabilities, so we'll describe the pros and cons of each for different cases and provide recommendations on their use.
-  在作用域函数的lambda中，可以通过**短引用**而不是其实际名称来使用上下文对象。每个作用域函数使用两种访问上下文对象的方式之一：作为lambda接收（this）或作为lambda自变量（it）。两者都提供相同的功能，因此我们将描述每种情况在每种情况下的利弊，并提供使用建议。
-  main方法中分别在req对象的基础上调用了run和let。在代码块中，可以使用短引用来代替上下文对象，如果上下文对象作为代码块的接收器，那么使用`this `短引用来代替上下文对象。而`this`短引用可以省略。如果上下文对象作为代码块的参数， 那么使用`it`来代替上下文对象。


##### this 
- 官方Scope Function说明
  - run, with, and apply refer to the context object as a lambda receiver - by keyword this. Hence, in their lambdas, the object is available as it would be in ordinary class functions. In most cases, you can omit this when accessing the members of the receiver object, making the code shorter. On the other hand, if this is omitted, it can be hard to distinguish between the receiver members and external objects or functions. So, having the context object as a receiver (this) is recommended for lambdas that mainly operate on the object members: call its functions or assign properties.
  - `run`、`with`、`apply` 函数都是通过lambda接收器运行，使用并应用该上下文对象-通过关键字this。因此，在它们的lambda中，该对象是可用的，就像在普通类函数中一样。在大多数情况下，可以在访问接收器对象的成员时省略此操作，从而使代码更短。另一方面，如果省略，则很难区分接收器构件和外部对象或功能。因此，**对于主要对对象成员进行操作的lambda，建议将上下文对象作为接收器**：调用其函数或分配属性。
    ```kotlin
                class Student(val stuNo: String) {
                    var stuName: String = ""
                    var stuAge: Int = 0
                    override fun toString(): String {
                        return stuNo + "\t" + stuName + "\t" +stuAge
                    }
                }

                fun main() {
                    Student("kotlin").apply {
                        stuName = "name"
                        stuAge = 10
                    }.run { println(toString()) }
                }
    ```

##### it
- In turn, let and also have the context object as a lambda argument. If the argument name is not specified, the object is accessed by the implicit default name it. it is shorter than this and expressions with it are usually easier for reading. However, when calling the object functions or properties you don't have the object available implicitly like this. Hence, having the context object as it is better when the object is mostly used as an argument in function calls. it is also better if you use multiple variables in the code block.Additionally, when you pass the context object as an argument, you can provide a custom name for the context object inside the scope.
- 反过来，`let`和`also`将上下文对象作为lambda参数。如果未指定参数名称，则使用**隐式默认名称**来访问该对象。它比这短，并且带有它的表达式通常更易于阅读。但是，在调用对象函数或属性时，您没有像这样隐式可用的对象。因此，**当在函数调用中将该对象主要用作自变量**时，最好使用上下文对象作为参数。如果在代码块中使用多个变量也更好。此外，当您将上下文对象作为参数传递时，可以在范围内为上下文对象提供自定义名称。（即不使用隐式默认参数时，可以提供自定义名称）

```kotlin
            fun getRandomInt(): Int {
                return Random.nextInt(100).also { custom ->
                    println("getRandomInt() generated value $custom")
                }
            }

            val i = getRandomInt()
```


```kotlin
        /**
        * Calls the specified function [block] and returns its result.
        *
        * For detailed usage information see the documentation for [scope functions](https://kotlinlang.org/docs/reference/scope-functions.html#run).
        */
        @kotlin.internal.InlineOnly
        public inline fun <R> run(block: () -> R): R {
            contract {
                callsInPlace(block, InvocationKind.EXACTLY_ONCE)
            }
            return block()
        }
```