package com.lonbon.kotlin;

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class Flow2<T> {

    private val flowA: Flow<Int> = flowOf(1,2,3)

    private val flowB: Flow<Int> = flowOf(1,2,3)

/*    fun test() {

        val stateFlow: MutableStateFlow<Int> = MutableStateFlow(0)

        GlobalScope.launch {
            stateFlow.flatMapConcat {
                if (it == 0) {
                    flowA.takeWhile {
                        stateFlow.value == 0
                    }
                } else {
                    flowB.takeWhile {
                        stateFlow.value == 1
                    }
                }
            }.collect {
                println(it)
            }
        }
    }*/

}
