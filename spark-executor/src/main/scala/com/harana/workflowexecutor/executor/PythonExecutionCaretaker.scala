package com.harana.workflowexecutor.executor

import com.harana.sdk.backend.models.flow.utils.Logging
import java.net.InetAddress
import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec
import scala.sys.process._

import org.apache.spark.SparkContext
import com.harana.sdk.backend.models.flow.CustomCodeExecutor
import com.harana.sdk.backend.models.flow.DataFrameStorage
import com.harana.spark.SparkSQLSession
import com.harana.workflowexecutor.Unzip
import com.harana.workflowexecutor.customcode.CustomCodeEntryPoint
import com.harana.workflowexecutor.pyspark.PythonPathGenerator
import com.harana.workflowexecutor.pythongateway.PythonGateway
import com.harana.workflowexecutor.pythongateway.PythonGateway.GatewayConfig

// This object is responsible for a companion Python process, that executes user-defined custom actions.
// It starts PyExecutor and takes care that it's restarted if it dies for any reason. Also, PyExecutor is killed when the JVM process dies.
// Another of its functions is to provide a facade for everything Python/UDF-related.

class PythonExecutionCaretaker(
    pythonExecutorPath: String,
    pythonPathGenerator: PythonPathGenerator,
    pythonBinary: String,
    val sparkContext: SparkContext,
    val sparkSQLSession: SparkSQLSession,
    val dataFrameStorage: DataFrameStorage,
    val customCodeEntryPoint: CustomCodeEntryPoint,
    val hostAddress: InetAddress
) extends Logging {

  def waitForPythonExecutor() = pythonGateway.codeExecutor

  def start() = {
    sys.addShutdownHook {
      pythonGateway.stop()
      destroyPyExecutorProcess()
    }

    pythonGateway.start()
    pyExecutorMonitorThread.start()

    try
      waitForPythonExecutor()
    catch {
      case e: Exception =>
        stop()
        throw e
    }
  }

  def stop() = {
    pythonGateway.stop()
    destroyPyExecutorProcess()
    pyExecutorMonitorThread.join()
  }

  def pythonCodeExecutor = pythonGateway.codeExecutor

  def gatewayListeningPort = pythonGateway.listeningPort

  private val pythonGateway = PythonGateway(GatewayConfig(), sparkContext, sparkSQLSession, dataFrameStorage, customCodeEntryPoint, hostAddress)
  private val pyExecutorProcess = new AtomicReference[Option[Process]](None)

  private def extractPyExecutor() =
    if (pythonExecutorPath.endsWith(".jar")) {
      val tempDir = Unzip.unzipToTmp(pythonExecutorPath, _.startsWith("pyexecutor/"))
      s"$tempDir/pyexecutor/pyexecutor.py"
    } else
      pythonExecutorPath

  private def runPyExecutor(gatewayPort: Int, pythonExecutorPath: String) = {
    logger.info(s"Initializing PyExecutor from: $pythonExecutorPath")
    val command = s"$pythonBinary $pythonExecutorPath " + s"--gateway-address ${hostAddress.getHostAddress}:$gatewayPort"
    logger.info(s"Starting a new PyExecutor process: $command")

    val pyLogger = ProcessLogger(fout = logger.error, ferr = logger.error)
    val processBuilder = Process(command, None, pythonPathGenerator.env())
    processBuilder.run(pyLogger)
  }

  // This thread starts PyExecutor in a loop as long as gateway's listening port is available.
  private val pyExecutorMonitorThread = new Thread(() => {
    val extractedPythonExecutorPath = extractPyExecutor()

    @tailrec
    def go(): Unit = {
      pythonGateway.listeningPort match {
        case None => logger.info("Listening port unavailable, not running PyExecutor")
        case Some(port) =>
          val process = runPyExecutor(port, extractedPythonExecutorPath)
          pyExecutorProcess.set(Some(process))
          val exitCode = process.exitValue()
          pyExecutorProcess.set(None)
          logger.info(s"PyExecutor exited with code $exitCode")
          Thread.sleep(250)
          go()
      }
    }
    go()
  })

  private def destroyPyExecutorProcess() = pyExecutorProcess.get.foreach(_.destroy())
}