package com.harana.sdk.shared.models.flow.graph.node

import NodeStatus.{Aborted, Completed, Draft, Failed, Queued, Running}
import com.harana.sdk.shared.models.flow.exceptions.FailureDescription
import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
sealed abstract class NodeStatus(val name: NodeStatusName, val results: Seq[Id]) extends Serializable {

  def start: Running
  def finish(results: Seq[Id]): Completed
  def fail(error: FailureDescription): Failed
  def abort: Aborted
  def enqueue: Queued

  def isDraft = this match {
    case Draft(_) => true
    case _        => false
  }

  def isQueued = this match {
    case Queued(_) => true
    case _         => false
  }

  def isRunning = this match {
    case Running(_, _) => true
    case _             => false
  }

  def isCompleted = this match {
    case Completed(_, _, _) => true
    case _                  => false
  }

  def isAborted = this match {
    case Aborted(_) => true
    case _          => false
  }

  def isFailed = this match {
    case Failed(_, _, _) => true
    case _               => false
  }

  def stateCannot(what: String): IllegalStateException = new IllegalStateException(s"State ${this.getClass.getSimpleName} cannot $what()")
}

object NodeStatus {

  @JsonCodec
  case class Draft(override val results: Seq[Id] = Seq.empty) extends NodeStatus(NodeStatusName.Draft, results) {
    def start = throw stateCannot("start")
    def finish(results: Seq[Id]) = throw stateCannot("finish")
    def abort = Aborted(results)
    def fail(error: FailureDescription) = {
      val now = Instant.now
      Failed(now, now, error)
    }
    def enqueue = Queued(results)
  }

  @JsonCodec
  case class Queued(override val results: Seq[Id] = Seq.empty) extends NodeStatus(NodeStatusName.Queued, results) {
    def start = Running(Instant.now, results)
    def finish(results: Seq[Id]) = throw stateCannot("finish")
    def abort = Aborted(results)
    def fail(error: FailureDescription) = {
      val now = Instant.now
      Failed(now, now, error)
    }
    def enqueue = throw stateCannot("enqueue")
  }

  @JsonCodec
  case class Running(started: Instant, override val results: Seq[Id] = Seq.empty) extends NodeStatus(NodeStatusName.Running, results) {
    def start = throw stateCannot("start")
    def abort = Aborted(results)
    def fail(error: FailureDescription) = Failed(started, Instant.now, error)
    def finish(results: Seq[Id]) = Completed(started, Instant.now, results)
    def enqueue: Queued = throw stateCannot("enqueue")
  }

  @JsonCodec
  sealed trait FinalState { self: NodeStatus =>
    def start: Running = throw stateCannot("start")
    def finish(results: Seq[Id]): Completed = throw stateCannot("finish")
    def abort: Aborted = throw stateCannot("abort")
    def fail(error: FailureDescription): Failed = throw stateCannot("fail")
    def enqueue: Queued = throw stateCannot("enqueue")
  }

  @JsonCodec
  case class Completed(started: Instant, ended: Instant, override val results: Seq[Id]) extends NodeStatus(NodeStatusName.Completed, results) with FinalState

  @JsonCodec
  case class Failed(started: Instant, ended: Instant, error: FailureDescription) extends NodeStatus(NodeStatusName.Failed, results = Seq.empty) with FinalState

  @JsonCodec
  case class Aborted(override val results: Seq[Id] = Seq.empty) extends NodeStatus(NodeStatusName.Aborted, results) with FinalState
}