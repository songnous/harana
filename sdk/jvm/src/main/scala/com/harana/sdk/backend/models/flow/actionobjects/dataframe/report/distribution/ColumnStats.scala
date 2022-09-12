package com.harana.sdk.backend.models.flow.actionobjects.dataframe.report.distribution

import org.apache.spark.mllib.stat.MultivariateStatisticalSummary

case class ColumnStats(min: Double, max: Double, mean: Double)

object ColumnStats {
  def fromMultiVarStats(multiVarStats: MultivariateStatisticalSummary, column: Int) =
    ColumnStats(multiVarStats.min(column), multiVarStats.max(column), multiVarStats.mean(column))
}