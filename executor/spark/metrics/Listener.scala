package com.harana.executor.spark.metrics

import java.time.Instant

import com.harana.sdk.backend.models.flow.execution.{ApplicationInfo, ExecutionStatus, AggregateMetric => HaranaAggregateMetric, AggregateMetrics => HaranaAggregateMetrics, AggregateValue => HaranaAggregateValue, ExecutorTimespan => HaranaExecutorTimespan, HostTimespan => HaranaHostTimespan, JobTimespan => HaranaJobTimespan, StageTimespan => HaranaStageTimespan}
import com.harana.sdk.shared.models.flow.FlowExecution
import com.harana.executor.spark.logs.LogsManager
import com.harana.executor.spark.metrics.common.{AggregateMetrics, AggregateValue, AppContext}
import com.harana.executor.spark.metrics.timespan._
import org.apache.spark.SparkConf
import org.apache.spark.scheduler._

class Listener(sparkConf: SparkConf) extends QuboleJobListener(sparkConf) {

  override def onApplicationEnd(applicationEnd: SparkListenerApplicationEnd) : Unit = {
    appInfo.endTime = applicationEnd.time

    jobMap.foreach(x => {
      if (jobMap(x._1).endTime == 0) {
        if (x._2.stageMap.nonEmpty) {
          jobMap(x._1).setEndTime(x._2.stageMap.map(y => y._2.endTime).max)
        }else {
          jobMap(x._1).setEndTime(appInfo.endTime)
        }
      }
    })

    saveMetrics()
  }

  override def onStageCompleted(stageCompleted: SparkListenerStageCompleted): Unit = {
    super.onStageCompleted(stageCompleted)
    saveMetrics()
  }

  private def saveMetrics(): Unit = {
    val flowExecutionId = sparkConf.get("harana.flowExecutionId")

    val flowException = MetricsManager.getFlowExecutionAndLogs(flowExecutionId)._1.copy(
      executionStatus = ExecutionStatus.Executing,
      info = Some(ApplicationInfo(appInfo.applicationID, Instant.ofEpochMilli(appInfo.startTime), Instant.ofEpochMilli(appInfo.endTime))),
      metrics = Some(convertMetrics(appMetrics)),
      executorCount = Some(0),
      coresPerExecutor = Some(0),
      hostMap = hostMap.map { case (k,v) => clean(k) -> convertHostTimespan(v) }.toMap,
      executorMap = executorMap.map { case (k,v) => clean(k) -> convertExecutorTimespan(v) }.toMap,
      jobMap = jobMap.mapValues(convertJobTimespan).toMap,
      stageMap = stageMap.mapValues(convertStageTimespan).toMap
    )
    LogsManager.get(flowExecutionId).mkString(", ")

    val flowLogs = MetricsManager.getFlowExecutionAndLogs(flowExecutionId)._2.copy(
      logs = LogsManager.get(flowExecutionId)
    )

    MetricsManager.updateFlow(flowException, flowLogs)
  }

  private def convertMetrics(metrics: AggregateMetrics): HaranaAggregateMetrics =
    HaranaAggregateMetrics(metrics.count, metrics.map.map { case (k,v) => convertMetric(k) -> convertValue(v) }.toMap)

  private def convertValue(av: AggregateValue): HaranaAggregateValue =
    HaranaAggregateValue(av.value, av.min, av.max, av.mean, av.variance, av.m2)

  private def convertMetric(metric: AggregateMetrics.Metric): HaranaAggregateMetric =
    metric match {
      case AggregateMetrics.shuffleWriteTime => HaranaAggregateMetric.ShuffleWriteTime
      case AggregateMetrics.shuffleWriteBytesWritten => HaranaAggregateMetric.ShuffleWriteBytesWritten
      case AggregateMetrics.shuffleWriteRecordsWritten => HaranaAggregateMetric.ShuffleWriteRecordsWritten
      case AggregateMetrics.shuffleReadFetchWaitTime => HaranaAggregateMetric.ShuffleReadFetchWaitTime
      case AggregateMetrics.shuffleReadBytesRead => HaranaAggregateMetric.ShuffleReadBytesRead
      case AggregateMetrics.shuffleReadRecordsRead => HaranaAggregateMetric.ShuffleReadRecordsRead
      case AggregateMetrics.shuffleReadLocalBlocks => HaranaAggregateMetric.ShuffleReadLocalBlocks
      case AggregateMetrics.shuffleReadRemoteBlocks => HaranaAggregateMetric.ShuffleReadRemoteBlocks
      case AggregateMetrics.executorRuntime => HaranaAggregateMetric.ExecutorRuntime
      case AggregateMetrics.jvmGCTime => HaranaAggregateMetric.JvmGCTime
      case AggregateMetrics.executorCpuTime => HaranaAggregateMetric.ExecutorCpuTime
      case AggregateMetrics.resultSize => HaranaAggregateMetric.ResultSize
      case AggregateMetrics.inputBytesRead => HaranaAggregateMetric.InputBytesRead
      case AggregateMetrics.outputBytesWritten => HaranaAggregateMetric.OutputBytesWritten
      case AggregateMetrics.memoryBytesSpilled => HaranaAggregateMetric.MemoryBytesSpilled
      case AggregateMetrics.diskBytesSpilled => HaranaAggregateMetric.DiskBytesSpilled
      case AggregateMetrics.peakExecutionMemory => HaranaAggregateMetric.PeakExecutionMemory
      case AggregateMetrics.taskDuration => HaranaAggregateMetric.TaskDuration
    }

  private def convertExecutorTimespan(ts: ExecutorTimeSpan): HaranaExecutorTimespan =
    HaranaExecutorTimespan(ts.executorID, ts.hostID, Instant.ofEpochMilli(ts.startTime), Instant.ofEpochMilli(ts.endTime), ts.cores, convertMetrics(ts.executorMetrics))

  private def convertHostTimespan(ts: HostTimeSpan): HaranaHostTimespan =
    HaranaHostTimespan(ts.hostID, Instant.ofEpochMilli(ts.startTime), Instant.ofEpochMilli(ts.endTime), convertMetrics(ts.hostMetrics))

  private def convertJobTimespan(ts: JobTimeSpan): HaranaJobTimespan =
    HaranaJobTimespan(ts.jobID.toString, Instant.ofEpochMilli(ts.startTime), Instant.ofEpochMilli(ts.endTime), convertMetrics(ts.jobMetrics), ts.stageMap.mapValues(convertStageTimespan).toMap)

  private def convertStageTimespan(ts: StageTimeSpan): HaranaStageTimespan =
    HaranaStageTimespan(ts.stageID, Instant.ofEpochMilli(ts.startTime), Instant.ofEpochMilli(ts.endTime), ts.taskExecutionTimes.length, convertMetrics(ts.stageMetrics), Instant.ofEpochMilli(ts.minTaskLaunchTime), Instant.ofEpochMilli(ts.maxTaskFinishTime), ts.parentStageIDs.toList, ts.taskExecutionTimes.toList, ts.taskPeakMemoryUsage.toList)

  private def clean(s: String): String =
    s.replace(".", "_")
}
