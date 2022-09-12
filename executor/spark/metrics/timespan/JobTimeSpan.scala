

package com.harana.executor.spark.metrics.timespan

import com.harana.executor.spark.metrics
import com.harana.executor.spark.metrics.common
import org.apache.spark.executor.TaskMetrics
import org.apache.spark.scheduler.TaskInfo
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JValue

import scala.collection.{immutable, mutable}


/*
* The timeSpan of a Job can seen with respect to other jobs as well
* as driver timeSpans providing a timeLine. The other way to look at
* Job timeline is to dig deep and check how the individual stages are
* doing
*
* @param jobID
*/

class JobTimeSpan(val jobID: Long) extends TimeSpan {
  var jobMetrics = new common.AggregateMetrics()
  var stageMap = new mutable.HashMap[Int, StageTimeSpan]()

  def addStage(stage: StageTimeSpan): Unit = {
    stageMap (stage.stageID) = stage
  }
  def updateAggregateTaskMetrics (taskMetrics: TaskMetrics, taskInfo: TaskInfo): Unit = {
    jobMetrics.update(taskMetrics, taskInfo)
  }

  /*
  This function computes the minimum time it would take to run this job.
  The computation takes into account the parallel stages.
   */
  def computeCriticalTimeForJob(): Long = {
    if (stageMap.isEmpty) {
      0L
    }else {
      val maxStageID = stageMap.map(x => x._1).max
      val data = stageMap.map(x =>
        (x._1,
          (
            x._2.parentStageIDs,
            x._2.stageMetrics.map(common.AggregateMetrics.executorRuntime).max
          )
        )
      )
      criticalTime(maxStageID, data)
    }
  }

  /*
  recursive function to compute critical time starting from the last stage
   */
  private def criticalTime(stageID: Int, data: mutable.HashMap[Int, (Seq[Int], Long)]): Long = {
    //Provide 0 value for
    val stageData = data.getOrElse(stageID, (List.empty[Int], 0L))
    stageData._2 + {
      if (stageData._1.size == 0) {
        0L
      }else {
        stageData._1.map(x => criticalTime(x, data)).max
      }
    }
  }

  override def getMap(): Map[String, _ <: Any] = {
    implicit val formats = DefaultFormats

    Map(
      "jobID" -> jobID,
      "jobMetrics" -> jobMetrics.getMap,
      "stageMap" -> common.AppContext.getMap(stageMap)) ++ super.getStartEndTime()
  }
}

object JobTimeSpan {
  def getTimeSpan(json: Map[String, JValue]): mutable.HashMap[Long, JobTimeSpan] = {
    implicit val formats = DefaultFormats
    val map = new mutable.HashMap[Long, JobTimeSpan]

    json.keys.map(key => {
      val value = json(key).extract[JValue]
      val timeSpan = new JobTimeSpan((value \ "jobID").extract[Long])

      timeSpan.jobMetrics = common.AggregateMetrics.getAggregateMetrics((value \ "jobMetrics")
              .extract[JValue])
      timeSpan.stageMap = metrics.timespan.StageTimeSpan.getTimeSpan((value \ "stageMap").extract[
        immutable.Map[String, JValue]])
      timeSpan.addStartEnd(value)
      map.put(key.toLong, timeSpan)

    })
    map
  }
}
