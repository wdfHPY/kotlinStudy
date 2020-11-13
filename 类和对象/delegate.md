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