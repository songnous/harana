

package com.harana.executor.spark.metrics.analyzer

import com.harana.executor.spark.metrics.common

import scala.collection.mutable

/*
 * Created by rohitk on 21/09/17.
 */
class SimpleAppAnalyzer extends AppAnalyzer {

  def analyze(appContext: common.AppContext, startTime: Long, endTime: Long): String = {
    val ac = appContext.filterByStartAndEndTime(startTime, endTime)
    val out = new mutable.StringBuilder()

    out.println("\nPrinting application meterics. These metrics are collected at " +
      "task-level granularity and aggregated across the app (all tasks, stages, and jobs).\n")
    ac.appMetrics.print("Application Metrics", out)
    out.println("\n")
    out.toString()
  }
}
