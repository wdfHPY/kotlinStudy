### Androidx SparseArrayCompat
#### 属性
- `private int mSize;`存储对象数组的大小。
-  `private static final Object DELETED = new Object();`代表着已经删除的对象，remove的时候将DELETED放到相对应的mValues映射的地方。
-  `private boolean mGarbage = false;`数据结构中是否存在垃圾。
- `private int[] mKeys;`存储key的数组。
- `private Object[] mValues;`存储key相对应value的数组。

#### 方法
-  `private void gc()`: 清除已删除的映射关系
-  `public void put(int key, E value)`:构造int值和value之间的映射关系
```java
    public void put(int key, E value) {
        int i =  ContainerHelpers.binarySearch(mKeys, mSize, key);

        if (i >= 0) {
            mValues[i] = value;
        } else {
            i = ~i;

            if (i < mSize && mValues[i] == DELETED) {
                mKeys[i] = key;
                mValues[i] = value;
                return;
            }

            if (mGarbage && mSize >= mKeys.length) {
                gc();

                // Search again because indices may have changed.
                i = ~ ContainerHelpers.binarySearch(mKeys, mSize, key);
            }

            if (mSize >= mKeys.length) {
                int n =  ContainerHelpers.idealIntArraySize(mSize + 1);

                int[] nkeys = new int[n];
                Object[] nvalues = new Object[n];

                // Log.e("SparseArray", "grow " + mKeys.length + " to " + n);
                System.arraycopy(mKeys, 0, nkeys, 0, mKeys.length);
                System.arraycopy(mValues, 0, nvalues, 0, mValues.length);

                mKeys = nkeys;
                mValues = nvalues;
            }

            if (mSize - i != 0) {
                // Log.e("SparseArray", "move " + (mSize - i));
                System.arraycopy(mKeys, i, mKeys, i + 1, mSize - i);
                System.arraycopy(mValues, i, mValues, i + 1, mSize - i);
            }

            mKeys[i] = key;
            mValues[i] = value;
            mSize++;
        }
    }
```