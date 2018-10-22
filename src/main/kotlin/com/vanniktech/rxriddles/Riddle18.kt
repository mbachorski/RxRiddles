package com.vanniktech.rxriddles

import io.reactivex.Observable

object Riddle18 {
    /**
     * Return an Observable that mirrors either the [first] or [second] Observable
     * depending on whoever emits or terminates first.
     *
     * Use case: You have multiple sources and want to get the data from either one and then be
     * consistent and not switch between multiple sources.
     */
    fun solve(first: Observable<Int>, second: Observable<Int>): Observable<Int> =
//            Observable.amb(listOf(first, second))
            Observable.ambArray(first, second)
    // given two or more source Observables, emit all of the items from only the first of these
    // Observables to emit an item
}
