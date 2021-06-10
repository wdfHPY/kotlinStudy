1. `kotlin`源程序

   ```kotlin
   fun main() {
       val x: (Boolean) -> String = { ifSuccess ->
           println("${ifSuccess}")
           "kotlin"
       }
   
       x(true)
   }
   ```

   1. 反编译成`java`代码之后：

2. ```java
   @Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 2, d1 = {"\000\006\n\000\n\002\020\002\032\006\020\000\032\0020\001"}, d2 = {"main", ""})
   public final class LambdaKt {
     public static final void main() {
       Function1 x = LambdaKt$main$x$1.INSTANCE;
       x.invoke(Boolean.valueOf(true));
     }
     
     @Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 3, d1 = {"\000\016\n\000\n\002\020\016\n\000\n\002\020\013\n\000\020\000\032\0020\0012\006\020\002\032\0020\003H\n\006\002\b\004"}, d2 = {"<anonymous>", "", "ifSuccess", "", "invoke"})
     static final class LambdaKt$main$x$1 extends Lambda implements Function1<Boolean, String> {
       public static final LambdaKt$main$x$1 INSTANCE = new LambdaKt$main$x$1();
       
       @NotNull
       public final String invoke(boolean ifSuccess) {
         String str = String.valueOf(ifSuccess);
         boolean bool = false;
         System.out.println(str);
         return "kotlin";
       }
       
       LambdaKt$main$x$1() {
         super(1);
       }
     }
   }
   
   ```

3. 可以先从`main`函数开始查看:

   1. 可以清晰看到出`lambda`表达式被映射成为`Function1`类的对象。
   2. `Function`的类型为`LambdaKt$main$x$1`。该类是一个`static final class`并且继承`Function1`，`Function1`便代表着其存在参数的个数。`<Boolean, String>`表示该lambda表达式接收一个`Boolean`类型参数，返回值为`String`。`kotlin`中`lambda`表达式通过`Function`函数多态判断传入的参数是否正确。
   3. 真正的参数并不是通过创建类时传递到类中的，而是通过接口进行限制判断参数和返回值。在类中存在一个`INSTANCE`静态对象。外部使用`lambda`的时，并不是创建匿名内部类对象。而是通过匿名内部类中`INSTANCE`对象来访问。
   4. `lambda`表达式真正的函数体被放置在匿名类实现的`invoke`方法中了。所以`lambda`函数的执行本质上都是执行`invoke()`

