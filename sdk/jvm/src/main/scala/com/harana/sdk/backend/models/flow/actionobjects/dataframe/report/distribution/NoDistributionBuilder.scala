package com.harana.sdk.backend.models.flow.actionobjects.dataframe.report.distribution

import com.harana.sdk.backend.models.flow.utils.aggregators.AggregatorBatch.BatchedResult
import com.harana.sdk.backend.models.flow.utils.aggregators.Aggregator
import com.harana.sdk.backend.models.flow.utils.aggregators.AggregatorBatch.BatchedResult
import com.harana.sdk.shared.models.flow.report.{Distribution, NoDistribution}
import org.apache.spark.sql.Row

case class NoDistributionBuilder(name: String, description: String) extends DistributionBuilder {

  def allAggregators: Seq[Aggregator[_, Row]] = Nil

  def build(results: BatchedResult): Distribution = NoDistribution(name, description)

}