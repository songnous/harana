package com.harana.designer.backend.services

import com.harana.designer.backend.flowexecutions.colors._
import com.harana.sdk.shared.models.flow.execution.spark.ExecutionLog

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

package object flowexecutions {

  val logFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").withLocale(Locale.ENGLISH).withZone(ZoneId.systemDefault)

  def formatLog(log: ExecutionLog): String = {
    val time = s"[${logFormatter.format(log.timestamp)}]"

    log.level match {
      case "DEBUG" => s"$time   $MAGENTA${log.message}$RESET"
      case "ERROR" => s"$time   $RED_BOLD${log.message}$RESET"
      case "TRACE" => s"$CYAN${log.message}$RESET"
      case "WARN" => s"$time   $YELLOW${log.message}$RESET"
      case _ => s"$time   ${log.message}"
    }
  }
}