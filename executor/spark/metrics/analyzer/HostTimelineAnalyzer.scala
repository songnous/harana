

package com.harana.executor.spark.metrics.analyzer

import com.harana.executor.spark.metrics.common
import com.harana.executor.spark.metrics.common.AppContext
import com.harana.executor.spark.metrics.{common, timespan}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/*
 * Created by rohitk on 21/09/17.
 */
class HostTimelineAnalyzer extends AppAnalyzer {

  def analyze(appContext: AppContext, startTime: Long, endTime: Long): String = {
    val ac = appContext.filterByStartAndEndTime(startTime, endTime)
    val out = new mutable.StringBuilder()
    out.println(s"\nTotal Hosts ${ac.hostMap.size}, " +
      s"and the maximum concurrent hosts = ${common.AppContext.getMaxConcurrent(ac.hostMap, ac)}")
    val minuteHostMap = new mutable.HashMap[Long, ListBuffer[timespan.HostTimeSpan]]()
    ac.hostMap.values
      .foreach( x => {
        val startMinute = x.startTime / 60*1000
        val minuteList = minuteHostMap.getOrElse(startMinute, new mutable.ListBuffer[timespan.HostTimeSpan]())
        minuteList += x
      })
    minuteHostMap.keys.toBuffer
      .sortWith( (a, b) => a < b)
      .foreach( x => {
        out.println (s"At ${pt(x*60*1000)} added ${minuteHostMap(x).size} hosts ")
      })
    out.println("\n")
    ac.hostMap.values.foreach(x => {
      val executorsOnHost = ac.executorMap.values.filter( _.hostID.equals(x.hostID))
      out.println(s"Host ${x.hostID} startTime ${pt(x.startTime)} executors count ${executorsOnHost.size}")
    })
    out.println("Done printing host timeline\n======================\n")
    out.toString()
  }
}
