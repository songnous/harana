package com.harana.executor.spark.logs

import java.time.Instant

import com.harana.sdk.backend.models.designer.flow.execution.ExecutionLog
import org.apache.logging.log4j.core.LogEvent

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.Try

object LogsManager {
  private val logs = mutable.Map[String, ListBuffer[ExecutionLog]]()

  def append(le: LogEvent): Unit = {
    val id = Try(le.getContextMap.get("flowExecutionId"))
    if (id.isSuccess) {
      val event = ExecutionLog(le.getLevel.toString, le.getMessage.toString, Instant.ofEpochMilli(le.getTimeMillis))
      logs.get(id.get) match {
        case Some(x) =>
          x += event
        case None =>
          val list = new ListBuffer[ExecutionLog]
          list += event
          logs.put(id.get, list)
      }
    }
  }

  def get(applicationId: String): List[ExecutionLog] =
    logs.get(applicationId).map(_.toList).getOrElse(List())

  def clear(applicationId: String): Unit =
    logs.remove(applicationId)
}