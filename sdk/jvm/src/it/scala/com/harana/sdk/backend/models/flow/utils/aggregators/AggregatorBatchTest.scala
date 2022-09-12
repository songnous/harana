package com.harana.sdk.backend.models.flow.utils.aggregators

import com.harana.sdk.backend.models.flow.{IntegratedTestSupport, UnitSpec}

case class SetAggregator() extends Aggregator[Set[Int], Int] {

  def initialElement = Set.empty

  def mergeValue(acc: Set[Int], elem: Int) = acc + elem

  def mergeCombiners(left: Set[Int], right: Set[Int]) = left ++ right

}

case class SumAggregator() extends Aggregator[Int, Int] {

  def initialElement = 0

  def mergeValue(acc: Int, elem: Int) = acc + elem

  def mergeCombiners(left: Int, right: Int) = left + right

}

class AggregatorBatchTest extends IntegratedTestSupport {

  "AggregatorBatch" should {

    "properly execute all aggregation actions for provided aggregators" in {
      val rdd = sparkContext.parallelize(Seq(1, 2, 3))

      val setAggregator = SetAggregator()
      val sumAggregator = SumAggregator()

      val results = AggregatorBatch.executeInBatch(rdd, Seq(setAggregator, sumAggregator))

      results.forAggregator(setAggregator) shouldEqual Set(1, 2, 3)
      results.forAggregator(sumAggregator) shouldEqual 6
    }

  }
}
