package com.harana.sdk.backend.models.flow.graph

import com.harana.sdk.backend.models.flow.graph.ActionTestClasses._
import com.harana.sdk.backend.models.flow.graph.DClassesForActions._
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarning, InferenceWarnings}
import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.actiontypes.ActionTypeType2To1
import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.exceptions.{FlowError, FlowMultiError}
import com.harana.sdk.shared.models.flow.graph.FlowGraph.Node[ActionTypeInfo]
import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.graph.{Edge, FlowGraph}
import com.harana.sdk.shared.models.flow.parameters.exceptions.ValidationError
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.reflect.runtime.{universe => ru}

class AbstractInferenceSpec extends AnyWordSpec with TestSupport with Matchers {

  val hierarchy = new ActionObjectCatalog
  hierarchy.register[A1]
  hierarchy.register[A2]

  val knowledgeA1: Knowledge[ActionObjectInfo] = Knowledge(A1())
  val knowledgeA2: Knowledge[ActionObjectInfo] = Knowledge(A2())
  val knowledgeA12: Knowledge[ActionObjectInfo] = Knowledge(A1(), A2())

  val inferenceCtx: InferContext = createInferContext(hierarchy)

  case class ActionTypeTypeA1A2ToFirst() extends ActionTypeType2To1[A1, A2, A] with ActionBaseFields {

    import ActionTypeTypeA1A2ToFirst._

    def execute(t1: A1, t2: A2)(context: ExecutionContext): A = ???

    override def validateParameters = if (parametersValid) List.empty else List(parameterInvalidError)

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

    lazy val tTagTI_0: Tag[A1] = Tag[A1]
    lazy val tTagTO_0: Tag[A] = Tag[A]
    lazy val tTagTI_1: Tag[A2] = Tag[A2]
  }

  object ActionTypeTypeA1A2ToFirst {

    val parameterInvalidError = new ValidationError("") {}

    val inferenceError = new FlowError("") {}

    val multiInferenceError = FlowMultiError(List(mock[FlowError], mock[FlowError]))

    val warning = mock[InferenceWarning]

  }

  val idCreateA1 = Node.Id.randomId
  val idA1ToA = Node.Id.randomId
  val idAToA1A2 = Node.Id.randomId
  val idA1A2ToFirst = Node.Id.randomId

  def nodeCreateA1 = Node(idCreateA1, ActionTypeTypeCreateA1())
  def nodeA1ToA = Node(idA1ToA, ActionTypeTypeA1ToA())
  def nodeAToA1A2 = Node(idAToA1A2, ActionTypeTypeAToA1A2())
  def nodeA1A2ToFirst = Node(idA1A2ToFirst, ActionTypeTypeA1A2ToFirst())

  def validGraph = FlowGraph(
    nodes = Set(nodeCreateA1, nodeAToA1A2, nodeA1A2ToFirst),
    edges = Set(
      Edge((nodeCreateA1, 0), (nodeAToA1A2, 0)),
      Edge((nodeAToA1A2, 0), (nodeA1A2ToFirst, 0)),
      Edge((nodeAToA1A2, 1), (nodeA1A2ToFirst, 1))
    )
  )

  def setParametersValid(node: Node[ActionTypeInfo]) = node.value.asInstanceOf[ActionTypeTypeA1A2ToFirst].setParametersValid()
  def setInferenceErrorThrowing(node: Node[ActionTypeInfo]) = node.value.asInstanceOf[ActionTypeTypeA1A2ToFirst].setInferenceErrorThrowing()
  def setInferenceErrorMultiThrowing(node: Node[ActionTypeInfo]) = node.value.asInstanceOf[ActionTypeTypeA1A2ToFirst].setInferenceErrorThrowingMultiException()
  def setParametersInvalid(node: Node[ActionTypeInfo]) = node.value.asInstanceOf[ActionTypeTypeA1A2ToFirst].setParametersInvalid()
  def setParametersValid(graph: FlowGraph) = setInGraph(graph, _.setParametersValid())

  def setInGraph(graph: FlowGraph, f: ActionTypeTypeA1A2ToFirst => Unit) = {
    val node = graph.node(idA1A2ToFirst)
    f(node.value.asInstanceOf[ActionTypeTypeA1A2ToFirst])
  }
}
