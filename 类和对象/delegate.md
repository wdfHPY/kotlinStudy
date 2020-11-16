#### 委托/代理
```kotlin
                package com.example.android.roomwordssample

            import kotlin.reflect.KProperty

            /*
            *   kotlin Delegate
            * */

            //委托中约束角色,主要提供业务的定义
            interface delegateConstains{
                fun delegate()
                fun workdelegate()
            }

            //委托对象角色 --> 委托对象角色本质上只是一个接受业务的对象.委托对象
            //将业务委托给业务真正执行的人.也就是被委托对象角色

            //kotlin 中原生即支持代理/委托. 使用by 关键字来进行委托.
            //by 后面的关键字便是委托对象.被委托对象通过构造函数的方式进行依赖注入到委托对象当中.
            class delegateRole(val role: delegatedRole): delegateConstains by role {
                //当然,通过在委托类覆盖方法的话 在调用此业务逻辑的话,此时调用的就是委托对象类中的方法了
                override fun delegate() {
                    println("This is a delegate function")
                }
            }


            //使用by 关键字 Java 代码在委托类定义一个被委托对象的实例.执行业务逻辑时 本质上还是调用被委托对象的相对应的业务方法.

            //被委托对象角色 --> 真正实现执行业务类

            class delegatedRole() :delegateConstains {
                override fun delegate() {
                    println("delegatedRole delegate")
                }

                override fun workdelegate() {
                    println("delegatedRole workdelegate")
                }

            }

            //委托属性
            class delegateField {
                var str:String by fieldDelegated()
            }

            class fieldDelegated{
                operator fun getValue(delegateField: delegateField, property: KProperty<*>): String {
                    return "$delegateField, thanks for delegate ${property.name}"
                }

                operator fun setValue(delegateField: delegateField, property: KProperty<*>, s: String) {
                    println("value is ready signed to ${property.name} in $delegateField")
                }
            }

            fun main() {
                val delegatedRole = delegatedRole()
                val delegateRole = delegateRole(delegatedRole) //这里可以看到将被委托对象作为参数传入委托对象当中去得了
                //虽然是委托对象调用这些方法,但是背后真正完成任务的对象是被委托对象.
                delegateRole.workdelegate()
                delegateRole.delegate()
                //属性初始化
                println(delegateField().str)
                delegateField().str = "myName"
            }
```


