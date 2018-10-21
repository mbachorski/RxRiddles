package com.vanniktech.rxriddles

import io.reactivex.Observable

object Riddle4 {
    /**
     * Implement a toggle mechanism. Initially we want to return false.
     * Every time [source] emits, we want to negate the previous value.
     *
     * Use case: Some button that can toggle two states. For instance a switch between White & Dark theme.
     */
    fun solve(source: Observable<Unit>): Observable<Boolean> {
//        In Kotlin, there is a convention that if the last parameter of a function accepts a function, a lambda expression that is passed as the corresponding argument can be placed outside the parentheses.
//        If the lambda is the only argument to that call, the parentheses can be omitted entirely.
        return source.scan(false) { initialValue, _ -> !initialValue }
//           return source.scan(false, BiFunction { t1, t2 -> !t1 })
    }
}
