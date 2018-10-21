package com.vanniktech.rxriddles

import io.reactivex.Observable
import org.junit.Test


class Riddle1001FlatmapTest {
    @Test
    @Throws(Exception::class)
    fun `Flat mapping observable of observables into one observable with all elements`() {
        Observable
                .just(Observable.just(1, 2), Observable.just(3, 4), Observable.just(5, 6))
                .flatMap {
                    it
                }
                .doOnNext { it -> System.out.println("doOnNext: $it") }
                .subscribe()
    }

    @Test
    @Throws(Exception::class)
    fun `Flat mapping as calling other function and merging into one observable with all elements`() {
        val observable = Observable.just("a", "b")

        Observable
                .just(1, 2, 3)
                // .doOnNext { it -> System.out.println("doOnNext: $it") }
                .flatMap {
                    observable
                }
                .doOnNext { it -> System.out.println("doOnNext: $it") }
                .subscribe()
    }

    @Test
    @Throws(Exception::class)
    fun `Flat mapping as calling other function and merging into one observable with all elements combined`() {
        val otherItems = Observable.just("a", "b")

        Observable
                .just(1, 2, 3)
                // .doOnNext { it -> System.out.println("doOnNext: $it") }
                .flatMap { originalItem ->
                    otherItems.map { originalItem.toString() + it }
                }
                .doOnNext { it -> System.out.println("doOnNext: $it") }
                .subscribe()
    }
}