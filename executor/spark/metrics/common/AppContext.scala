
package com.harana.executor.spark.metrics.common

import com.harana.executor.spark.metrics
import com.harana.executor.spark.metrics.timespan
import org.json4s.JsonAST.JValue
import org.json4s.{DefaultFormats, MappingException}
import org.json4s.jackson.Serialization

import scala.collection.mutable

case class AppContext(appInfo:          ApplicationInfo,
                      appMetrics:       AggregateMetrics,
                      executorCount:    Int,
                      coresPerExecutor: Int,
                      hostMap:          mutable.HashMap[String, timespan.HostTimeSpan],
                      executorMap:      mutable.HashMap[String, timespan.ExecutorTimeSpan],
                      jobMap:           mutable.HashMap[Long, timespan.JobTimeSpan],
                      jobSQLExecIdMap:  mutable.HashMap[Long, Long],
                      stageMap:         mutable.HashMap[Int, timespan.StageTimeSpan],
                      stageIDToJobID:   mutable.HashMap[Int, Long]) {

  def filterByStartAndEndTime(startTime: Long, endTime: Long): AppContext = {
    new AppContext(appInfo,
      appMetrics,
      executorCount,
      coresPerExecutor,
      hostMap,
      executorMap
        .filter(x => x._2.endTime == 0 ||            //still running
                     x._2.endTime >= startTime ||    //ended while app was running
                     x._2.startTime <= endTime),     //started while app was running
      jobMap
        .filter(x => x._2.startTime >= startTime &&
                     x._2.endTime <= endTime),
      jobSQLExecIdMap,
      stageMap
        .filter(x => x._2.startTime >= startTime &&
                     x._2.endTime <= endTime),
      stageIDToJobID)
  }

  override def toString(): String = {
    implicit val formats = DefaultFormats
    val map = Map(
      "appInfo" -> appInfo.getMap(),
      "appMetrics" -> appMetrics.getMap(),
      "executorCount" -> executorCount,
      "coresPerExecutor" -> coresPerExecutor,
      "hostMap" -> AppContext.getMap(hostMap),
      "executorMap" -> AppContext.getMap(executorMap),
      "jobMap" -> AppContext.getMap(jobMap),
      "jobSQLExecIdMap" -> jobSQLExecIdMap,
      "stageMap" -> AppContext.getMap(stageMap),
      "stageIDToJobID" -> stageIDToJobID
    )
    Serialization.writePretty(map)
  }
}

object AppContext {

  def getMaxConcurrent[Span <: timespan.TimeSpan](map: mutable.HashMap[String, Span],
                                                  appContext: AppContext = null): Long = {

    // sort all start and end times on basis of timing
    val sorted = map.values.flatMap(timeSpan => {
      val correctedEndTime = if (timeSpan.endTime == 0) {
        if (appContext == null || appContext.appInfo.endTime == 0) {
          System.currentTimeMillis()
        } else appContext.appInfo.endTime
      } else timeSpan.endTime
      Seq[(Long, Long)]((timeSpan.startTime, 1L), (correctedEndTime, -1L))
    }).toArray
      .sortWith((t1: (Long, Long), t2: (Long, Long)) => {
        // for same time entry, we add them first, and then remove
        if (t1._1 == t2._1) {
          t1._2 > t2._2
        } else t1._1 < t2._1
      })

    var count = 0L
    var maxConcurrent = 0L

    sorted.foreach(tuple => {
      count = count + tuple._2
      maxConcurrent = math.max(maxConcurrent, count)
    })

    //when running in local mode, we don't get
    //executor added event. Default to 1 instead of 0
    math.max(maxConcurrent, 1)
  }

  def getExecutorCores(ac: AppContext): Int = {
    if (ac.executorMap.values.nonEmpty) {
      ac.executorMap.values.last.cores
    } else {
      //using default 1 core
      1
    }
  }

  def getMap[T](map: mutable.HashMap[T, _ <: timespan.TimeSpan]): Map[String, Any] = {
    if (map.isEmpty) {
      Map.empty[String, Any]
    } else {
      map.keys.last match {
        case _: String | _: Long | _: Int =>
          map.keys.map(key => (key.toString, map(key).getMap())).toMap
        case _ => throw new RuntimeException("Unknown map key type")
      }
    }
  }

  def getContext(json: JValue): AppContext = {

    implicit val formats = DefaultFormats

    new AppContext(
      metrics.common.ApplicationInfo.getObject((json \ "appInfo").extract[JValue]),
      metrics.common.AggregateMetrics.getAggregateMetrics((json \ "appMetrics").extract[JValue]),
      0, // FIXME
      0, // FIXME
      timespan.HostTimeSpan.getTimeSpan((json \ "hostMap").extract[Map[String, JValue]]),
      timespan.ExecutorTimeSpan.getTimeSpan((json \ "executorMap").extract[Map[String, JValue]]),
      timespan.JobTimeSpan.getTimeSpan((json \ "jobMap").extract[Map[String, JValue]]),
      getJobSQLExecIdMap(json, new mutable.HashMap[Long, Long]),
      timespan.StageTimeSpan.getTimeSpan((json \ "stageMap").extract[Map[String, JValue]]),
      getJobToStageMap((json \ "stageIDToJobID").extract[Map[Int, JValue]])
    )
}

  private def getJobToStageMap(json: Map[Int, JValue]): mutable.HashMap[Int, Long] = {
    implicit val formats = DefaultFormats
    val map = new mutable.HashMap[Int, Long]()

    json.keys.map(key => {
      map.put(key, json(key).extract[Long])
    })
    map
  }

  private def getLongToLongMap(json: Map[Long, JValue]): mutable.HashMap[Long, Long] = {
    implicit val formats = DefaultFormats
    val map = new mutable.HashMap[Long, Long]()

    json.keys.map(key => {
      map.put(key, json(key).extract[Long])
    })
    map
  }

  // jobSQLExecIdMap was added in release 0.2.2, so the Sparklens Jsons generated by the older
  // versions will not have it. Ensure that we return an empty map in such cases
  private def getJobSQLExecIdMap(json: JValue,
                                 defaultMap: mutable.HashMap[Long, Long]): mutable.HashMap[Long, Long] = {
    try {
      implicit val formats = DefaultFormats
      getLongToLongMap((json \ "jobSQLExecIdMap").extract[Map[Long, JValue]])
    } catch {
      case e: MappingException =>
        defaultMap
      case e: Exception =>
        throw(e)
    }
  }
}

