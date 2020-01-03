package com.vanniktech.rxriddles

import io.reactivex.Single
import io.reactivex.functions.BiFunction

object Riddle6 {
  /**
   * Execute both [first] and [second] Single's in parallel and provide both results as a pair.
   *
   * Use case: Execute two network requests in parallel and wait for each other and process the combined data.
   */
  fun solve(first: Single<Int>, second: Single<Int>) = first.zipWith(second, BiFunction<Int, Int, Pair<Int, Int>>{ first_, second_ -> Pair(first_,second_)})
}
