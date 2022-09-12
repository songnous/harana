package com.harana.sdk.backend.models.flow

import ActionExecutionDispatcher.Result
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrameBuilder
import com.harana.sdk.backend.models.flow.filesystemclients.FileSystemClient
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.backend.models.flow.mail.EmailSender
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.shared.models.flow.ExecutionMode
import com.harana.sdk.shared.models.flow.utils.Id
import com.harana.spark.SparkSQLSession
import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame => SparkDataFrame}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class CommonExecutionContext(
    sparkContext: SparkContext,
    sparkSQLSession: SparkSQLSession,
    inferContext: InferContext,
    executionMode: ExecutionMode,
    fsClient: FileSystemClient,
    tempPath: String,
    libraryPath: String,
    innerWorkflowExecutor: InnerWorkflowExecutor,
    dataFrameStorage: DataFrameStorage,
    emailSender: Option[EmailSender],
    customCodeExecutionProvider: CustomCodeExecutionProvider
) extends Logging {

  def createExecutionContext(workflowId: Id, nodeId: Id): ExecutionContext =
    ExecutionContext(
      sparkContext,
      sparkSQLSession,
      inferContext,
      executionMode,
      fsClient,
      tempPath,
      libraryPath,
      innerWorkflowExecutor,
      ContextualDataFrameStorage(dataFrameStorage, workflowId, nodeId),
      emailSender,
      ContextualCustomCodeExecutor(customCodeExecutionProvider, workflowId, nodeId)
    )

}

object CommonExecutionContext {

  def apply(context: ExecutionContext): CommonExecutionContext =
    CommonExecutionContext(
      context.sparkContext,
      context.sparkSQLSession,
      context.inferContext,
      context.executionMode,
      context.fsClient,
      context.tempPath,
      context.libraryPath,
      context.innerWorkflowExecutor,
      context.dataFrameStorage.dataFrameStorage,
      context.emailSender,
      context.customCodeExecutor.customCodeExecutionProvider
    )
}

case class ExecutionContext(
    sparkContext: SparkContext,
    sparkSQLSession: SparkSQLSession,
    inferContext: InferContext,
    executionMode: ExecutionMode,
    fsClient: FileSystemClient,
    tempPath: String,
    libraryPath: String,
    innerWorkflowExecutor: InnerWorkflowExecutor,
    dataFrameStorage: ContextualDataFrameStorage,
    emailSender: Option[EmailSender],
    customCodeExecutor: ContextualCustomCodeExecutor
) extends Logging {

  def dataFrameBuilder: DataFrameBuilder = inferContext.dataFrameBuilder

}

case class ContextualDataFrameStorage(dataFrameStorage: DataFrameStorage, workflowId: Id, nodeId: Id) {

  def setInputDataFrame(portNumber: Int, dataFrame: SparkDataFrame) =
    dataFrameStorage.setInputDataFrame(workflowId, nodeId, portNumber, dataFrame)

  def removeNodeInputDataFrames(portNumber: Int) =
    dataFrameStorage.removeNodeInputDataFrames(workflowId, nodeId, portNumber)

  def removeNodeInputDataFrames() =
    dataFrameStorage.removeNodeInputDataFrames(workflowId, nodeId)

  def getOutputDataFrame(portNumber: Int): Option[SparkDataFrame] =
    dataFrameStorage.getOutputDataFrame(workflowId, nodeId, portNumber)

  def setOutputDataFrame(portNumber: Int, dataFrame: SparkDataFrame) =
    dataFrameStorage.setOutputDataFrame(workflowId, nodeId, portNumber, dataFrame)

  def removeNodeOutputDataFrames() =
    dataFrameStorage.removeNodeOutputDataFrames(workflowId, nodeId)

  def withInputDataFrame[T](portNumber: Int, dataFrame: SparkDataFrame)(block: => T): T = {
    setInputDataFrame(portNumber, dataFrame)
    try
      block
    finally
      removeNodeInputDataFrames(portNumber)
  }
}

case class ContextualCustomCodeExecutor(customCodeExecutionProvider: CustomCodeExecutionProvider, workflowId: Id, nodeId: Id) extends Logging {

  def isPythonValid: String => Boolean = customCodeExecutionProvider.pythonCodeExecutor.isValid
  def isRValid: String => Boolean = customCodeExecutionProvider.rCodeExecutor.isValid
  def runPython: String => Result = run(_, customCodeExecutionProvider.pythonCodeExecutor)
  def runR: String => Result = run(_, customCodeExecutionProvider.rCodeExecutor)

  private def run(code: String, executor: CustomCodeExecutor) = {
    val result = customCodeExecutionProvider.actionExecutionDispatcher.executionStarted(workflowId, nodeId)
    executor.run(workflowId.toString, nodeId.toString, code)
    println("Waiting for user's custom action to finish")
    Await.result(result, Duration.Inf)
  }
}
