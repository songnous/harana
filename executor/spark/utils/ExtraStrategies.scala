package com.harana.executor.spark.utils

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Strategy
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.SparkPlan


object ExtraStrategies extends Strategy {
  def register(spark: SparkSession): Unit = {
    spark.experimental.extraStrategies = Seq(ExtraStrategies)
  }

  def apply(plan: LogicalPlan): Seq[SparkPlan] = {
    plan match {
      case CountRecords(child, counter) => CountRecordsExec(planLater(child), counter) :: Nil
      case _ => Nil
    }
  }
}