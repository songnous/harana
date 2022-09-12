package com.harana.sdk.backend.models.flow.utils.aggregators

import org.apache.spark.rdd.RDD

object AggregatorBatch {

  case class BatchedResult(rawResults: Map[Aggregator[_, _], Any]) {
    def forAggregator[U, Any](aggregator: Aggregator[U, _]): U = rawResults(aggregator).asInstanceOf[U]
  }

  def executeInBatch[T](rdd: RDD[T], aggregators: Seq[Aggregator[_, T]]) = {
    val batch   = SplitterAggregator[Any, T](aggregators.map(_.asInstanceOf[Aggregator[Any, T]]))
    val results = batch.execute(rdd)

    val rawResultsMap: Map[Aggregator[_, _], Any] = aggregators.zip(results).map { case (aggregator, result) => aggregator -> result }.toMap
    BatchedResult(rawResultsMap)
  }

  private case class SplitterAggregator[U, T](aggregators: Seq[Aggregator[U, T]]) extends Aggregator[Seq[U], T] {
    def initialElement: Seq[U] = aggregators.map(_.initialElement)

    def mergeValue(accSeq: Seq[U], elem: T) =
      accSeq.lazyZip(aggregators).map((acc, aggregator) => aggregator.mergeValue(acc, elem))

    def mergeCombiners(leftSeq: Seq[U], rightSeq: Seq[U]) =
      leftSeq.lazyZip(rightSeq).lazyZip(aggregators).map { (left, right, aggregator) =>
        aggregator.mergeCombiners(left, right)
      }
  }
}
