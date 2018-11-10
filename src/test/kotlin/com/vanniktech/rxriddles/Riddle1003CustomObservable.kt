package com.vanniktech.rxriddles

import com.vanniktech.rxriddles.solutions.Riddle12Solution
import com.vanniktech.rxriddles.tools.RxRule
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/** Solution [Riddle12Solution] */
class Riddle1003CustomObservable {
    @get:Rule
    val rxRule = RxRule()

    @Test
    fun `Simple observable creation`() {
        val o = Observable.create<Int> { }
        assertThat(o.test().hasSubscription())
    }

    @Test
    fun `Simple observable creation with single Int pushed to the stream`() {
        val o = Observable.create<Int> { emitter ->
            emitter.onNext(1)
            emitter.onComplete()
        }

        o.test()
                .assertSubscribed()
                .assertValues(1)
                .assertComplete()
                .assertNoErrors()
    }


    /**
     * https://medium.com/@vanniktech/testing-rxjava-code-made-easy-4cc32450fc9a
     */
    @Test
    fun `Same as above but simplier`() {
        Observable.just(1)
                .test()
                .assertResult(1)
    }

    @Test
    fun shouldCreateFromArray() {
        Observable.fromArray("a", "b")
                .doOnNext(System.out::println)
                .test()
                .assertResult("a", "b")
    }

    @Test
    fun shouldCreateFromJust() {
        Observable.just("a", "b")
                .doOnNext(System.out::println)
                .test()
                .assertResult("a", "b")
    }

    @Test
    fun shouldCreateFromCreate() {
        Observable.create<String> { emitter ->
            emitter.onNext("a")
            emitter.onNext("b")
        }
                .doOnNext(System.out::println)
                .test()
                .assertNotComplete()
    }

    // create vs subscribe (when code runs) vs defer

    // code in create is executed after subscription
    @Test
    fun shouldRunWhenSubscribed() {
        val counter = AtomicInteger()
        val observable = Observable.create<Long> { emitter ->
            System.out.println("Observable is created")
            for (i in 1..10) {
                System.out.println("counter: ${counter.incrementAndGet()}")
                emitter.onNext(System.currentTimeMillis() % 100)
            }
            System.out.println("End of emission (no onComplete)")
        }

        System.out.println("TIME before subscriptions: ${System.currentTimeMillis() % 100}")

        observable
                .doOnNext {
                    System.out.println("1: [$it]")
                }
                .test()
                .assertNotComplete()

        observable
                .doOnNext {
                    System.out.println("2: [$it]")
                }
                .test()
                .assertNotComplete()

        observable
                .doOnNext {
                    System.out.println("3: [$it]")
                }
                .test()
                .assertNotComplete()
    }

    @Test
    fun shouldRunWhenSubscribedWithOnComplete() {
        val counter = AtomicInteger()
        val observable = Observable.create<Long> { emitter ->
            System.out.println("Observable is created")
            for (i in 1..10) {
                System.out.println("counter: ${counter.incrementAndGet()}")
                emitter.onNext(System.currentTimeMillis() % 100)
            }
            System.out.println("End of emission >> onComplete")
            emitter.onComplete()
        }

        System.out.println("TIME before subscriptions: ${System.currentTimeMillis() % 100}")

        observable
                .doOnNext {
                    System.out.println("1: [$it]")
                }
                .test()
                .assertComplete()

        observable
                .doOnNext {
                    System.out.println("2: [$it]")
                }
                .test()
                .assertComplete()

        observable
                .doOnNext {
                    System.out.println("3: [$it]")
                }
                .test()
                .assertComplete()
    }

    @Test
    fun shouldRunWhenSubscribedWithOnCompleteAndJust() {
        val counter = AtomicInteger()
        val observable = Observable.fromArray(getCurrentTime(), getCurrentTime(), getCurrentTime(), getCurrentTime(), getCurrentTime(), getCurrentTime(), getCurrentTime(), getCurrentTime(), getCurrentTime(), getCurrentTime())

        System.out.println("TIME before subscriptions: ${getCurrentTime()}")

        observable
                .doOnNext {
                    System.out.println("1: [$it]")
                }
                .test()
                .assertComplete()

        observable
                .doOnNext {
                    System.out.println("2: [$it]")
                }
                .test()
                .assertComplete()

        observable
                .doOnNext {
                    System.out.println("3: [$it]")
                }
                .test()
                .assertComplete()
    }

    private fun getCurrentTime(): Long {
        val time = System.currentTimeMillis() % 1000
        System.out.println("getCurrentTime(): $time")
        return time
    }

    @Test
    fun shouldDelayButDoesnt1() {
        val counter = AtomicInteger()

        val observable = Observable.create<Int> { emitter ->
            System.out.println("Observable is created")

            for (i in 1..3) {
                val currentCount = counter.getAndIncrement()
                System.out.println("Emitting counter: $currentCount")

                emitter.onNext(currentCount)
            }
            System.out.println("End of emission >> onComplete")

            emitter.onComplete()
        }

        System.out.println("Counter before subscriptions: ${counter.getAndIncrement()}")

        // HERE IS DELAY FOR FLAT MAPPED OBSERVABLE
        val o = observable
                .flatMap { t -> Observable.just(t).delay(10, TimeUnit.MILLISECONDS) }
                .test()

        o.assertEmpty()
        o.assertNotComplete()

        rxRule.advanceTimeBy(10, TimeUnit.MILLISECONDS)

        o.assertResult(1, 2, 3)
    }


    @Test
    fun shouldDelaySubscriptionButNotIndividualEmissions() {
        val counter = AtomicInteger()

        val observable = Observable.create<Int> { emitter ->
            System.out.println("Observable is created")

            for (i in 1..3) {
                val currentCount = counter.getAndIncrement()
                System.out.println("Emitting counter: $currentCount")

                emitter.onNext(currentCount)
            }
            System.out.println("End of emission >> onComplete")

            emitter.onComplete()
        }

        System.out.println("Counter before subscriptions: ${counter.getAndIncrement()}")

        val o = observable
                .delay(10, TimeUnit.MILLISECONDS)
                .delaySubscription(10, TimeUnit.MILLISECONDS)
                .test()

        o.assertEmpty()
        o.assertNotComplete()

        rxRule.advanceTimeBy(20, TimeUnit.MILLISECONDS)

        o.assertResult(1, 2, 3)
    }

    class SomeClass {
        private var value: String = "EMPTY"

        fun setValue(value: String) {
            this.value = value
        }

        fun getObservable(): Observable<String> {
            return Observable.just(value)
        }

        fun getObservableFromCreate(): Observable<String> {
            return Observable.create { emitter ->
                emitter.onNext(value)
                emitter.onComplete()
            }
        }
    }

    @Test
    fun shouldReturnEmptyUsingJust() {
        val instance = SomeClass()
        val observable = instance.getObservable()
        instance.setValue("VALUE !")
        observable.subscribe(System.out::println)
    }

    @Test
    fun shouldReturnValueUsingCreate() {
        val instance = SomeClass()
        val observable = instance.getObservableFromCreate()
        instance.setValue("VALUE !")
        observable.subscribe(System.out::println)
    }
}
