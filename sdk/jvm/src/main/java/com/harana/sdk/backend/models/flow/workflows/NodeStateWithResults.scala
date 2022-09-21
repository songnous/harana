package com.harana.sdk.backend.models.flow.workflows

import com.harana.sdk.backend.models.flow.Knowledge
import com.harana.sdk.backend.models.flow.graph.NodeInferenceResult
import com.harana.sdk.backend.models.flow.inference.InferenceWarnings
import com.harana.sdk.shared.models.flow
import com.harana.sdk.shared.models.flow.{ActionObjectInfo, EntitiesMap, NodeState}
import com.harana.sdk.shared.models.flow.exceptions.FailureDescription
import com.harana.sdk.shared.models.flow.report.ReportContent
import com.harana.sdk.shared.models.flow.utils.Id

case class NodeStateWithResults(nodeState: NodeState, actionObjects: Map[Id, ActionObjectInfo], knowledge: Option[NodeInferenceResult]) {

  def abort: NodeStateWithResults = copy(nodeState = nodeState.abort)
  def enqueue: NodeStateWithResults = copy(nodeState = nodeState.enqueue)
  def draft: NodeStateWithResults = copy(nodeState = nodeState.draft)
  def fail(failureDescription: FailureDescription): NodeStateWithResults = copy(nodeState = nodeState.fail(failureDescription))

  def withKnowledge(inferredKnowledge: NodeInferenceResult): NodeStateWithResults = copy(knowledge = Some(inferredKnowledge))
  def clearKnowledge: NodeStateWithResults = copy(knowledge = None)

  def isCompleted: Boolean = nodeState.isCompleted
  def isQueued: Boolean = nodeState.isQueued
  def isRunning: Boolean = nodeState.isRunning
  def isFailed: Boolean = nodeState.isFailed
  def isAborted: Boolean = nodeState.isAborted
  def isDraft: Boolean = nodeState.isDraft

  def start: NodeStateWithResults = copy(nodeState = nodeState.start)

  def finish(entitiesIds: Seq[Id], reports: Map[Id, ReportContent], actionObjects: Map[Id, ActionObjectInfo]) = {
    val results = flow.EntitiesMap(actionObjects, reports)
    val actionObjectsKnowledge = entitiesIds.flatMap(id => actionObjects.get(id)).map(Knowledge(_)).toList
    val newWarnings = knowledge.map(_.warnings).getOrElse(InferenceWarnings.empty)
    val newKnowledge = Some(NodeInferenceResult(actionObjectsKnowledge, newWarnings, List.empty))
    NodeStateWithResults(nodeState.finish(entitiesIds, results), actionObjects, newKnowledge)
  }
}

object NodeStateWithResults {
  def draft = NodeStateWithResults(NodeState.draft, Map(), None)
}