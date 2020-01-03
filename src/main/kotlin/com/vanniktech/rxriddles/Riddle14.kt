package com.vanniktech.rxriddles

import io.reactivex.Single
import java.lang.IllegalArgumentException

object Riddle14 {
  /**
   * Retry the given [source] up to three times unless an [IllegalArgumentException] has been emitted.
   *
   * Use case: Retry an operation for a number of times or until a valid error occurred.
   */
  fun solve(source: Single<Unit>): Single<Unit> {
    return source.retry(2) { it !is IllegalArgumentException } // why 2 times??
  }
}
