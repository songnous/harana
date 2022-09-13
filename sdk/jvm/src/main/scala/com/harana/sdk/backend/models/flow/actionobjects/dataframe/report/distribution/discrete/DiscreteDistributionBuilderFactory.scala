package com.harana.sdk.backend.models.flow.actionobjects.dataframe.report.distribution.discrete

import com.harana.sdk.backend.models.flow.utils.SparkTypeConverter._
import com.harana.sdk.backend.models.flow.utils.aggregators.{CountOccurenceAggregator, CountOccurrencesWithKeyLimitAggregator}
import org.apache.spark.sql.types.StructField

private[distribution] object DiscreteDistributionBuilderFactory {

  val MaxDistinctValuesToCalculateDistribution = 10

  def prepareBuilder(columnIndex: Int, field: StructField): DiscreteDistributionBuilder = {
    val missing = CountOccurenceAggregator[Option[Any]](None)
      .mapInput(getOption(columnIndex))

    val categories = CountOccurrencesWithKeyLimitAggregator(
      MaxDistinctValuesToCalculateDistribution
    ).mapInput(getColumnAsString(columnIndex))

    DiscreteDistributionBuilder(categories, missing, field)
  }
}