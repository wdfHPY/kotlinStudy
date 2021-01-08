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
  - state(当前生命周期所处的状态)
  - `Event`(当前生命周期改变所对应的时间):当`Lifecycler`对象发生变化之后,那么就会产生相对应的`Event`时间.比如`Fragment`执行`onCreate()`方法的话,那么`Lifecycler`对象会发出`ON_CREATE`事件.

#### `Fragement`中`Lifecycler`

- ```kotlin
  public class Fragment implements ComponentCallbacks, OnCreateContextMenuListener, LifecycleOwner,
          ViewModelStoreOwner, HasDefaultViewModelProviderFactory, SavedStateRegistryOwner,
          ActivityResultCaller
  ```

- 通过`Fragment`的类名可以发现`Fragment`实现了`LifecyclerOwner`接口

  - `LifecyclerOwner`接口

  - ```kotlin
    /**
     * A class that has an Android lifecycle. These events can be used by custom components to
     * handle lifecycle changes without implementing any code inside the Activity or the Fragment.
     *
     * @see Lifecycle
     * @see ViewTreeLifecycleOwner
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public interface LifecycleOwner {
        /**
         * Returns the Lifecycle of the provider.
         *
         * @return The lifecycle of the provider.
         */
        @NonNull
        Lifecycle getLifecycle();
    }
    ```

  - `Fragment`中实现的`getLifecycle()`方法

    - ```kotlin
      public Lifecycle getLifecycle() {
          return mLifecycleRegistry;
      }
      ```

    - `mLifecycleRegistry`的类型为`LifecycleRegistry`, 而`LifecycleRegistry`是继承自`Lifecycle`

  - `Fragment`生命周期会由`FragmentManager`来进行管理.当执行相对应周期方法时,`Fragment`如何改变其中包含的`mLifecycleRegistry`对象的状态呢?

    - ```kotlin
      void performStart() {
          mChildFragmentManager.noteStateNotSaved();
          mChildFragmentManager.execPendingActions(true);
          mState = STARTED; 
          mCalled = false;
          onStart();// 执行生命周期方法.
          if (!mCalled) {
              throw new SuperNotCalledException("Fragment " + this
                                                + " did not call through to super.onStart()");
          }
          mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);//Registry来处
          //理Lifecycler的Event.传入的参数是Event.ON_START
          if (mView != null) {
              mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START);
          }
          mChildFragmentManager.dispatchStart();
      }
      ```

    - ```kotlin
      void performResume() { //Fragment Resume时执行
          mChildFragmentManager.noteStateNotSaved();
          mChildFragmentManager.execPendingActions(true);
          mState = RESUMED;
          mCalled = false;
          onResume(); //执行Resume生命周期方法
          if (!mCalled) {
              throw new SuperNotCalledException("Fragment " + this
                                                + " did not call through to super.onResume()");
          }
          //Registry来处理Lifecycler的Event.传入的参数是Event.ON_RESUME
          mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
          if (mView != null) {
              mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
          }
          mChildFragmentManager.dispatchResume();
      }
      ```

    - 随着`Fragment`不同走到不同的生命周期，除了暴露给我们的生命周期方法`onCreate/onStart/..../onDestroy`等，同时，`Fragment`内部的`Lifecycle`对象（就是`mLifecycleRegistry`）还将生命周期对应的事件作为参数传给了`handleLifecycleEvent()` 方法。

    - ![Lifecycle 在Fragment中的时序图](https://user-gold-cdn.xitu.io/2019/5/28/16afeb4f42be3b47?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

    - 当Fragment将生命周期对应的事件交给其内部的`Lifecycle`处理后，`Lifecycle`会通知`LifecyclerOberver`便可以执行相对应的状态方法.那么`Lifecycler`是如何通知`LifecyclerObserver`?

    -  ```kotlin
      mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
       ```

    - ```kotlin
      //上面处理LifecycleEvent 通知LifeObserver的方法.
      public void handleLifecycleEvent(@NonNull Lifecycle.Event event) {
          enforceMainThreadIfNeeded("handleLifecycleEvent");
          moveToState(event.getTargetState());
      }
      ```

    - 上面的函数的作用文档中描述为: 设置当前状态并通知观察者。

    - ```kotlin
      private void moveToState(State next) {//具体通知观察者的方法
          if (mState == next) {
              return;
          }
          mState = next;
          if (mHandlingEvent || mAddingObserverCounter != 0) {
              mNewEventOccurred = true;
              // we will figure out what to do on upper level.
              return;
          }
          mHandlingEvent = true;
          sync();
          mHandlingEvent = false;
      }
      ```



#### `LifecyclerRegistry`

- `LifecycleRegistry `本身就是一个成熟的 `Lifecycle`实现类，它被实例化在`Activity`和`Fragment`中使用，如果我们需要自定义`LifecycleOwner `并实现接口需要返回一个`Lifecycle`实例，完全可以直接在自定义`LifecycleOwner`中`new`一个`LifecycleRegistry`成员并返回它。