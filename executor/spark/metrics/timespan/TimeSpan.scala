
package com.harana.executor.spark.metrics.timespan

import org.json4s.DefaultFormats
import org.json4s.JsonAST.JValue

/*
 * We will look at the application as a sequence of timeSpans
 */
trait TimeSpan  {
  var startTime: Long = 0
  var endTime: Long = 0

  def setEndTime(time: Long): Unit = {
    endTime = time
  }

  def setStartTime(time: Long): Unit = {
    startTime = time
  }
  def isFinished(): Boolean = (endTime != 0 && startTime != 0)

  def duration(): Option[Long] = {
    if (isFinished()) {
      Some(endTime - startTime)
    } else {
      None
    }
  }
  def getMap(): Map[String, _ <: Any]

  def getStartEndTime(): Map[String, Long] = {
    Map("startTime" -> startTime, "endTime" -> endTime)
  }

  def addStartEnd(json: JValue): Unit = {
    implicit val formats = DefaultFormats
    this.startTime = (json \ "startTime").extract[Long]
    this.endTime = (json \ "endTime").extract[Long]
  }
}
