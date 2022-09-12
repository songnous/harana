package com.harana.sdk.shared.models.flow

import com.harana.sdk.shared.models.flow
import com.harana.sdk.shared.models.flow.exceptions.FailureDescription
import com.harana.sdk.shared.models.flow.graph.node.NodeStatus
import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.generic.JsonCodec

@JsonCodec
case class NodeState(nodeStatus: NodeStatus, reports: Option[EntitiesMap]) {

  def reportEntities() = reports.map(_.entities).getOrElse(Map.empty[Id, EntitiesMap.Entry])

  def withoutReports = copy(reports = None)

  def abort = copy(nodeStatus = nodeStatus.abort)
  def enqueue = copy(nodeStatus = nodeStatus.enqueue)
  def draft = copy(nodeStatus = NodeStatus.Draft(nodeStatus.results))
  def fail(failureDescription: FailureDescription) = copy(nodeStatus = nodeStatus.fail(failureDescription), Some(EntitiesMap()))

  def isCompleted = nodeStatus.isCompleted
  def isDraft = nodeStatus.isDraft
  def isQueued = nodeStatus.isQueued
  def isRunning = nodeStatus.isRunning
  def isFailed = nodeStatus.isFailed
  def isAborted = nodeStatus.isAborted

  def start = copy(nodeStatus = nodeStatus.start)
  def finish(entitiesIds: Seq[Id], results: EntitiesMap) = flow.NodeState(nodeStatus.finish(entitiesIds), Some(results))
}

object NodeState {
  def draft = NodeState(NodeStatus.Draft(), Some(EntitiesMap()))
}
