1. runblocking函数： 运行一个新的协程，并阻止当前线程，可中断，直到其完成。一般使用main功能和测试中，由kotlinx所提供。
2. run 方法：
   -   run：run方法的源码
    ```kotlin
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
    - run() 调用指定的功能块，并且this值作为它的receiver并返回其结果。run方法类名中存在类型T，而起参数block类型是T.()，说明这个传入的参数函数T对象中的方法。返回值是R，即返回R对象。R对象返回给this，调用run方法的对象作为返回值的R的receiver。使用T.run时传递的是this而下面的let传递的是不是this，而是相当于一个lambera表达式。使用调用者时可以使用it指代。

```kotlin
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

- T.run和T.let都是扩展的函数，区别就在于作用域内时指代不同。 T.run是一this指代，而T.let是it指代。

```kotlin

            /**
        * Calls the specified function [block] with `this` value as its argument and returns `this` value.
        *
        * For detailed usage information see the documentation for [scope functions](https://kotlinlang.org/docs/reference/scope-functions.html#also).
        */
        @kotlin.internal.InlineOnly
        @SinceKotlin("1.1")
        public inline fun <T> T.also(block: (T) -> Unit): T {
            contract {
                callsInPlace(block, InvocationKind.EXACTLY_ONCE)
            }
            block(this)
            return this
        }
```

- https://zhuanlan.zhihu.com/p/37085876