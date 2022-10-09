package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.{CommonExecutionContext, ExecutionContext, Knowledge}
import com.harana.sdk.shared.models.designer.flow
import io.circe.syntax.EncoderOps
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.types.StructType
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.serialization.JsonObjectPersistence
import com.harana.sdk.backend.models.flow.actiontypes.exceptions.CustomActionExecutionError
import com.harana.sdk.backend.models.flow.graph.{GraphInference, GraphKnowledge, NodeInferenceResult}
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.backend.models.flow.utils.CustomTransformerFactory
import com.harana.sdk.shared.models.flow.actionobjects.{CustomTransformerInfo, ParameterWithValues}
import com.harana.sdk.shared.models.flow.parameters.custom.{InnerWorkflow, PublicParameter}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterMap, ParameterPair}

import java.util.UUID

class CustomTransformer(innerWorkflow: InnerWorkflow = InnerWorkflow.empty,
                        publicParametersWithValues: Seq[ParameterWithValues[_]] = Seq.empty)
  extends CustomTransformerInfo(publicParametersWithValues) with Transformer {

  def this() = this(InnerWorkflow.empty, Seq.empty)

  def applyTransform(ctx: ExecutionContext, df: DataFrame) = ctx.innerWorkflowExecutor.execute(CommonExecutionContext(ctx), workflowWithParameters(), df)

  override def applyTransformSchema(schema: StructType, inferCtx: InferContext): Option[StructType] = {
    val workflow = workflowWithParameters()
    val initialKnowledge = GraphKnowledge(Map(workflow.source.id -> NodeInferenceResult(List(Knowledge(DataFrame.forInference(schema))))))
    val graphKnowledge = GraphInference.inferKnowledge(workflow.graph, inferCtx, initialKnowledge)

    if (graphKnowledge.errors.nonEmpty) {
      throw CustomActionExecutionError("Inner workflow contains errors:\n" + graphKnowledge.errors.values.flatten.map(_.toString).mkString("\n")).toException
    }

    graphKnowledge
      .getKnowledge(workflow.sink.id)(0)
      .asInstanceOf[Knowledge[DataFrame]]
      .single
      .schema
  }

  override def replicate(extra: ParameterMap = ParameterMap.empty) = {
    val that = new CustomTransformer(innerWorkflow, publicParametersWithValues).asInstanceOf[this.type]
    copyValues(that, extra)
  }

  private def workflowWithParameters() = {
    innerWorkflow.publicParameters.foreach { case PublicParameter(nodeId, parameterName, publicName) =>
      val node = innerWorkflow.graph.node(nodeId)
      val action = node.value
// FIXME
//      val innerParameter = getParameter(action.allParameters, parameterName).asInstanceOf[Parameter[Any]]
//      action.set(innerParameter -> $(getParameter(allParameters, publicName)))
    }
    innerWorkflow
  }

  override def saveTransformer(ctx: ExecutionContext, path: String) = {
    val innerWorkflowPath = CustomTransformer.innerWorkflowPath(path)
    val innerWorkflowJson = ctx.innerWorkflowExecutor.toJson(innerWorkflow)
    JsonObjectPersistence.saveJsonToFile(ctx, innerWorkflowPath, innerWorkflowJson)
  }

  override def loadTransformer(ctx: ExecutionContext, path: String) = {
    val innerWorkflowPath = CustomTransformer.innerWorkflowPath(path)
    val innerWorkflowJson = JsonObjectPersistence.loadJsonFromFile(ctx, innerWorkflowPath)
    val innerWorkflow = ctx.innerWorkflowExecutor.parse(innerWorkflowJson.asJson)
    CustomTransformerFactory.createCustomTransformer(innerWorkflow).asInstanceOf[this.type]
  }
}

object CustomTransformer {
  private val innerWorkflow = "innerWorkflow"
  def innerWorkflowPath(path: String) = new Path(path, innerWorkflow).toString
}