package com.harana.sdk.shared.models.flow.graph

import com.harana.sdk.shared.models.flow.exceptions.FailureDescription

sealed abstract class GraphState(name: String) extends Serializable {
  def isDraft = false
  def isRunning = false
  def isCompleted = false
  def isAborted = false
  def isFailed = false
}

object GraphState {

  case object Draft extends GraphState(Names.draft) {
    override def isDraft = true
  }

  case object Running extends GraphState(Names.running) {
    override def isRunning = true
  }

  case object Completed extends GraphState(Names.completed) {
    override def isCompleted = true
  }

  case object Aborted extends GraphState(Names.aborted) {
    override def isAborted = true
  }

  case class Failed(error: FailureDescription) extends GraphState(Names.failed) {
    override def isFailed = true
  }

  def fromString: PartialFunction[String, GraphState] = {
    case Names.draft     => Draft
    case Names.completed => Completed
    case Names.aborted   => Aborted
    case Names.running   => Running
  }

  def failedFromString(error: => FailureDescription): PartialFunction[String, GraphState] = { case Names.failed =>
    Failed(error)
  }

  def fromString(error: => FailureDescription): PartialFunction[String, GraphState] =
    fromString.orElse(failedFromString(error))

  object Names {
    val draft = "DRAFT"
    val running = "RUNNING"
    val completed = "COMPLETED"
    val aborted = "ABORTED"
    val failed = "FAILED"
  }
}
