### `Lifecycle`

#### `Lifecycle`定义

- 什么是`Lifecycle`
  - `Lifecycle`是框架,不仅仅是一个`Lifecycle`类.和它相关的类还有一些.
  - `Lifecycle`是用来`Android`组件(`Actvity`或者`Fragment`)生命周期内提供类的监听器来控制数据.
  - `MVVM` 架构中 `LiveData` 和 `ViewModel` 都是依赖于 `Lifecycle` 框架.

#### `Lifecycle` 简介和使用

#### `Lifecycle` 原理分析

- ![Lifecycle组件原理](https://user-gold-cdn.xitu.io/2019/5/28/16afeb4f42a9ed89?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

- `Lifecycle`框架中类
  - `LifecycleObserver` `Interface `(生命周期观察者):实现`LifecycleObserver`可以通过注解的方式便,可以通过被`LifecycleOwner`类的 `addObserver(LifecycleObserver o)` 方法注册,被注册后， `LifecycleObserver` 便可以观察到  `LifecycleOwner` 的**生命周期事件**。
    - `@OnLifecycleEvent**(Lifecycle.Event.ON_CREATE)`
  - `LifecycleOwner` `Interface`(生命周期持有者): 实现`LifecycleOwner `的接口的会持有`lifecycle`生命周期.此`lifecycle` 对象的改变会被注册的 `lifecyleObserver` 观察方法出发并执行与其相对应的事件.
  - `Lifecycle` 生命周期:`LifecycleOwner`内持有的对象便是`Lifecycle`.LifecycleOwner可以通过`getLifecycle()`方法来获取`Lifecyle`对象.`LifecycleObserver` 能够观察到生命周期产生变化也是由于`Lifecycle`的state产生变化.

