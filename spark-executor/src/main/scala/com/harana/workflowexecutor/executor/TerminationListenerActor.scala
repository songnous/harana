package com.harana.workflowexecutor.executor

import scala.concurrent.Promise
import akka.actor.Actor
import akka.actor.Props
import com.harana.sdk.shared.models.designer.flow.ExecutionReport
import com.harana.spark.AkkaUtils

class TerminationListenerActor(finishedExecutionStatus: Promise[ExecutionReport]) extends Actor {

  override def receive: Receive = { case status: ExecutionReport =>
    finishedExecutionStatus.success(status)
    AkkaUtils.terminate(context.system)
  }

}

object TerminationListenerActor {

  def props(finishedExecutionReport: Promise[ExecutionReport]) =
    Props(new TerminationListenerActor(finishedExecutionReport))

}