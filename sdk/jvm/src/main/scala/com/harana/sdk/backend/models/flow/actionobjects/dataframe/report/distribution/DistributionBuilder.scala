package com.harana.sdk.backend.models.flow.actionobjects.dataframe.report.distribution

import com.harana.sdk.backend.models.flow.utils.aggregators.AggregatorBatch.BatchedResult
import com.harana.sdk.backend.models.flow.utils.aggregators.Aggregator
import com.harana.sdk.backend.models.flow.utils.aggregators.AggregatorBatch.BatchedResult
import com.harana.sdk.shared.models.flow.report.Distribution
import org.apache.spark.sql.Row

trait DistributionBuilder {

  def allAggregators: Seq[Aggregator[_, Row]]

  def build(results: BatchedResult): Distribution

}
