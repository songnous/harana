

package com.harana.executor.spark.metrics.timespan

import com.harana.executor.spark.metrics.common
import org.apache.spark.executor.TaskMetrics
import org.apache.spark.scheduler.TaskInfo
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JValue

import scala.collection.mutable


class HostTimeSpan(val hostID: String) extends TimeSpan {
  var hostMetrics = new common.AggregateMetrics()

/*
We don't get any event when host is lost.
TODO: may be mark all host end time when execution is stopped
 */
  override def duration():Option[Long] = {
    Some(super.duration().getOrElse(System.currentTimeMillis() - startTime))
  }

  def updateAggregateTaskMetrics (taskMetrics: TaskMetrics, taskInfo: TaskInfo): Unit = {
    hostMetrics.update(taskMetrics, taskInfo)
  }
  override def getMap(): Map[String, _ <: Any] = {
    implicit val formats = DefaultFormats
    Map("hostID" -> hostID, "hostMetrics" -> hostMetrics.getMap) ++ super.getStartEndTime()
  }

}

object HostTimeSpan {
  def getTimeSpan(json: Map[String, JValue]): mutable.HashMap[String, HostTimeSpan] = {
    implicit val formats = DefaultFormats
    val map = new mutable.HashMap[String, HostTimeSpan]

    json.keys.map(key => {
      val value = json(key)
      val timeSpan = new HostTimeSpan((value \ "hostID").extract[String])
      timeSpan.hostMetrics = common.AggregateMetrics.getAggregateMetrics((value \ "hostMetrics")
        .extract[JValue])
      timeSpan.addStartEnd(value)
      map.put(key, timeSpan)
    })

    map
  }
}
