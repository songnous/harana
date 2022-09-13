package com.harana.workflowexecutor

import akka.actor.{Actor, Props}
import akka.pattern.pipe
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.shared.models.designer.flow.ExecutionReport
import com.harana.sdk.shared.models.designer.flow.flows.WorkflowWithResults
import com.harana.sdk.shared.models.designer.flow.utils.Id
import com.harana.workflowexecutor.WorkflowManagerClientActorProtocol.{GetWorkflow, Request, SaveState, SaveWorkflow}

import scala.concurrent.Future

class WorkflowManagerClientActor(
    val workflowOwnerId: String,
    val wmUsername: String,
    val wmPassword: String,
    val workflowApiAddress: String,
    val workflowApiPrefix: String,
    val reportsApiPrefix: String
) extends Actor with Logging {

  import context.dispatcher

  private val HaranaUserIdHeaderName = "X-Harana-UserId"

  override def receive: Receive = {
    case r: Request =>
      r match {
        case GetWorkflow(workflowId) => getWorkflow(workflowId).pipeTo(sender())
        case SaveWorkflow(workflow) => saveWorkflowWithState(workflow).pipeTo(sender())
        case SaveState(workflowId, state) => saveState(workflowId, state).pipeTo(sender())
      }
    case message => unhandled(message)
  }

  private val downloadWorkflowUrl = (workflowId: Id) => s"$workflowApiAddress/$workflowApiPrefix/$workflowId"
  private val saveWorkflowWithStateUrl = (workflowId: Id) => s"$workflowApiAddress/$workflowApiPrefix/$workflowId"
  private val saveStateUrl = (workflowId: Id) => s"$workflowApiAddress/$reportsApiPrefix/$workflowId"

  private def getWorkflow(workflowId: Id) = {
    // FIXME
    Future.unit
  }

  private def saveWorkflowWithState(workflow: WorkflowWithResults) = {
    // FIXME
    Future.unit
  }

  private def saveState(workflowId: Id, state: ExecutionReport) = {
  // FIXME
    Future.unit
  }
}

object WorkflowManagerClientActor {

  def props(
      workflowOwnerId: String,
      wmUsername: String,
      wmPassword: String,
      workflowApiAddress: String,
      workflowApiPrefix: String,
      reportsApiPrefix: String
  ) =
    Props(
      new WorkflowManagerClientActor(
        workflowOwnerId,
        wmUsername: String,
        wmPassword: String,
        workflowApiAddress,
        workflowApiPrefix,
        reportsApiPrefix
      )
    )
}

object WorkflowManagerClientActorProtocol {
  sealed trait Request
  case class GetWorkflow(workflowId: Id) extends Request
  case class SaveWorkflow(workflow: WorkflowWithResults) extends Request
  case class SaveState(workflowId: Id, state: ExecutionReport) extends Request
}
