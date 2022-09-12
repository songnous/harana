

package com.harana.executor.spark.metrics.analyzer

import com.harana.executor.spark.metrics.{common, timespan}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/*
 * Created by rohitk on 21/09/17.
 */
class ExecutorTimelineAnalyzer extends AppAnalyzer {

  def analyze(appContext: common.AppContext, startTime: Long, endTime: Long): String = {
    val ac = appContext.filterByStartAndEndTime(startTime, endTime)
    val out = new mutable.StringBuilder()

    out.println("\nPrinting executors timeline....\n")
    out.println(s"Total Executors ${ac.executorMap.size}, " +
      s"and maximum concurrent executors = ${common.AppContext.getMaxConcurrent(ac.executorMap, ac)}")

    val minuteExecutorMap = new mutable.HashMap[String, (ListBuffer[timespan.ExecutorTimeSpan], ListBuffer[timespan.ExecutorTimeSpan])]()

    ac.executorMap.values
      .foreach( x => {
        val startMinute = MINUTES_DF.format(x.startTime)
        val minuteLists = minuteExecutorMap.getOrElseUpdate(startMinute, (new mutable.ListBuffer[timespan.ExecutorTimeSpan](), new mutable.ListBuffer[timespan.ExecutorTimeSpan]()))
        minuteLists._1 += x
        if (x.endTime != 0) {
          val endMinute = MINUTES_DF.format(x.endTime)
          val minuteEndList = minuteExecutorMap.getOrElse(endMinute, (new mutable.ListBuffer[timespan.ExecutorTimeSpan](), new mutable.ListBuffer[timespan.ExecutorTimeSpan]()))
          minuteEndList._2 += x
        }
      })

    var currentCount = 0
    minuteExecutorMap.keys.toBuffer
      .sortWith( (a, b) => a < b)
      .foreach( x => {
        currentCount = currentCount  + minuteExecutorMap(x)._1.size -  minuteExecutorMap(x)._2.size
        out.println (s"At ${x} executors added ${minuteExecutorMap(x)._1.size} & removed  ${minuteExecutorMap(x)._2.size} currently available ${currentCount}")
      })

    out.println("\nDone printing executors timeline...\n============================\n")
    out.toString()
  }
}
