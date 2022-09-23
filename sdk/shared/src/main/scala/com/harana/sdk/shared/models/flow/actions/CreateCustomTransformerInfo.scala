package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.CustomTransformerInfo
import com.harana.sdk.shared.models.flow.actions.custom.{SinkInfo, SourceInfo}
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.Custom
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.graph.{Edge, FlowGraph}
import com.harana.sdk.shared.models.flow.parameters.WorkflowParameter
import com.harana.sdk.shared.models.flow.parameters.custom.InnerWorkflow
import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.Json

import java.util.UUID
import scala.reflect.runtime.universe.TypeTag

trait CreateCustomTransformerInfo extends TransformerAsFactoryInfo[CustomTransformerInfo] with ActionDocumentation {

  val id: Id = "65240399-2987-41bd-ba7e-2944d60a3404"
  val name = "Create Custom Transformer"
  val since = Version(1, 0, 0)
  val category = Custom

  lazy val portO_0: TypeTag[CustomTransformerInfo] = typeTag

  val innerWorkflowParameter = WorkflowParameter(name = "inner workflow")
  setDefault(innerWorkflowParameter, CreateCustomTransformerInfo.default)
  def getInnerWorkflow = $(innerWorkflowParameter)
  def setInnerWorkflow(workflow: Json): this.type = set(innerWorkflowParameter, workflow.as[InnerWorkflow].toOption.get)
  def setInnerWorkflow(workflow: InnerWorkflow): this.type = set(innerWorkflowParameter, workflow)

  override val parameters = Array(innerWorkflowParameter)
  override def getDatasourcesIds: Set[UUID] = getInnerWorkflow.getDatasourcesIds
}

object CreateCustomTransformerInfo extends CreateCustomTransformerInfo {
  private val sourceNodeId: Id = "2603a7b5-aaa9-40ad-9598-23f234ec5c32"
  private val sinkNodeId: Id = "d7798d5e-b1c6-4027-873e-a6d653957418"

  private val sourceNode = Node(sourceNodeId, new SourceInfo() {})
  private val sinkNode = Node(sinkNodeId, new SinkInfo() {})

  def apply(pos: (Int, Int), color: Option[String] = None) = new CreateCustomTransformerInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }

  val default = InnerWorkflow(
    FlowGraph(nodes = Set(sourceNode, sinkNode), edges = Set(Edge((sourceNode, 0), (sinkNode, 0))))
  )
}