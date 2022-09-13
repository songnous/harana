package com.harana.workflowexecutor.executor

import akka.actor.ActorSystem
import com.harana.models.json.workflow.exceptions._
import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.client.datasources.DatasourceInMemoryClientFactory
import com.harana.sdk.backend.models.flow.client.datasources.DatasourceTypes.DatasourceList
import com.harana.sdk.backend.models.flow.filesystemclients.{FileInfo, FileSystemClient}
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.shared.models.designer.flow.flows.{WorkflowInfo, WorkflowWithResults, WorkflowWithVariables}
import com.harana.sdk.shared.BuildInfo
import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.designer.flow.{ActionObjectInfo, ExecutionReport}
import com.harana.sdk.shared.models.designer.flow.exceptions.{CyclicGraphError, WorkflowVersionError, WorkflowVersionFormatError, WorkflowVersionNotFoundError, WorkflowVersionNotSupportedError}
import com.harana.sdk.shared.models.designer.flow.json.workflow.WorkflowVersionUtil
import com.harana.sdk.shared.models.designer.flow.utils.Id
import com.harana.spark.AkkaUtils
import com.harana.workflowexecutor.WorkflowExecutorActor.Messages.Launch
import com.harana.workflowexecutor._
import com.harana.workflowexecutor.customcode.CustomCodeEntryPoint
import com.harana.workflowexecutor.exception.{UnexpectedHttpResponseException, WorkflowExecutionException}
import com.harana.workflowexecutor.pyspark.PythonPathGenerator
import com.harana.workflowexecutor.session.storage.DataFrameStorageImpl
import com.typesafe.config.ConfigFactory
import org.apache.spark.SparkContext

import java.io._
import java.net.InetAddress
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.duration._
import scala.io.Source
import scala.util.{Failure, Success, Try}

// WorkflowExecutor creates an execution context and then executes a workflow on Spark.
case class WorkflowExecutor(
    workflow: WorkflowWithVariables,
    customCodeExecutorsPath: String,
    pythonPathGenerator: PythonPathGenerator,
    tempPath: String
) extends Executor {

  val actionObjectCache = mutable.Map[Id, ActionObjectInfo]()

  private val actorSystemName = "WorkflowExecutor"

  def execute(sparkContext: SparkContext) = Try {

    if (workflow.graph.containsCycle) {
      val cyclicGraphException = new CyclicGraphError
      logger.error("WorkflowExecutorActor failed due to incorrect workflow: ", cyclicGraphException)
      throw cyclicGraphException
    }

    val dataFrameStorage = new DataFrameStorageImpl
    val sparkSQLSession = createSparkSQLSession(sparkContext)

    val hostAddress = HostAddressResolver.findHostAddress()
    logger.info("HOST ADDRESS: {}", hostAddress.getHostAddress)

    val pythonBinary = ConfigFactory.load.getString("pythoncaretaker.python-binary-default")

    val actionExecutionDispatcher = new ActionExecutionDispatcher

    val customCodeEntryPoint = new CustomCodeEntryPoint(sparkContext, sparkSQLSession, dataFrameStorage, actionExecutionDispatcher)

    val pythonExecutionCaretaker = new PythonExecutionCaretaker(customCodeExecutorsPath, pythonPathGenerator, pythonBinary, sparkContext, sparkSQLSession, dataFrameStorage, customCodeEntryPoint, hostAddress)
    pythonExecutionCaretaker.start()

    val rExecutionCaretaker = new RExecutionCaretaker(customCodeExecutorsPath, customCodeEntryPoint)
    rExecutionCaretaker.start()

    val customCodeExecutionProvider = CustomCodeExecutionProvider(
      pythonExecutionCaretaker.pythonCodeExecutor,
      rExecutionCaretaker.rCodeExecutor,
      actionExecutionDispatcher
    )

    val libraryPath = "/library"

    val datasources = WorkflowExecutor.datasourcesFrom(workflow)

    val executionContext = createExecutionContext(
      dataFrameStorage = dataFrameStorage,
      executionMode = ExecutionMode.Batch,
      emailSender = None,
      datasourceClientFactory = new DatasourceInMemoryClientFactory(datasources),
      customCodeExecutionProvider = customCodeExecutionProvider,
      sparkContext = sparkContext,
      sparkSQLSession = sparkSQLSession,
      tempPath = tempPath,
      libraryPath = libraryPath
    )

    val actorSystem = ActorSystem(actorSystemName)
    val finishedExecutionStatus = Promise[ExecutionReport]()
    val statusReceiverActor = actorSystem.actorOf(TerminationListenerActor.props(finishedExecutionStatus))

    val workflowWithResults = WorkflowWithResults(
      workflow.id,
      workflow.metadata,
      workflow.graph,
      workflow.thirdPartyData,
      ExecutionReport(Map(), None),
      WorkflowInfo.forId(workflow.id)
    )
    val workflowExecutorActor = actorSystem.actorOf(BatchWorkflowExecutorActor.props(executionContext, statusReceiverActor, workflowWithResults), workflow.id.toString)
    workflowExecutorActor ! Launch(workflow.graph.nodes.map(_.id))

    logger.debug("Awaiting execution end...")
    AkkaUtils.awaitTermination(actorSystem)

    val report: ExecutionReport = finishedExecutionStatus.future.value.get match {
      case Failure(exception) => // WEA failed with an exception
        logger.error("WorkflowExecutorActor failed: ", exception)
        throw exception
      case Success(executionReport: ExecutionReport) =>
        logger.debug(s"WorkflowExecutorActor finished successfully: ${workflow.graph}")
        executionReport
    }

    cleanup(actorSystem, executionContext, pythonExecutionCaretaker)
    report
  }

  private def cleanup(actorSystem: ActorSystem, executionContext: CommonExecutionContext, pythonExecutionCaretaker: PythonExecutionCaretaker) = {
    logger.debug("Cleaning up...")
    pythonExecutionCaretaker.stop()
    logger.debug("PythonExecutionCaretaker terminated!")
    AkkaUtils.terminate(actorSystem)
    logger.debug("Akka terminated!")
    executionContext.sparkContext.stop()
    logger.debug("Spark terminated!")
  }
}

