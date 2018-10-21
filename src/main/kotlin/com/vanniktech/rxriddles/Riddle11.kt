package com.vanniktech.rxriddles

import io.reactivex.Observable
import java.util.concurrent.TimeUnit

object Riddle11 {
    /**
     * Let the first emission of the [source] within a time window of 300ms travel downstream but don't emit any other events until the next time window.
     *
     * Use case: Handle the click of a button right away but prevent double clicking by not handling multiple click events within a given time window.
     */
    fun solve(source: Observable<Unit>): Observable<Unit> = source.throttleFirst(300, TimeUnit.MILLISECONDS)

    //Throttle: the original function be called at most once per specified period.
    //
    //Debounce: the original function be called after the caller stops calling the decorated function after a specified period.
}
