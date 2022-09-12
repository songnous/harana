package com.harana.sdk.shared.models.flow.utils

object CollectionExtensions {

  implicit class RichSeq[T](seq: Seq[T]) {

    def hasUniqueValues: Boolean = seq.distinct.size == seq.size
    def hasDuplicates: Boolean = !hasUniqueValues

    def lookupBy[R](f: T => R): Map[R, T] = {
      val mapEntries = seq.map(e => f(e) -> e)
      assert(mapEntries.size == seq.size, "Function f must be injective, otherwise we would override some key")
      mapEntries.toMap
    }

    def isSorted(implicit ord: Ordering[T]): Boolean = seq == seq.sorted
  }

  implicit class RichSet[T](set: Set[T]) {
    def xor(another: Set[T]): Set[T] = set.diff(another).union(another.diff(set))
  }
}
