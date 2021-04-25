### kotlin中属性

#### kotlin 中属性的定义

1. kotlin中属性完整定义为： 

   - ```kotlin
     val/var fieldName:  <propertyType> [<initializer>]
     		[get() = ...]
     		[set() = ...]
     ```

   - `val`和`var`关键字都是可以用来区修饰属性的。但是`val`和 `var`之间是存在区别的，`var`声明为可变的,而 `val `关键字声明的是只读的。

   - 其中 `属性初始化器`和`setter`以及`getter`都是可选的。

   - `propertyType`可以通过`setter`和`属性初始化器`作出类型推断出来。

2. ⼀个只读属性的语法和⼀个可变的属性的语法有两⽅⾯的不同：

   - 只读属性的⽤ `val` 开始代替 `var`
   - 只读属 性不允许 `setter`

3. ```kotlin
   val req = 1 // 使用初始化器来初始化属性,默认不显示 getter，但是req 属性是 提供getter方法的。不过不需要进行定义其他的内容。Decompaile 成Java代码可以清楚看到是提供getter的
   ```

4. ```kotlin
   val req2 get() = run {//通过 getter来声明一个属性。当访问该属性时都会调⽤ getter 方法
       println("This is the getter")
       "string"
   }
   //上面即自定义了一个getter来 req2 赋值。通过反编译成Java代码可以秦楚看到println 被放到属性的 getter方法当中去了而已的。
   ```

5. ```kotlin
   var stringRepresentation: String
       get() = this.toString()
       set(value) {
       setDataFromString(value) // 解析字符串并赋值给其他属性
       }
   ```

6. 需要注意的是：**针对属性的setter和getter 方法都是针对属性而言的而不是变量**。访问属性的值和设置属性的值便会调用属性的` setter` 和 `getter` 方法。`getter` 和`setter` 方法都是通过 操作符`. `来进行访问。包括之后幕后变量和幕后属性都是这样，不能认为定义在顶层的变量都存在这样的特性。