package com.harana.sdk.backend.models.flow.utils.aggregators

import scala.collection.mutable

case class CountOccurrencesWithKeyLimitAggregator[T](limit: Long) extends Aggregator[Option[mutable.Map[T, Long]], T] {

  def initialElement: Option[mutable.Map[T, Long]] = Option(mutable.Map.empty[T, Long])

  def mergeValue(accOpt: Option[mutable.Map[T, Long]], next: T) = {
    accOpt.foreach(acc => addOccurrencesToMap(acc, next, 1))
    replacedWithNoneIfLimitExceeded(accOpt)
  }

  def mergeCombiners(leftOpt: Option[mutable.Map[T, Long]], rightOpt: Option[mutable.Map[T, Long]]) = {
    for {
      left <- leftOpt
      rightMap <- rightOpt
    } {
      rightMap.foreach {
        case (element, count) => addOccurrencesToMap(left, element, count)
      }
    }
    replacedWithNoneIfLimitExceeded(leftOpt)
  }

  private def addOccurrencesToMap(occurrences: mutable.Map[T, Long], element: T, count: Long) =
    occurrences(element) = occurrences.getOrElse(element, 0L) + count

  private def replacedWithNoneIfLimitExceeded(mapOpt: Option[mutable.Map[T, Long]]) =
    mapOpt.flatMap { map => if (map.size <= limit) mapOpt else None }
}
