

package com.harana.executor.spark.metrics.timespan

import com.harana.executor.spark.metrics.common
import org.apache.spark.executor.TaskMetrics
import org.apache.spark.scheduler.TaskInfo
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JValue

import scala.collection.mutable

class ExecutorTimeSpan(val executorID: String,
                       val hostID: String,
                       val cores: Int) extends TimeSpan {
  var executorMetrics = new common.AggregateMetrics()

  def updateAggregateTaskMetrics (taskMetrics: TaskMetrics, taskInfo: TaskInfo): Unit = {
    executorMetrics.update(taskMetrics, taskInfo)
  }

  override def getMap(): Map[String, _ <: Any] = {
    implicit val formats = DefaultFormats

    Map("executorID" -> executorID, "hostID" -> hostID, "cores" -> cores, "executorMetrics" ->
      executorMetrics.getMap()) ++ super.getStartEndTime()
  }
}

object ExecutorTimeSpan {
  def getTimeSpan(json: Map[String, JValue]): mutable.HashMap[String, ExecutorTimeSpan] = {

    implicit val formats = DefaultFormats
    val map = new mutable.HashMap[String, ExecutorTimeSpan]

    json.keys.map(key => {
      val value = json(key)
      val timeSpan = new ExecutorTimeSpan(
        (value \ "executorID").extract[String],
        (value \ "hostID").extract[String],
        (value \ "cores").extract[Int]
      )
      timeSpan.executorMetrics = common.AggregateMetrics.getAggregateMetrics((value
              \ "executorMetrics").extract[JValue])
      timeSpan.addStartEnd(value)
      map.put(key, timeSpan)
    })
    map
  }
}