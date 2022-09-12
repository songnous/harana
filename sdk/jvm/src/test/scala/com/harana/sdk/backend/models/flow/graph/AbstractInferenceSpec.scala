package com.harana.sdk.backend.models.flow.graph

import scala.reflect.runtime.{universe => ru}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.harana.sdk.backend.models.designer.flow._
import com.harana.sdk.backend.models.designer.flow.graph.DClassesForActions._
import com.harana.sdk.backend.models.designer.flow.graph.ActionTestClasses._
import com.harana.sdk.backend.models.designer.flow.graph.FlowGraph.FlowNode
import com.harana.sdk.backend.models.designer.flow.inference.{InferContext, InferenceWarning, InferenceWarnings}
import com.harana.sdk.backend.models.flow.{Action2To1, ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarning, InferenceWarnings}
import com.harana.sdk.shared.models.designer.flow
import com.harana.sdk.shared.models.designer.flow.catalogs.ActionObjectCatalog
import com.harana.sdk.shared.models.designer.flow.ActionObjectCatalog
import com.harana.sdk.shared.models.flow.graph.FlowGraph.FlowNode
import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.exceptions.{FlowError, FlowMultiError}
import com.harana.sdk.shared.models.flow.graph.{Edge, FlowGraph}
import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.parameters.exceptions.ValidationError

class AbstractInferenceSpec extends AnyWordSpec with TestSupport with Matchers {

  val hierarchy = new ActionObjectCatalog
  hierarchy.register[A1]
  hierarchy.register[A2]

  val knowledgeA1: Knowledge[ActionObjectInfo] = Knowledge(A1())
  val knowledgeA2: Knowledge[ActionObjectInfo] = Knowledge(A2())
  val knowledgeA12: Knowledge[ActionObjectInfo] = Knowledge(A1(), A2())

  val inferenceCtx: InferContext = createInferContext(hierarchy)

  case class ActionA1A2ToFirst() extends Action2To1[A1, A2, A] with ActionBaseFields {

    import ActionA1A2ToFirst._

    def execute(t1: A1, t2: A2)(context: ExecutionContext): A = ???

    override def validateParameters = if (parametersValid) Vector.empty else Vector(parameterInvalidError)

    private var parametersValid: Boolean = _

    def setParametersValid() = parametersValid = true
    def setParametersInvalid() = parametersValid = false

    private var inferenceShouldThrow = false
    private var multiException = false

    def setInferenceErrorThrowing() = inferenceShouldThrow = true

    def setInferenceErrorThrowingMultiException() = {
      inferenceShouldThrow = true
      multiException = true
    }

    override def inferKnowledge(k0: Knowledge[A1], k1: Knowledge[A2])(context: InferContext): (Knowledge[A], InferenceWarnings) = {
      if (inferenceShouldThrow)
        if (multiException) throw multiInferenceError else throw inferenceError
      (k0, InferenceWarnings(warning))
    }

    lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]
    lazy val tTagTO_0: ru.TypeTag[A] = ru.typeTag[A]
    lazy val tTagTI_1: ru.TypeTag[A2] = ru.typeTag[A2]
  }

  object ActionA1A2ToFirst {

    val parameterInvalidError = new ValidationError("") {}

    val inferenceError = new FlowError("") {}

    val multiInferenceError = FlowMultiError(Vector(mock[FlowError], mock[FlowError]))

    val warning = mock[InferenceWarning]

  }

  val idCreateA1 = Node.Id.randomId
  val idA1ToA = Node.Id.randomId
  val idAToA1A2 = Node.Id.randomId
  val idA1A2ToFirst = Node.Id.randomId

  def nodeCreateA1 = Node(idCreateA1, ActionCreateA1())
  def nodeA1ToA = Node(idA1ToA, ActionA1ToA())
  def nodeAToA1A2 = Node(idAToA1A2, ActionAToA1A2())
  def nodeA1A2ToFirst = Node(idA1A2ToFirst, ActionA1A2ToFirst())

  def validGraph = FlowGraph(
    nodes = Set(nodeCreateA1, nodeAToA1A2, nodeA1A2ToFirst),
    edges = Set(
      flow.graph.Edge(nodeCreateA1, 0, nodeAToA1A2, 0),
      flow.graph.Edge(nodeAToA1A2, 0, nodeA1A2ToFirst, 0),
      flow.graph.Edge(nodeAToA1A2, 1, nodeA1A2ToFirst, 1)
    )
  )

  def setParametersValid(node: FlowNode) = node.value.asInstanceOf[ActionA1A2ToFirst].setParametersValid()
  def setInferenceErrorThrowing(node: FlowNode) = node.value.asInstanceOf[ActionA1A2ToFirst].setInferenceErrorThrowing()
  def setInferenceErrorMultiThrowing(node: FlowNode) = node.value.asInstanceOf[ActionA1A2ToFirst].setInferenceErrorThrowingMultiException()
  def setParametersInvalid(node: FlowNode) = node.value.asInstanceOf[ActionA1A2ToFirst].setParametersInvalid()
  def setParametersValid(graph: FlowGraph) = setInGraph(graph, _.setParametersValid())

  def setInGraph(graph: FlowGraph, f: ActionA1A2ToFirst => Unit) = {
    val node = graph.node(idA1A2ToFirst)
    f(node.value.asInstanceOf[ActionA1A2ToFirst])
  }
}
