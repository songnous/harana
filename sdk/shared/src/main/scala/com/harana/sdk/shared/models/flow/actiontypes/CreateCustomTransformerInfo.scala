package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action
import com.harana.sdk.shared.models.flow.actionobjects.CustomTransformerInfo
import com.harana.sdk.shared.models.flow.actiontypes.custom.{SinkInfo, SourceInfo}
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.Custom
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.graph.{Edge, FlowGraph}
import com.harana.sdk.shared.models.flow.parameters.custom.InnerWorkflow
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, WorkflowParameter}
import com.harana.sdk.shared.models.flow.utils.Id
import com.harana.sdk.shared.utils.HMap
import io.circe.Json

import java.util.UUID
import izumi.reflect.Tag

trait CreateCustomTransformerInfo extends TransformerAsFactoryInfo[CustomTransformerInfo] with ActionDocumentation {

  val id: Id = "65240399-2987-41bd-ba7e-2944d60a3404"
  val name = "create-custom-transformer"
  val since = Version(1, 0, 0)
  val category = Custom

  lazy val portO_0: Tag[CustomTransformerInfo] = typeTag

// FIXME
  val innerWorkflowParameter = WorkflowParameter("inner-workflow", default = None)
  //  val innerWorkflowParameter = WorkflowParameter("inner-workflow", default = Some(CreateCustomTransformerInfo.default))
  def getInnerWorkflow = $(innerWorkflowParameter)
  def setInnerWorkflow(workflow: Json): this.type = set(innerWorkflowParameter, workflow.as[InnerWorkflow].toOption.get)
  def setInnerWorkflow(workflow: InnerWorkflow): this.type = set(innerWorkflowParameter, workflow)

  override val parameterGroups = List(ParameterGroup("", innerWorkflowParameter))
}

object CreateCustomTransformerInfo extends CreateCustomTransformerInfo {
  val sourceAction = Action(SourceInfo, (0, 0), None, None, None, HMap.empty)
  private val sourceNode = Node(sourceAction.id, sourceAction)

  val sinkAction = Action(SinkInfo, (0, 0), None, None, None, HMap.empty)
  private val sinkNode = Node(sinkAction.id, sinkAction)

  // FIXME: This is broken
  lazy val default = InnerWorkflow(
    FlowGraph(nodes = Set(sourceNode, sinkNode), edges = Set(Edge((sourceNode, 0), (sinkNode, 0))))
  )
}