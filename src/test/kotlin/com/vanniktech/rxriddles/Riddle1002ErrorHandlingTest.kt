package com.vanniktech.rxriddles

import com.vanniktech.rxriddles.solutions.Riddle12Solution
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

/** Solution [Riddle12Solution] */
class Riddle1002ErrorHandlingTest {

    private var subscribeCounter = AtomicInteger(0)

    private val observable = Observable.create<Int> {
        subscribeCounter.getAndIncrement()
        it.onNext(1)
        it.onError(RuntimeException())
        it.onNext(3)
    }

    @Before
    fun setup() {
        subscribeCounter = AtomicInteger(0)
    }

    @Test
    fun `items after on error will be skipped`() {
        observable
                .onErrorResumeNext(Observable.just(2))
                .test()
                .assertResult(1, 2)
    }

    @Test
    fun `onComplete will be called and last item won't pass the stream`() {
        observable
                .onErrorResumeNext(Observable.just(2))
                .doOnError {
                    System.out.println("doOnError: $it")
                }
                .subscribe(
                        { System.out.println("onNext: $it") },
                        { System.out.println("onError: $it") },
                        { System.out.println("onComplete!") }
                )
    }

    @Test
    fun `retry not working with onErrorResumeNext`() {
        observable
                .onErrorResumeNext(Observable.just(2)) // This calls on complete and retry is not working!
                .retry(2)
                .subscribe(
                        { System.out.println("onNext: $it") },
                        { System.out.println("onError: $it") },
                        { System.out.println("onComplete!") }
                )
        assertThat(subscribeCounter.get()).isEqualTo(1) // <<< 1 = no retries !
    }

    @Test
    fun `onErrorResumeNext calling onComplete after retries`() {
        observable
                .retry(2) // without "times" it retries forever
                .onErrorResumeNext(Observable.just(2)) // This calls on complete and retry is not working!
                .subscribe(
                        { System.out.println("onNext: $it") },
                        { System.out.println("onError: $it") },
                        { System.out.println("onComplete!") }
                )
        assertThat(subscribeCounter.get()).isEqualTo(3)
    }

    @Test
    fun `doOnError no influence on retry`() {
        observable
                .doOnError { System.out.println("doOnError: $it") } // doesn't influence retry, can be earlier
                .retry(2) // without "times" it retries forever
                .subscribe(
                        { System.out.println("onNext: $it") },
                        { System.out.println("onError: $it") },
                        { System.out.println("onComplete!") }
                )
        assertThat(subscribeCounter.get()).isEqualTo(3)
    }

    @Test
    fun `handle simple error with switchmap`() {
        Observable
                .just(true, false, true)
                .switchMap { returnedStatus ->
                    if (returnedStatus) {
                        Observable.just("OK")
                    } else {
                        Observable.just("ERROR !")
                    }
                }
                .doOnNext { System.out.println("doOnNext: $it") }
                .test()
    }

    @Test
    fun `handle simple error with flatmap same as switchmap`() {
        Observable
                .just(true, false, true)
                .flatMap { returnedStatus ->
                    if (returnedStatus) {
                        Observable.just("OK")
                    } else {
                        Observable.just("ERROR !")
                    }
                }
                .doOnNext { System.out.println("doOnNext: $it") }
                .test()
    }
}
