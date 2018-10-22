package com.vanniktech.rxriddles

import com.vanniktech.rxriddles.solutions.Riddle12Solution
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/** Solution [Riddle12Solution] */
class Riddle1003CustomObservable {

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


}
