package com.vanniktech.rxriddles

import io.reactivex.Observable
import java.util.concurrent.TimeUnit

object Riddle36 {
    /**
     * Return an Observable that only emits items from [source] if there isn't another
     * emission before [milliseconds] has passed.
     *
     * Debounce: only emit an item from an Observable if a particular timespan has passed
     * without it emitting another item
     *
     * Use case: You want the user-input to trigger a search request for the entered text
     * but only when no changes have been made for a pre-determined time to avoid unnecessary requests.
     */
    fun solve(source: Observable<String>, milliseconds: Long): Observable<String> = source
            .debounce(milliseconds, TimeUnit.MILLISECONDS)
}
