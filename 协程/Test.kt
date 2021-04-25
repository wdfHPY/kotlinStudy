package com.lonbon.kotlin;
 class Test {
     var age = 18
         set(value) {
             if (value > 0) field = value
         }
}
