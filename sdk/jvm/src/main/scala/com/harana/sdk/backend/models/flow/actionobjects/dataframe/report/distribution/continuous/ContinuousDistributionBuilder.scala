package com.harana.sdk.backend.models.flow.actionobjects.dataframe.report.distribution.continuous

import com.harana.sdk.backend.models.flow.utils.aggregators.AggregatorBatch.BatchedResult
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.report.distribution.{ColumnStats, DistributionBuilder}
import com.harana.sdk.backend.models.flow.utils.aggregators.Aggregator
import com.harana.sdk.backend.models.flow.utils.aggregators.AggregatorBatch.BatchedResult
import com.harana.sdk.shared.models.flow.report.{ContinuousDistribution, Distribution, Statistics}
import com.harana.sdk.shared.models.flow.utils.DoubleUtils
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

import java.time.Instant

case class ContinuousDistributionBuilder(
    histogram: Aggregator[Array[Long], Row],
    missing: Aggregator[Long, Row],
    field: StructField,
    columnStats: ColumnStats
) extends DistributionBuilder {

  def allAggregators: Seq[Aggregator[_, Row]] = Seq(histogram, missing)

  def build(results: BatchedResult): Distribution = {
    val buckets = BucketsCalculator.calculateBuckets(field.dataType, columnStats)

    val histogramCounts = results.forAggregator(histogram)
    val nullsCount      = results.forAggregator(missing)

    val labels = buckets2Labels(buckets.toIndexedSeq, field)

    val stats = Statistics(
      double2Label(field)(columnStats.max),
      double2Label(field)(columnStats.min),
      mean2Label(field)(columnStats.mean)
    )

    ContinuousDistribution(
      field.name,
      s"Continuous distribution for ${field.name} column",
      nullsCount,
      labels,
      histogramCounts.toIndexedSeq,
      stats
    )
  }

  private def buckets2Labels(buckets: Seq[Double], structField: StructField) =
    buckets.map(double2Label(structField))

  // We want to present mean of integer-like values as a floating point number, however dates, timestamps and booleans should be converted to their original type.
  def mean2Label(structField: StructField)(d: Double) = structField.dataType match {
    case ByteType | ShortType | IntegerType | LongType => DoubleUtils.double2String(d)
    case _                                             => double2Label(structField)(d)
  }

  def double2Label(structField: StructField)(d: Double) =
    if (d.isNaN)
      "NaN"
    else {
      structField.dataType match {
        case ByteType                                => d.toByte.toString
        case ShortType                               => d.toShort.toString
        case IntegerType                             => d.toInt.toString
        case LongType                                => d.toLong.toString
        case FloatType | DoubleType | _: DecimalType => DoubleUtils.double2String(d)
        case BooleanType                             => if (d == 0d) false.toString else true.toString
        case TimestampType | DateType                => Instant.ofEpochMilli(d.toLong).toString
      }
    }
}