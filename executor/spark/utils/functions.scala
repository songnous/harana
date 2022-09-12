package com.harana.executor.spark

import com.harana.executor.spark.utils.CountRecords
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.util.LongAccumulator

package object utils {

  def ofRows(sparkSession: SparkSession, logicalPlan: LogicalPlan): DataFrame = {
    val qe = sparkSession.sessionState.executePlan(logicalPlan)
    qe.assertAnalyzed()
    new Dataset[Row](sparkSession, logicalPlan, RowEncoder(qe.analyzed.schema))
  }

  def countRecords(df: DataFrame): Long = {
    val counter = df.sparkSession.sparkContext.longAccumulator
    CountRecords(df.queryExecution.logical, counter)
    counter.count
  }

  def countRecords(df: DataFrame, counter: LongAccumulator): DataFrame =
    ofRows(df.sparkSession, CountRecords(df.queryExecution.logical, counter))
}
