package com.harana.sdk.backend.models.flow.actionobjects.dataframe.report.distribution.discrete

import com.harana.sdk.backend.models.flow.utils.aggregators.AggregatorBatch.BatchedResult
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.report.DataFrameReportGenerator
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.report.distribution.{DistributionBuilder, NoDistributionReasons}
import com.harana.sdk.backend.models.flow.actionobjects.report.ReportUtils
import com.harana.sdk.backend.models.flow.utils.aggregators.Aggregator
import com.harana.sdk.backend.models.flow.utils.aggregators.AggregatorBatch.BatchedResult
import com.harana.sdk.shared.models.flow.report.{DiscreteDistribution, Distribution, NoDistribution}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{BooleanType, StringType, StructField}

import scala.collection.mutable.{Map => MutableMap}

case class DiscreteDistributionBuilder(categories: Aggregator[Option[MutableMap[String, Long]], Row], missing: Aggregator[Long, Row], field: StructField) extends
  DistributionBuilder {

  def allAggregators = Seq(categories, missing)

  def build(results: BatchedResult) = {
    val categoriesMap = results.forAggregator(categories)
    val nullsCount    = results.forAggregator(missing)

    categoriesMap match {
      case Some(occurrencesMap) =>
        val labels = field.dataType match {
          case StringType  => occurrencesMap.keys.toSeq.sorted
          case BooleanType => Seq(false.toString, true.toString)
        }
        val counts = labels.map(occurrencesMap.getOrElse(_, 0L))
        DiscreteDistribution(
          field.name,
          s"Discrete distribution for ${field.name} column",
          nullsCount,
          labels.map(ReportUtils.shortenLongStrings(_, DataFrameReportGenerator.StringPreviewMaxLength)),
          counts
        )
      case None => NoDistribution(field.name, NoDistributionReasons.TooManyDistinctCategoricalValues)
    }
  }
}