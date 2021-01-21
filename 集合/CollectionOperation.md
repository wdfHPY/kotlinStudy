### 集合的操作

#### 集合映射操作

1. 集合映射涉及到的函数有`map()`和`mapIndex()`,`mapNotNull()`,`mapIndexedNotNull`,`mapkeys()`,`mapvalues()`

2. ```kotlin
       val setCol = setOf("str1", "str2", "str3")
       //map 函数一般是将一个Collection 映射转化为另外一个Collection.集合映射变换并不会改变Collection的先后顺序.map 函数接受一个lambda 表达式作为参数.
       val mappedCollection = setCol.map {
           it.plus(" + str")
       }
       //mapIndexed映射函数进行函数映射时一般会涉及到index.lambda表达式参数index即表示index
       val mappedIndexCollection = setCol.mapIndexed { index, s ->
           s.plus(" + $index")
       }
       //集合映射时的空值过滤
       val notnullMappedCollection = setCol.mapNotNull {
           if(it == "str1") null else it.plus(" + not str1")
       }
       //集合index映射非空集合过滤
       val notnullIndexMappedCollection = setCol.mapIndexedNotNull { index, s ->  }
   
       //当然,针对Map类型的集合可以分别针对键和值进行映射
   
       val hashMap = mapOf("st1" to 1, "str2" to 2, "str3" to 3)
   
       //使用mapKeys可以针对键的集合来作为集合映射
       println(hashMap.mapKeys {
           it.key.plus(" + new key")
       })
   
       //使用mapValues可以针对值的集合来作为映射
       println(hashMap.mapValues {
           it.value.plus(2)
       })
   
       mappedCollection.forEach {
           println(it)
       }
   
       mappedIndexCollection.forEach {
           println(it)
       }
   
       notnullMappedCollection.forEach { //空值被过滤
           println(it)
       }
   ```

3. 集合的合并可以简单的理解为:将一个集合转换为另外的一个集合.

#### 集合的双路合并(zip)

1. 双路合并在于将两个集合中位置相同的元素组合成一个的集合.该集合的元素是由`Pair`元素.

2. `kotlin`函数库当中,是通过`zip()`函数扩展函数完成

3. 集合的双路拆分的函数是通过`unzip()`函数的扩展函数来完成

4. ```kotlin
       val list1 = listOf(1, 2, 3, 4)
       val list2 = listOf("str1", "str2", "str3", "str4")
       println(list1 zip list2) //直接将两个列表进行了双路的合并
       //当双路的集合长度不相同时,那么此时即以短的列表来进行双路的合并
   
       val list3 = listOf(1, 2, 3, 4, 5, 6, 7)
       val list4 = listOf("str1", "str2", "str3", "str4")
       println(list3 zip list4) //可以看到仍然以短的Collection来作为集合个数来作为Pair Collection的个数
   
       //zip 函数还可以携带一个lambda 表达式. 该lambda表达式存在两个参数 接受者元素的和参数元素
       val list5 = listOf(1, 2, 3, 4, 5, 6, 7)
       val list6 = listOf("str1", "str2", "str3", "str4")
       println(list5.zip(list6) { one, two ->
           println("$one $two")
       })
   
       val unzipList = listOf("str" to "1", "str2" to "2", "str3" to "3", "str4" to "4").unzip()
       println(unzipList.first)
       println(unzipList.second)
   ```

#### 集合的关联

1. 关联转换允许元素和其关联的某些值构建出`Map`.不同关联函数可以关联出`Map`的键或值.

2. 集合的关联操作在于和集合相关的`Map`对象.`kotlin`中关联的函数主要有`associateWith()`,`associateBy()`,`associate()`

3. ```kotlin
       /**
        * associateWith() 会通过键来生成值
        * associateWith() 函数会将接受者作为键,后面的lambda表达式来产生值value.但是作为键值时, 键值可能会产生重复的.
        * associateWith() 函数作为键值时,会自动进行去重.
        */
       val list = listOf("str1", "str2", "str2").associateWith {
           it.length
       }
       println(list)
   
       /**
        * associateBy() 函数会将接受者作为 Map作为值.后面的lambda表达式来产生键值.生成键值.同样的道理.associateBy通过
        * 值来生成键.所以对于值也只能保留最后一个.
        *
        * 对于生成键值的associateBy()来说,lambda 表达式对于不同的值会生成不同的键.如果不相同的值还是会生成相同的键的话,那么
        * 只会取最后一个
        */
       val list2 = listOf("str2", "str2").associateBy {
           it.length
       }
       println(list2)
   
       /**
        * 还存在一种关联关系,无论是键还是值都是使用接受者来使用.
        * 通过关联函数associate() 函数可以生成一个Map.而此Map的键值和value值都是使用函数来生成的.
        * associate() 函数的会临时生成一个Pair来创建一个Map. Pair的first值作为键.Pair的second值作为值
        */
       val list3 = listOf("str3", "str4", "str5", "s", "s").associate {
           it to it.length
       }
       println(list3)
   ```

#### 集合的打平

1. 集合的打平:将接受者所有集合全部元素单个列表的输出.

2. `kotlin`中使用函数`flatten()`

3. ```kotlin
   fun main() {
       val numberSets = listOf(setOf(1, 2, 3), setOf(4, 5, 6), setOf(1, 2)) //可以看到,numberSets集合中存在三个子集合.
       println(numberSets) //直接输出集合的话, 每一个集合,都会这样输出 [[1, 2, 3], [4, 5, 6], [1, 2]] --->可以看到,不是单个列名
       println(numberSets.flatten()) //集合中所有集合全部元素的单个列表 [1, 2, 3, 4, 5, 6, 1, 2]
   }
   ```



#### 集合的字符串表示

