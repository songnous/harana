package com.harana.sdk.backend.models.flow.actionobjects.dataframe.report.distribution.continuous

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.report.distribution.ColumnStats
import org.apache.spark.sql.types._

object BucketsCalculator {

  val DefaultBucketsNumber = 20
  val DoubleTolerance = 0.000001

  def calculateBuckets(dataType: DataType, columnStats: ColumnStats) =
    customRange(columnStats.min, columnStats.max, numberOfSteps(columnStats, dataType))

  private def numberOfSteps(columnStats: ColumnStats, dataType: DataType) =
    if (columnStats.max - columnStats.min < DoubleTolerance)
      1
    else if (isIntegerLike(dataType))
      Math.min(columnStats.max.toLong - columnStats.min.toLong + 1, DefaultBucketsNumber).toInt
    else
      DefaultBucketsNumber

  private def customRange(min: Double, max: Double, steps: Int) =
    (Range.Int(0, steps, 1).map(s => min + (s * max - min) / steps) :+ max).toArray

  private def isIntegerLike(dataType: DataType) =
    dataType match {
      case ByteType | ShortType | IntegerType | LongType | TimestampType | DateType => true
      case _                                                                        => false
    }
}
