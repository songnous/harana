package com.harana.workflowexecutor.pythongateway

import com.harana.sdk.backend.models.flow.utils.Logging
import java.net.InetAddress
import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec
import scala.concurrent.duration._

import org.apache.spark.SparkContext
import py4j._
import com.harana.sdk.backend.models.flow._
import com.harana.spark.SparkSQLSession
import com.harana.workflowexecutor.customcode.CustomCodeEntryPoint
import com.harana.workflowexecutor.pythongateway.PythonGateway.GatewayConfig

case class PythonGateway(gatewayConfig: GatewayConfig,
                         sparkContext: SparkContext,
                         sparkSQLSession: SparkSQLSession,
                         dataFrameStorage: DataFrameStorage,
                         pythonEntryPoint: CustomCodeEntryPoint,
                         hostAddress: InetAddress) extends Logging {

  import PythonGateway._

  private val gatewayStateListener = new GatewayEventListener
  val gatewayServer = createGatewayServer(pythonEntryPoint, gatewayStateListener)

  def start() = gatewayServer.start()
  def stop() = gatewayServer.shutdown()

  def codeExecutor =
    pythonEntryPoint.getCodeExecutor(gatewayConfig.pyExecutorSetupTimeout)

  def listeningPort =
    (gatewayServer.getListeningPort, gatewayStateListener.running) match {
      case (-1, _)    => None
      case (_, false) => None
      case (p, true)  => Some(p)
    }

  private def createGatewayServer(entryPoint: CustomCodeEntryPoint, listener: GatewayEventListener) = {
    val callbackClient = new LazyCallbackClient(() => entryPoint.getPythonPort(gatewayConfig.pyExecutorSetupTimeout), hostAddress)

    // It is quite important that these values are 0, which translates to infinite timeout.
    // Non-zero values might lead to the server shutting down unexpectedly.
    val connectTimeout = 0
    val readTimeout    = 0
    val port           = 0 // Use a random available port.

    val gateway = GatewayServerFactory.create(entryPoint, port, connectTimeout, readTimeout, callbackClient, hostAddress)
    gateway.addListener(listener)
    gateway
  }
}

object PythonGateway {

  /** A wrapper around Py4j's CallbackClient that instantiates the actual CallbackClient lazily, and re-instantiates it
    * every time the callback port changes.
    *
    * This way we don't have to know Python's listening port at the time of Gateway instantiation and are prepared for
    * restarting the callback server.
    */
  class LazyCallbackClient(val getCallbackPort: () => Int, val host: InetAddress) extends CallbackClient(0, host) {

    private val clientRef = new AtomicReference(new CallbackClient(0, host))

    override def sendCommand(command: String): String = {
      @tailrec
      def updateAndGet(): CallbackClient = {
        val port = getCallbackPort()
        val currentClient = clientRef.get()

        if (currentClient.getPort == port)
          currentClient
        else {
          val newClient = new CallbackClient(port, host)
          if (clientRef.compareAndSet(currentClient, newClient)) {
            currentClient.shutdown()
            newClient
          } else
            updateAndGet()
        }
      }
      updateAndGet().sendCommand(command)
    }

    override def shutdown(): Unit = {
      clientRef.get.shutdown()
      super.shutdown()
    }
  }

  class GatewayEventListener extends DefaultGatewayServerListener with Logging {
    var running: Boolean = true

    override def serverStopped() = {
      logger.info("Gateway server stopped")
      running = false
    }
  }

  case class GatewayConfig(pyExecutorSetupTimeout: Duration = 30.seconds)
}