```kotlin
                    package com.example.android.roomwordssample
                    import kotlin.properties.Delegates
                    import kotlin.properties.ReadOnlyProperty
                    import kotlin.properties.ReadWriteProperty
                    import kotlin.reflect.KProperty

                    /*
                    *   kotlin Delegate
                    * */

                    //委托中约束角色,主要提供业务的定义
                    interface delegateConstains{
                        fun delegate()
                        fun workdelegate()
                    }

                    //委托对象角色 --> 委托对象角色本质上只是一个接受业务的对象.委托对象
                    //将业务委托给业务真正执行的人.也就是被委托对象角色

                    //kotlin 中原生即支持代理/委托. 使用by 关键字来进行委托.
                    //by 后面的关键字便是委托对象.被委托对象通过构造函数的方式进行依赖注入到委托对象当中. ---> 被依赖对象要和依赖对象之间产生依赖关系.这里是通过高抛函数进行注入
                    class delegateRole(val role: delegatedRole): delegateConstains by role {
                        //当然,通过在委托类覆盖方法的话 在调用此业务逻辑的话,此时调用的就是委托对象类中的方法了
                        override fun delegate() {
                            println("This is a delegate function")
                        }
                    }


                    //使用by 关键字 Java 代码在委托类定义一个被委托对象的实例.执行业务逻辑时 本质上还是调用被委托对象的相对应的业务方法.

                    //被委托对象角色 --> 真正实现执行业务类

                    class delegatedRole() :delegateConstains {
                        override fun delegate() {
                            println("delegatedRole delegate")
                        }

                        override fun workdelegate() {
                            println("delegatedRole workdelegate")
                        }

                    }

                    //委托属性.通过上面的委托可以知道:实现代理模式三个角色必不可少.约束类
                    //委托对象和被委托对象.而针对属性,约束类其中所做的约束为setter和getter方法.所以说,原先约束的interface类不存在了.
                    //委托对象也很好确定.属性所在的类即为委托类.所以属性的委托那么就去重写一个被委托类即可.
                    class delegateField {
                        var str:String by fieldDelegated()//委托属性的类型是var 可读写,那么实现它就必须要getValue和setValue都是需要的
                        /**
                        * 可以看到, 针对val 仅读的变量来说的话,被委托类仅需实现一个的getValue()即可
                        * 针对var 类型的属性,被委托类就需要实现两个方法.setValue和getValue即可.
                        */
                        val str2:String by readOnlyPropertyClass()

                        var int:Int by readWritePropertyClass()

                    }

                    class fieldDelegated{
                        //getValue 参数: 第一参数是读出的对象.在这里,读出的对象即委托类,第二个参数为属性自身的描述.表示为哪一个属性使用委托.
                        operator fun getValue(delegateField: delegateField, property: KProperty<*>): String {
                            return "$delegateField, thanks for delegate ${property.name}"
                        }

                        operator fun setValue(delegateField: delegateField, property: KProperty<*>, s: String) {
                            println("value is ready signed to ${property.name} in $delegateField")
                        }
                    }

                    //kotlin 标准库当 可以使用接口来完成getValue和setValue
                    // Kotlin 标准库中声明了2个含所需 operator方法的 ReadOnlyProperty / ReadWriteProperty 接口
                    //ReadOnlyProperty --> val
                    //ReadWriteProperty --> var
                    /**
                    * public interface ReadOnlyProperty<in R, out T>
                    *  可以看到 ReadOnlyProperty 第一个参数可以为其超类型.第二个参数可以为其子类型
                    */
                    class readOnlyPropertyClass():ReadOnlyProperty<Any, String> {
                        override fun getValue(thisRef: Any, property: KProperty<*>): String {
                            return "read value use readOnlyProperty"
                        }
                    }

                    /**
                    *  针对类型val
                    */
                    class readWritePropertyClass :ReadWriteProperty<Any, Int> {
                        override fun getValue(thisRef: Any, property: KProperty<*>): Int {
                            return 1
                        }

                        override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
                            println("ready to setValue")
                        }

                    }

                    /**
                    * 除了上面针对委托属性的两种方式.
                    * kotlin 函数库还提供了其他支持的委托属性的方法
                    * 1.延迟属性(lazy properties)
                    * 2.可观察属性（observable）
                    */

                    /**
                    * 延迟属性
                    * ｌａｚｙ是接受一个lambda表达式的并且返回Lazy<T> 的函数.
                    * lazy函数返回的关键词可以作为实现延迟属性的委托.第一次调用 get() 会执行已传递给 lazy() 的 lambda
                    * 表达式并记录结果， 后续调用 get() 只是返回记录的结果。第一次调用时会执行lambda 表达式的代码块.
                    *
                    * lazy函数的话还可以带上参数.参数设置是针对于第一次执行线程是否安全.lazy代码块存在两个参数.第一个参数
                    * 是关于LazyThreadSafetyMode\第二个参数即传入的lambda表达式了.
                    *
                    * lazyThreadSafeMode存在三种值:
                    *      1.SYNCHRONIZED 加上线程的同步锁来完成初始化.
                    *      2.PUBLICATION 可以并发的进行执行lazy 代码块.仅仅只返回并发线程中第一个初始化代码块的值.
                    *      3.NONE 不做任何的处理.
                    *   默认参数是第一种参数SYNCHRONIZED.
                    */
                    class delegateClass3 {
                        val name:String by lazy(LazyThreadSafetyMode.NONE){
                            println("This is the first call get Function")
                            "kotlin"
                        }
                    }

                    /**
                    * 可观察属性observable
                    *  如果是需要观察属性的变化,那么就可以委托给可观察的属性.Delegates.observable
                    *  Delegates.observable 接受两个参数.第一个参数为属性的初始值,第二个参数为属性产生变化导致的回调.
                    *  observable 回调存在三个参数,第一个参数是针对被赋值的,第二个参数是oldValue,第三个参数为newValue
                    */

                    class deletgateClass4 {
                        var age:Int by Delegates.observable(24) {
                            property, oldValue, newValue ->
                            println("property age is $property, oldValue is $oldValue, newValue is $newValue")
                        }
                    }

                    /**
                    * Delegates.vetoable和上面的可观察属性差不多.但是观察属性不能决定新值是否生效.vetoable()即可来判断
                    * 是否满足条件,新值是否生效.
                    */
                    class delegateClass5 {
                        var age: Int by Delegates.vetoable(14) { property, oldValue, newValue ->
                            println("property age is $age, oldValue is $oldValue, newValue is $newValue")
                            if (oldValue > newValue) true else false
                        }
                    }

                    /**
                    * 当Map存储属性的值时,此时可以通过Map自身的实例来进行属性委托
                    */
                    class delegateClass6(val map:Map<String,Any>) {
                        val name by map
                        val age by map
                    }



                    fun main() {
                        val delegatedRole = delegatedRole()
                        val delegateRole = delegateRole(delegatedRole) //这里可以看到将被委托对象作为参数传入委托对象当中去得了
                        //虽然是委托对象调用这些方法,但是背后真正完成任务的对象是被委托对象.
                        delegateRole.workdelegate()
                        delegateRole.delegate()
                        //属性初始化
                        println(delegateField().str)
                        delegateField().str = "myName"
                        println("--------------------")
                        val de = delegateField()
                        println(de.str2)
                        de.int = 14
                        println(de.int)
                        println(" - - - - - - - - -")
                        val x = delegateClass3()//
                        println(x.name)
                        println(x.name)
                        println(x.name)
                        println(x.name)
                        println("------------------")
                        val y = deletgateClass4()
                        y.age = 14
                        y.age = 45

                        println("------------------")
                        val dele = delegateClass5()
                        dele.age = 15
                        dele.age = 1

                        println("- - - - - - - - - - -")
                        val deleg = delegateClass6(mapOf(
                                "name" to "kotlin",
                                "age" to  14
                        ))
                        println(deleg.age)
                        println(deleg.name)
                    }
```