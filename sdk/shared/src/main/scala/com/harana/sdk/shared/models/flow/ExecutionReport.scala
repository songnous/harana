package com.harana.sdk.shared.models.flow

import com.harana.sdk.shared.models.flow
import com.harana.sdk.shared.models.flow.exceptions.FailureDescription
import com.harana.sdk.shared.models.flow.graph.node.{Node, NodeStatus}
import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.generic.JsonCodec

@JsonCodec
case class ExecutionReport(states: Map[Id, NodeState], error: Option[FailureDescription] = None) {
  def nodesStatuses: Map[Id, NodeStatus] = states.view.mapValues(_.nodeStatus).toMap
  def resultEntities = flow.EntitiesMap(states.valuesIterator.flatMap(_.reportEntities().toSeq).toMap)
  def statesOnly: ExecutionReport = copy(states = states.map(p => p._1 -> p._2.withoutReports))
}

object ExecutionReport {

  def apply(nodes: Map[Id, NodeStatus], resultEntities: EntitiesMap, error: Option[FailureDescription]): ExecutionReport =
    ExecutionReport(toNodeStates(nodes, resultEntities), error)

  def statesOnly(nodes: Map[Id, NodeStatus], error: Option[FailureDescription]): ExecutionReport =
    ExecutionReport(nodes.view.mapValues(status => flow.NodeState(status, None)).toMap, error)

  private def toNodeStates(nodes: Map[Id, NodeStatus], resultEntities: EntitiesMap): Map[Id, NodeState] =
    nodes.view.mapValues(status => flow.NodeState(status, Some(resultEntities.subMap(status.results.toSet)))).toMap

}