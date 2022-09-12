
package com.harana.executor.spark.metrics

import com.harana.executor.spark.metrics.common.AppContext
import org.apache.spark.scheduler.{SparkListenerApplicationEnd, SparkListenerTaskStart}
import org.apache.spark.util.SizeEstimator
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class QuboleNotebookListener(sparkConf: SparkConf) extends QuboleJobListener(sparkConf: SparkConf) {

  private lazy val defaultExecutorCores = sparkConf.getInt("spark.executor.cores", 2)

  override def onApplicationEnd(applicationEnd: SparkListenerApplicationEnd): Unit = {
    stageMap.values.foreach(x => x.tempTaskTimes.clear())
    appInfo.endTime = applicationEnd.time
  }

  override def onTaskStart(taskStart: SparkListenerTaskStart): Unit = {
    val taskInfo = taskStart.taskInfo
    val executorTimeSpan = executorMap.get(taskInfo.executorId)
    if (executorTimeSpan.isEmpty) {
      val timeSpan = new timespan.ExecutorTimeSpan(taskInfo.executorId,
        taskInfo.host, defaultExecutorCores)
      timeSpan.setStartTime(taskInfo.launchTime)
      executorMap(taskInfo.executorId) = timeSpan
    }
    val hostTimeSpan = hostMap.get(taskInfo.host)
    if (hostTimeSpan.isEmpty) {
      val timeSpan = new timespan.HostTimeSpan(taskInfo.host)
      timeSpan.setStartTime(taskInfo.launchTime)
      hostMap(taskInfo.host) = timeSpan
    }
  }


  def estimateSize(): Long = {
    SizeEstimator.estimate(this)
  }

  def purgeJobsAndStages():Unit = {
    stageMap.clear()
    jobMap.clear()
  }

  def getStats(fromTime: Long, toTime: Long): String = {
    val list = new ListBuffer[analyzer.AppAnalyzer]
    list += new analyzer.StageSkewAnalyzer
    list += new analyzer.ExecutorWallclockAnalyzer
    list += new analyzer.EfficiencyStatisticsAnalyzer

    val appContext = new AppContext(appInfo,
      appMetrics,
      0, // FIXME
      0, // FIXME
      hostMap,
      executorMap,
      jobMap,
      jobSQLExecIDMap,
      stageMap,
      stageIDToJobID)

    val out = new mutable.StringBuilder()

    list.foreach(x => {
      try {
        val result = x.analyze(appContext, fromTime, toTime)
        out.append(result)
      } catch {
        case e:Throwable => {
          println(s"Failed in Analyzer ${x.getClass.getSimpleName}")
          e.printStackTrace()
        }
      }
    })
    out.toString()
  }

  def getMaxDataSize: Int  = maxDataSize
  def getWaiTimeInSeconds: Int  = waitTimeMs/1000

  val maxDataSize = 32 * 1024 * 1024
  val waitTimeMs = 5000

  def profileIt[R](block: => R): R = {
    if (estimateSize() > maxDataSize ) {
      purgeJobsAndStages()
    }
    val startTime = System.currentTimeMillis()
    val result = block
    val endTime = System.currentTimeMillis()
    Thread.sleep(waitTimeMs)
    println(getStats(startTime,endTime))
    result
  }
}

object QuboleNotebookListener {
  def registerAndGet(sc: SparkContext): QuboleNotebookListener = {
    val listener = new QuboleNotebookListener(sc.getConf)
    sc.addSparkListener(listener)
    listener
  }
}