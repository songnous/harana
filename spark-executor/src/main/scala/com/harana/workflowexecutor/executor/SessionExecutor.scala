package com.harana.workflowexecutor.executor

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing._
import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.client.datasources.DatasourceRestClientFactory
import com.harana.sdk.backend.models.flow.mail.{EmailSender, EmailSenderAuthorizationConfig, EmailSenderConfig}
import com.harana.sdk.shared.models.designer.flow.catalogs.{ActionObjectCatalog, FlowCatalog}
import com.harana.sdk.shared.models.designer.flow.flows.Workflow
import com.harana.spark.{AkkaUtils, SparkSQLSession}
import com.harana.workflowexecutor.WorkflowExecutorActor.Messages.Init
import com.harana.workflowexecutor._
import com.harana.workflowexecutor.communication.mq.serialization.json.Global.{GlobalMQDeserializer, GlobalMQSerializer}
import com.harana.workflowexecutor.communication.mq.serialization.json.{MQCommunication, ProtocolJsonDeserializer, ProtocolJsonSerializer}
import com.harana.workflowexecutor.customcode.CustomCodeEntryPoint
import com.harana.workflowexecutor.notebooks.KernelManagerCaretaker
import com.harana.workflowexecutor.pyspark.PythonPathGenerator
import com.harana.workflowexecutor.rabbitmq._
import com.harana.workflowexecutor.session.storage.DataFrameStorageImpl
import com.newmotion.akka.rabbitmq.ConnectionActor
import com.rabbitmq.client.ConnectionFactory
import com.typesafe.config.ConfigFactory
import org.apache.spark.SparkContext

import java.net.{InetAddress, URL}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.reflect.io.Path