object WorkflowExecutor extends Logging with Executor {

  private val outputFile = "result.json"

  def datasourcesFrom(workflow: WorkflowWithVariables): DatasourceList = {
    val datasourcesJson = workflow.thirdPartyData.fields("datasources")
    val datasourcesJsonString = datasourcesJson.compactPrint
    // FIXME
    //    DatasourceListJsonProtocol.fromString(datasourcesJsonString)
    null
  }

  def runInNoninteractiveMode(params: ExecutionParameters, pythonPathGenerator: PythonPathGenerator): Unit = {
    val sparkContext = createSparkContext()

    val workflowVersionUtil = new WorkflowVersionUtil with Logging {
      override def currentVersion = Version(BuildInfo.apiVersionMajor, BuildInfo.apiVersionMinor, BuildInfo.apiVersionPatch)
    }
    val workflow = loadWorkflow(params, workflowVersionUtil)

    val executionReport = workflow.map { w =>
      WorkflowExecutor(w, params.customCodeExecutorsPath.get, pythonPathGenerator, params.tempPath.get).execute(sparkContext)
    }

    val workflowWithResultsFuture = workflow.flatMap(w =>
      executionReport.map {
        case Success(r) => WorkflowWithResults(w.id, w.metadata, w.graph, w.thirdPartyData, r, WorkflowInfo.forId(w.id))
        case Failure(ex) => logger.error(s"Error while processing workflow: $workflow") ; throw ex
      }
    )

    // Await for workflow execution
    Await.ready(workflowWithResultsFuture, Duration.Inf).value.get match {
      // Workflow execution failed
      case Failure(exception) =>
        exception match {
          case e: WorkflowVersionError => handleVersionException(e)
          case e: DeserializationException => handleDeserializationException(e)
          case e: WorkflowExecutionException => logger.error(e.getMessage, e)
          case e: Exception => logger.error("Unexpected workflow execution exception", e)
        }

      // Workflow execution succeeded
      case Success(workflowWithResults) =>
        logger.info("Handling execution report")
        // Saving execution report to file
        val reportPathFuture: Future[Option[String]] = params.outputDirectoryPath match {
          case None => Future.successful(None)
          case Some(path) => saveWorkflowToFile(path, workflowWithResults, workflowVersionUtil)
        }

        val reportPathTry: Try[Option[String]] =
          try
            Await.ready(reportPathFuture, 1.minute).value.get
          catch {
            case e: Exception =>
              executionReportDump(workflowWithResults, workflowVersionUtil)
              throw e
          }

        if (reportPathTry.isFailure)
          executionReportDump(workflowWithResults, workflowVersionUtil)

        reportPathTry match {
          case Success(None) => // Saving execution report to file was not requested
          case Success(Some(path)) => logger.info(s"Execution report successfully saved to file under path: $path")
          case Failure(exception) =>
            exception match {
              case e: WorkflowVersionError => handleVersionException(e)
              case e: DeserializationException => handleDeserializationException(e)
              case e: UnexpectedHttpResponseException => logger.error(e.getMessage)
              case e: Exception => logger.error("Saving execution report to file failed", e)
            }
        }
    }
  }

