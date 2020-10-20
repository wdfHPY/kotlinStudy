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
   - **本质上：第一种区别就在于传递方向不同**

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
    - 从两个函数源码的参数可以清楚显示第一种区别：


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