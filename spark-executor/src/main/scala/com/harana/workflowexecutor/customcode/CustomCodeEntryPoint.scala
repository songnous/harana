package com.harana.workflowexecutor.customcode

import com.harana.sdk.backend.models.flow.utils.Logging
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.Promise

import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.sql.DataFrame
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import com.harana.sdk.backend.models.flow._
import com.harana.spark.SparkSQLSession

/** An entry point to our application designed to be accessible by custom code processes. */
class CustomCodeEntryPoint(
    val sparkContext: SparkContext,
    val sparkSQLSession: SparkSQLSession,
    val dataFrameStorage: DataFrameStorage,
    val actionExecutionDispatcher: ActionExecutionDispatcher
) extends Logging {

  import com.harana.workflowexecutor.customcode.CustomCodeEntryPoint._

  def getSparkContext: JavaSparkContext = sparkContext
  def getSparkSQLSession: SparkSQLSession = sparkSQLSession
  def getNewSparkSQLSession: SparkSQLSession = sparkSQLSession.newSession()
  def getSparkConf: SparkConf = sparkContext.getConf

  private val codeExecutor: AtomicReference[Promise[CustomCodeExecutor]] = new AtomicReference(Promise())
  private val pythonPort: AtomicReference[Promise[Int]] = new AtomicReference(Promise())

  def getCodeExecutor(timeout: Duration) = getFromPromise(codeExecutor.get, timeout)
  def getPythonPort(timeout: Duration): Int = getFromPromise(pythonPort.get, timeout)

  def registerCodeExecutor(newCodeExecutor: CustomCodeExecutor): Unit = replacePromise(codeExecutor, newCodeExecutor)
  def registerCallbackServerPort(newPort: Int): Unit = replacePromise(pythonPort, newPort)

  def retrieveInputDataFrame(workflowId: String, nodeId: String, portNumber: Int) = dataFrameStorage.getInputDataFrame(workflowId, nodeId, portNumber).get
  def retrieveOutputDataFrame(workflowId: String, nodeId: String, portNumber: Int) = dataFrameStorage.getOutputDataFrame(workflowId, nodeId, portNumber).get
  def registerOutputDataFrame(workflowId: String, nodeId: String, portNumber: Int, dataFrame: DataFrame) =dataFrameStorage.setOutputDataFrame(workflowId, nodeId, portNumber, dataFrame)

  def executionCompleted(workflowId: String, nodeId: String): Unit = actionExecutionDispatcher.executionEnded(workflowId, nodeId, Right(()))
  def executionFailed(workflowId: String, nodeId: String, error: String): Unit = actionExecutionDispatcher.executionEnded(workflowId, nodeId, Left(error))
}

object CustomCodeEntryPoint {
  private case class PromiseReplacedException() extends Exception

  @tailrec
  private def getFromPromise[T](promise: => Promise[T], timeout: Duration): T = {
    try
      Await.result(promise.future, timeout)
    catch {
      case e: TimeoutException => throw e
      case e: PromiseReplacedException => getFromPromise(promise, timeout)
    }
  }

  private def replacePromise[T](promise: AtomicReference[Promise[T]], newValue: T) =
    try
      promise.getAndSet(Promise.successful(newValue)).failure(new PromiseReplacedException)
    catch {
      // The oldPromise will have been completed always, except for the first time.
      // The illegal state is expected, but we have to complete the oldPromise, since someone might be waiting on it.
      case e: IllegalStateException => ()
    }

  case class CustomCodeEntryPointConfig(pyExecutorSetupTimeout: Duration = 5.seconds)
}