  private def executionReportDump(workflowWithResults: WorkflowWithResults, workflowVersionUtil: WorkflowVersionUtil) = {
    import workflowVersionUtil._
    logger.error("Execution report dump: \n" + workflowWithResults.asJson.prettyPrint)
  }

  private def handleVersionException(versionException: WorkflowVersionError) =
    versionException match {
      case e @ WorkflowVersionFormatError(stringVersion) => logger.error(e.getMessage)
      case WorkflowVersionNotFoundError(supportedApiVersion) => logger.error("The input workflow does not contain version identifier. Unable to proceed...")
      case WorkflowVersionNotSupportedError(workflowApiVersion, supportedApiVersion) =>
        logger.error(
          "The input workflow is incompatible with this WorkflowExecutor. " +
            s"Workflow's version is '${workflowApiVersion.humanReadable}' but " +
            s"WorkflowExecutor's version is '${supportedApiVersion.humanReadable}'."
        )
    }

  private def handleDeserializationException(exception: DeserializationException): Unit =
    logger.error(s"WorkflowExecutor is unable to parse the input file: ${exception.getMessage}")

  private def loadWorkflow(params: ExecutionParameters, workflowVersionUtil: WorkflowVersionUtil) =
    Future(Source.fromFile(params.workflowFilename.get).mkString)
      .map(_.parseJson)
      .map(w => WorkflowJsonParametersOverrider.overrideParameters(w, params.extraVars))
      .map(_.as[WorkflowWithVariables](workflowVersionUtil.versionedWorkflowWithVariablesReader))

  private def saveWorkflowToFile(outputDir: String, result: WorkflowWithResults, workflowVersionUtil: WorkflowVersionUtil) = {
    import workflowVersionUtil._
    logger.info(s"Execution report file ($outputFile) will be written on host: ${InetAddress.getLocalHost.getHostName} (${InetAddress.getLocalHost.getHostAddress})")
    var writerOption: Option[PrintWriter] = None
    try {
      val resultsFile = new File(outputDir, outputFile)
      val parentFile  = resultsFile.getParentFile
      if (parentFile != null) parentFile.mkdirs()

      logger.info(s"Writing execution report file to: ${resultsFile.getPath}")
      writerOption = Some(new PrintWriter(new FileWriter(resultsFile, false)))
      writerOption.get.write(result.asJson.prettyPrint)
      writerOption.get.flush()
      writerOption.get.close()

      Future.successful(Some(resultsFile.getPath))
    } catch {
      case e: Exception =>
        writerOption.foreach { writer =>
          try
            writer.close()
          catch {
            case e: Exception =>
              logger.warn("Exception during emergency closing of PrintWriter", e)
          }
        }
        Future.failed(e)
    }
  }
}

private case class FileSystemClientStub() extends FileSystemClient {
  override def copyLocalFile[T <: Serializable](localFilePath: String, remoteFilePath: String): Unit = ()
  override def delete(path: String): Unit = ()
  override def saveObjectToFile[T <: Serializable](path: String, instance: T): Unit = ()
  override def fileExists(path: String): Boolean = throw new UnsupportedOperationException()
  override def saveInputStreamToFile(inputStream: InputStream, destinationPath: String): Unit = ()
  override def getFileInfo(path: String): Option[FileInfo] = throw new UnsupportedOperationException
  override def readFileAsObject[T <: Serializable](path: String): T = throw new UnsupportedOperationException
}
