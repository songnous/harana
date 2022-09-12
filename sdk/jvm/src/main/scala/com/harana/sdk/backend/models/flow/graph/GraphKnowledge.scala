package com.harana.sdk.backend.models.flow.graph

import com.harana.sdk.backend.models.designer.flow.Knowledge
import com.harana.sdk.backend.models.designer.flow.inference.InferenceWarnings
import com.harana.sdk.backend.models.designer.flow.inference.exceptions.{AllTypesNotCompilableError, NoInputEdgesError}
import com.harana.sdk.backend.models.designer.flow.inference.warnings.SomeTypesNotCompilableWarning
import com.harana.sdk.backend.models.flow.inference.InferenceWarnings
import com.harana.sdk.shared.models.flow.ActionObjectInfo._
import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.generic.JsonCodec

case class GraphKnowledge(private[graph] val resultsMap: Map[Id, NodeInferenceResult]) {

  def addInference(id: Id, inferenceResult: NodeInferenceResult) = GraphKnowledge(resultsMap + (id -> inferenceResult))

  def addInference(other: GraphKnowledge): GraphKnowledge = {
    require(
      this.resultsMap.keySet.intersect(other.resultsMap.keySet).isEmpty,
      "Each summed graph knowledge object must describe different set of nodes so there is no ambiguity"
    )

    GraphKnowledge(this.resultsMap ++ other.resultsMap)
  }

  def getResult(id: Id): NodeInferenceResult = resultsMap(id)
  def results: Map[Id, NodeInferenceResult] = resultsMap

  def containsNodeKnowledge(id: Id): Boolean = resultsMap.contains(id)
  def getKnowledge(id: Id): Vector[Knowledge[ActionObjectInfo]] = getResult(id).ports

  lazy val errors =
    for {
      (nodeId, result) <- resultsMap
      errors  = result.errors
      if errors.nonEmpty
    } yield (nodeId, errors)
}

object GraphKnowledge {
  def apply(): GraphKnowledge = GraphKnowledge(Map.empty)
}

case class NodeInferenceResult(ports: Vector[Knowledge[ActionObjectInfo]],
                               warnings: InferenceWarnings = InferenceWarnings.empty,
                               errors: Vector[FlowError] = Vector.empty
)

object NodeInferenceResult {
  def empty: NodeInferenceResult = NodeInferenceResult(Vector.empty)
}

object TypesAccordance {

  trait TypesAccordance {
    def errors: Vector[FlowError] = Vector.empty
    def warnings: InferenceWarnings = InferenceWarnings.empty
  }

  case class All() extends TypesAccordance

  case class Some(portIndex: Int) extends TypesAccordance {
    override def warnings = InferenceWarnings(SomeTypesNotCompilableWarning(portIndex))
  }

  case class None(portIndex: Int) extends TypesAccordance {
    override def errors = Vector(AllTypesNotCompilableError(portIndex))
  }

  case class NotProvided(portIndex: Int) extends TypesAccordance {
    override def errors = Vector(NoInputEdgesError(portIndex))
  }
}
