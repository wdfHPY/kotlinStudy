#### kodein DI(用于kotlin 依赖注入库)

- `kodein DI`通过延迟加载的方式来进行依赖注入

- 声明依赖:初始化`Dependency Inject`容器.

  ```kotlin
  var di = DI {
      /*Bindings*/
  }
  //声明一个名称为di 的依赖项容器
  ```

- `Bindings:绑定:声明依赖项`.

  - 依赖容器中声明依赖项.

  - 依赖项都是以bind<TYPE>() with来开头

  - ```kotlin
    bind<ZoneInpatientRepository>() with singleton {
        ZoneInpatientRepositoryImpl(context.applicationContext)
    }
    ```

  - 绑定依赖项的方式存在四种

    - `Tagged bindings`

    - `Provider binding`

    - `Singleton binding`:会将一个类型绑定到该类型的实例.实例会在第一次使用时使用单例函数懒惰地创建.函数不接受任何参数并且返回绑定类型的对象.

      - ```kotlin
        bind<ZoneInpatientRepository>() with singleton {
            ZoneInpatientRepositoryImpl(context.applicationContext)
        }
        //将ZoneInpationRepository绑定单例懒惰函数返回的函数ZoneInpatientRepositoryImpl()..
        ```

      - 

    - 

