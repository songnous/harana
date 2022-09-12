package com.harana.sdk.backend.models.flow.utils.aggregators

case class CountOccurenceAggregator[T](elementToCount: T) extends Aggregator[Long, T] {

  def initialElement: Long = 0

  def mergeValue(acc: Long, elem: T) = if (elem == elementToCount) acc + 1 else acc

  def mergeCombiners(left: Long, right: Long): Long = left + right

}