// SessionExecutor waits for user instructions in an infinite loop.
case class SessionExecutor(
    messageQueueHost: String,
    messageQueuePort: Int,
    messageQueueUser: String,
    messageQueuePass: String,
    workflowId: String,
    wmAddress: String,
    wmUsername: String,
    wmPassword: String,
    mailServerHost: String,
    mailServerPort: Int,
    mailServerUser: String,
    mailServerPassword: String,
    mailServerSender: String,
    notebookServerAddress: URL,
    datasourceServerAddress: URL,
    depsZip: String,
    workflowOwnerId: String,
    tempPath: String,
    pythonBinaryPath: Option[String]
) extends Executor {

  private val workflowIdObject = Workflow.Id.fromString(workflowId)
  private val config = ConfigFactory.load
  private val subscriptionTimeout = config.getInt("subscription-timeout").seconds
  private val keepAliveInterval = config.getInt("keep-alive.interval").seconds
  private val heartbeatInterval = config.getInt("heartbeat.interval").seconds
  private val workflowManagerTimeout = config.getInt("workflow-manager.timeout")
  private val wmWorkflowsPath = config.getString("workflow-manager.workflows.path")
  private val wmReportsPath = config.getString("workflow-manager.reports.path")

  val FlowCatalog(_, actionObjectCatalog, actionsCatalog) = CatalogRecorder.resourcesCatalogRecorder.catalogs

  /** WARNING: Performs an infinite loop. */
  def execute(): Unit = {
    logger.info(s"SessionExecutor for '$workflowId' starts...")
    val sparkContext = createSparkContext()
    val sparkSQLSession = createSparkSQLSession(sparkContext)
    val dataFrameStorage = new DataFrameStorageImpl

    val hostAddress: InetAddress = HostAddressResolver.findHostAddress()
    logger.info("Host address: {}", hostAddress.getHostAddress)

    val tempPath = Unzip.unzipAll(depsZip)

    val pythonPathGenerator =
      Option(System.getenv("SPARK_HOME"))
        .map(sparkHome => Path(sparkHome)./("python").toAbsolute.toString())
        .map(new PythonPathGenerator(_))
        .getOrElse(throw new RuntimeException("Could not find PySpark!"))

    val pythonBinary = pythonBinaryPath.getOrElse(ConfigFactory.load.getString("pythoncaretaker.python-binary-default"))

    val actionExecutionDispatcher = new ActionExecutionDispatcher

    val customCodeEntryPoint = new CustomCodeEntryPoint(sparkContext, sparkSQLSession, dataFrameStorage, actionExecutionDispatcher)

    val pythonExecutionCaretaker = new PythonExecutionCaretaker(
      s"$tempPath/pyexecutor/pyexecutor.py",
      pythonPathGenerator,
      pythonBinary,
      sparkContext,
      sparkSQLSession,
      dataFrameStorage,
      customCodeEntryPoint,
      hostAddress
    )
    pythonExecutionCaretaker.start()

    val rExecutionCaretaker = new RExecutionCaretaker(s"$tempPath/r_executor.R", customCodeEntryPoint)
    rExecutionCaretaker.start()

    val customCodeExecutionProvider = CustomCodeExecutionProvider(
      pythonExecutionCaretaker.pythonCodeExecutor,
      rExecutionCaretaker.rCodeExecutor,
      actionExecutionDispatcher
    )

    implicit val system = ActorSystem()

    val workflowManagerClientActor = system.actorOf(WorkflowManagerClientActor.props(workflowOwnerId, wmUsername, wmPassword, wmAddress, wmWorkflowsPath, wmReportsPath))
    val communicationFactory = createCommunicationFactory(system)
    val workflowsSubscriberActor = createWorkflowsSubscriberActor(sparkContext, sparkSQLSession, actionObjectCatalog, dataFrameStorage, customCodeExecutionProvider, system, workflowManagerClientActor, communicationFactory)
    val workflowsSubscriberReady = communicationFactory.registerSubscriber(MQCommunication.Topic.allWorkflowsSubscriptionTopic(workflowId), workflowsSubscriberActor)

    waitUntilSubscribersAreReady(Seq(workflowsSubscriberReady))

    val kernelManagerCaretaker = new KernelManagerCaretaker(
      system,
      pythonBinary,
      pythonPathGenerator,
      communicationFactory,
      tempPath,
      hostAddress.getHostAddress,
      pythonExecutionCaretaker.gatewayListeningPort.get,
      hostAddress.getHostAddress,
      rExecutionCaretaker.backendListeningPort,
      messageQueueHost,
      messageQueuePort,
      messageQueueUser,
      messageQueuePass,
      // TODO: Currently sessionId == workflowId
      workflowId,
      workflowIdObject
    )

    kernelManagerCaretaker.start()

    logger.info(s"Sending Init() to WorkflowsSubscriberActor")
    workflowsSubscriberActor ! Init()

    AkkaUtils.awaitTermination(system)
    cleanup(system, sparkContext, pythonExecutionCaretaker, kernelManagerCaretaker)
    logger.debug("SessionExecutor ends")
    System.exit(0)
  }

  private def createWorkflowsSubscriberActor(sparkContext: SparkContext,
                                             sparkSQLSession: SparkSQLSession,
                                             actionObjectCatalog: ActionObjectCatalog,
                                             dataFrameStorage: DataFrameStorageImpl,
                                             customCodeExecutionProvider: CustomCodeExecutionProvider,
                                             system: ActorSystem,
                                             workflowManagerClientActor: ActorRef,
                                             communicationFactory: MQCommunicationFactory
  ): ActorRef = {

    def createHeartbeatPublisher: ActorRef = {
      val haranaPublisher = communicationFactory.createPublisher(
        // TODO: Currently sessionId == workflowId
        MQCommunication.Topic.haranaPublicationTopic(workflowId),
        MQCommunication.Actor.Publisher.harana
      )

      val heartbeatWorkflowBroadcaster = communicationFactory.createBroadcaster(
        MQCommunication.Exchange.heartbeats(workflowIdObject),
        MQCommunication.Actor.Publisher.heartbeat(workflowIdObject)
      )

      val heartbeatAllBroadcaster = communicationFactory.createBroadcaster(
        MQCommunication.Exchange.heartbeatsAll,
        MQCommunication.Actor.Publisher.heartbeatAll
      )

      val routePaths = scala.collection.immutable
        .Iterable(haranaPublisher, heartbeatWorkflowBroadcaster, heartbeatAllBroadcaster)
        .map(_.path.toString)

      system.actorOf(Props.empty.withRouter(BroadcastGroup(routePaths)), "heartbeatBroadcastingRouter")
    }

    val heartbeatPublisher: ActorRef = createHeartbeatPublisher

    val emailSender = {
      val emailSenderConfig = EmailSenderConfig(
        smtpHost = mailServerHost,
        smtpPort = mailServerPort,
        from = mailServerSender,
        authorizationConfig = Some(EmailSenderAuthorizationConfig(user = mailServerUser, password = mailServerPassword))
      )
      EmailSender(emailSenderConfig)
    }

    // TODO There might be need to have it passed to we.jar as argument eventually
    val libraryPath = "/library"

    val executionContext = createExecutionContext(
      dataFrameStorage = dataFrameStorage,
      executionMode = ExecutionMode.Interactive,
      emailSender = Some(emailSender),
      datasourceClientFactory = new DatasourceRestClientFactory(datasourceServerAddress, workflowOwnerId),
      customCodeExecutionProvider = customCodeExecutionProvider,
      sparkContext = sparkContext,
      sparkSQLSession = sparkSQLSession,
      tempPath = tempPath,
      libraryPath = libraryPath,
      actionObjectCatalog = Some(actionObjectCatalog)
    )

    val readyBroadcaster = communicationFactory.createBroadcaster(
      MQCommunication.Exchange.ready(workflowIdObject),
      MQCommunication.Actor.Publisher.ready(workflowIdObject)
    )

    val publisher: ActorRef = communicationFactory.createPublisher(
      // TODO: Currently sessionId == workflowId
      MQCommunication.Topic.workflowPublicationTopic(workflowIdObject, workflowId),
      MQCommunication.Actor.Publisher.workflow(workflowIdObject)
    )

    val actorProvider = new SessionWorkflowExecutorActorProvider(executionContext, workflowManagerClientActor,
      heartbeatPublisher, readyBroadcaster, workflowManagerTimeout, publisher,
      // TODO: Currently sessionId == workflowId
      workflowId, heartbeatInterval)

    val workflowsSubscriberActor = system.actorOf(
      WorkflowTopicSubscriber.props(
        actorProvider,
        // TODO: Currently sessionId == workflowId
        workflowId,
        workflowIdObject
      ),
      MQCommunication.Actor.Subscriber.workflows
    )

    workflowsSubscriberActor
  }

  private def createCommunicationFactory(system: ActorSystem): MQCommunicationFactory = {
    val connection = createConnection(system)
    val messageDeserializer = ProtocolJsonDeserializer().orElse(GlobalMQDeserializer)
    val messageSerializer  = ProtocolJsonSerializer().orElse(GlobalMQSerializer)
    MQCommunicationFactory(system, connection, messageSerializer, messageDeserializer)
  }

  private def createConnection(system: ActorSystem): ActorRef = {
    val factory = new ConnectionFactory()
    factory.setHost(messageQueueHost)
    factory.setPort(messageQueuePort)
    factory.setUsername(messageQueueUser)
    factory.setPassword(messageQueuePass)
    system.actorOf(ConnectionActor.props(factory), MQCommunication.mqActorSystemName)
  }

  // Clients after receiving ready or heartbeat will assume that we are listening for their response
  private def waitUntilSubscribersAreReady[T](subscribers: Seq[Future[T]]): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val subscribed: Future[Seq[T]] = Future.sequence(subscribers)
    logger.info("Waiting for subscribers...")
    Await.result(subscribed, subscriptionTimeout)
    logger.info("Subscribers READY!")
  }

  private def cleanup(system: ActorSystem, sparkContext: SparkContext, pythonExecutionCaretaker: PythonExecutionCaretaker, kernelManagerCaretaker: KernelManagerCaretaker) = {
    logger.debug("Cleaning up...")
    pythonExecutionCaretaker.stop()
    kernelManagerCaretaker.stop()
    sparkContext.stop()
    logger.debug("Spark terminated!")
    AkkaUtils.terminate(system)
    logger.debug("Akka terminated!")
  }
}