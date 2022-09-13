package com.harana.workflowexecutor.executor

import com.harana.sdk.backend.models.flow.utils.Logging
import org.apache.spark.api.r.SparkRBackend
import com.harana.sdk.backend.models.flow.CustomCodeExecutor
import com.harana.workflowexecutor.Unzip
import com.harana.workflowexecutor.customcode.CustomCodeEntryPoint

class RExecutionCaretaker(rExecutorPath: String, customCodeEntryPoint: CustomCodeEntryPoint) extends Logging {

  private val backend = new SparkRBackend()

  def backendListeningPort = backend.port
  def rCodeExecutor = RExecutor(backend.port, backend.entryPointId, customCodeEntryPoint, extractRExecutor())

  def start(): Unit = backend.start(customCodeEntryPoint)

  private def extractRExecutor() =
    if (rExecutorPath.endsWith(".jar")) {
      val tempDir = Unzip.unzipToTmp(rExecutorPath, _.equals("r_executor.R"))
      s"$tempDir/r_executor.R"
    } else
      rExecutorPath

}
