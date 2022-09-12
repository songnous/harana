package com.harana.workflowexecutor

import scala.util.control.NonFatal
import akka.actor.Actor
import akka.actor.PoisonPill
import com.harana.sdk.backend.models.designer.flow.utils.Logging
import com.harana.sdk.backend.models.designer.flow._
import com.harana.sdk.backend.models.designer.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.designer.flow.graph.FlowGraph.FlowNode
import com.harana.sdk.shared.models.designer.flow.ActionObjectInfo
import com.harana.sdk.shared.models.designer.flow.report.{ReportContent, ReportType}
import com.harana.sdk.shared.models.designer.flow.utils.{Entity, Id}
import com.harana.workflowexecutor.WorkflowExecutorActor.Messages.NodeCompleted
import com.harana.workflowexecutor.WorkflowExecutorActor.Messages.NodeFailed
import com.harana.workflowexecutor.WorkflowExecutorActor.Messages.NodeStarted

/** WorkflowNodeExecutorActor is responsible for execution of single node. Sends NodeStarted at the beginning and
  * NodeCompleted or NodeFailed at the end of execution depending on whether the execution succeeded or failed.
  */
class WorkflowNodeExecutorActor(executionContext: ExecutionContext, node: FlowNode, input: Vector[ActionObjectInfo]) extends Actor with Logging {

  import com.harana.workflowexecutor.WorkflowNodeExecutorActor.Messages._

  val nodeDescription = s"'${node.value.name}-${node.id}'"
  var executionStart: Long = _

  override def receive: Receive = {
    case Start() =>
      executionStart = System.currentTimeMillis()
      logger.info(s"Starting execution of node $nodeDescription")
      sendStarted()
      try {
        asSparkJobGroup {
          val resultVector = executeAction()
          val nodeExecutionResults = nodeExecutionResultsFrom(resultVector)
          sendCompleted(nodeExecutionResults)
        }
      } catch {
        case SparkExceptionAsDeeplangException(flowEx) => sendFailed(flowEx)
        case e: Exception => sendFailed(e)
        case NonFatal(e) => sendFailed(new RuntimeException(e))
        case fatal: Throwable =>
          logger.error(s"FATAL ERROR. MSG: ${fatal.getMessage}", fatal)
          fatal.printStackTrace()
          throw fatal
      } finally {
        // Exception thrown here could result in slightly delayed graph execution
        val duration = (System.currentTimeMillis() - executionStart) / 1000.0
        logger.info(s"Ending execution of node $nodeDescription (duration: $duration seconds)")
        self ! PoisonPill
      }
    case Delete() =>
      val storage = executionContext.dataFrameStorage
      storage.removeNodeOutputDataFrames()
      storage.removeNodeInputDataFrames()
  }

  def sendFailed(e: Exception): Unit = {
    logger.error(s"Workflow execution failed in node with id=${node.id}.", e)
    sender ! NodeFailed(node.id, e)
  }

  def sendCompleted(nodeExecutionResults: NodeExecutionResults): Unit = {
    val nodeCompleted = NodeCompleted(node.id, nodeExecutionResults)
    logger.debug(s"Node completed: ${nodeExecutionResults.actionObjects}")
    sender ! nodeCompleted
  }

  def sendStarted(): Unit = {
    val nodeStarted = NodeStarted(node.id)
    sender ! nodeStarted
    logger.debug(s"Sending $nodeStarted.")
  }

  def nodeExecutionResultsFrom(actionResults: Vector[ActionObjectInfo]): NodeExecutionResults = {
    val registeredResults: Seq[(Id, ActionObjectInfo)]  = registerResults(actionResults)
    val registeredResultsMap: Map[Id, ActionObjectInfo] = registeredResults.toMap
    val reports: Map[Id, ReportContent]          = collectReports(registeredResultsMap)
    NodeExecutionResults(registeredResults.map(_._1), reports, registeredResultsMap)
  }

  private def registerResults(actionObjects: Seq[ActionObjectInfo]): Seq[(Id, ActionObjectInfo)] = {
    logger.debug(s"Registering data from action output ports in node ${node.id}")
    val results: Seq[(Id, ActionObjectInfo)] = actionObjects.map(actionObject => (Entity.Id.randomId, actionObject))
    logger.debug(s"Data registered for $nodeDescription: results=$results")
    results
  }

  def collectReports(results: Map[Id, ActionObjectInfo]): Map[Id, ReportContent] = {
    logger.debug(s"Collecting reports for ${node.id}")
    results.map { case (id, actionObject) =>
      (id, actionObject.report(extended = node.value.getReportType == Action.ReportParameter.Extended()).content)
    }
  }

  def executeAction(): Vector[ActionObjectInfo] = {
    logger.debug(s"$nodeDescription inputVector.size = ${input.size}")
    val inputKnowledge = input.map(actionObject => Knowledge(actionObject))
    // if inference throws, we do not perform execution
    node.value.inferKnowledgeUntyped(inputKnowledge)(executionContext.inferContext)

    val resultVector = node.value.executeUntyped(input)(executionContext)

    resultVector.zipWithIndex.foreach {
      case (dataFrame: DataFrame, portNumber: Int) => executionContext.dataFrameStorage.setOutputDataFrame(portNumber, dataFrame.sparkDataFrame)
      case (_, _) => ()
    }

    logger.debug(s"$nodeDescription executed (without reports): $resultVector")
    resultVector
  }

  private def asSparkJobGroup[T](sparkCode: => T): T = try {
    // interrupt on cancel = false because of HDFS-1208
    executionContext.sparkContext.setJobGroup(node.id.toString, s"Execution of node id=${node.id.toString}", interruptOnCancel = false)
    sparkCode
  } finally
    // clear job group, because this thread will be used by other actors
    executionContext.sparkContext.clearJobGroup()

}

object WorkflowNodeExecutorActor {
  object Messages {
    sealed trait Message
    case class Start()  extends Message
    case class Delete() extends Message
  }
}