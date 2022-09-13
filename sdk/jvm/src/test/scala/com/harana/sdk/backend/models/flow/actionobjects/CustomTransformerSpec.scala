package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.actionobjects.InnerWorkflowTestFactory.simpleGraph
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.ConvertType
import com.harana.sdk.backend.models.flow.filesystemclients.FileSystemClient
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.backend.models.flow.{ContextualCustomCodeExecutor, ContextualDataFrameStorage, ExecutionContext, InnerWorkflowExecutor}
import com.harana.sdk.backend.models.flow.actions.ConvertType
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.shared.models.designer.flow.parameters.custom
import com.harana.sdk.shared.models.flow.ExecutionMode
import com.harana.sdk.shared.models.flow.actionobjects.{ParameterWithValues, TargetTypeChoices}
import com.harana.sdk.shared.models.flow.parameters.custom
import com.harana.sdk.shared.models.flow.parameters.custom.{InnerWorkflow, PublicParameter}
import com.harana.spark.SparkSQLSession
import io.circe.Json
import org.apache.spark.SparkContext
import org.apache.spark.sql.types._
import org.mockito.ArgumentMatchers.{any, same}
import org.mockito.Mockito.when
import org.mockito.invocation.InvocationOnMock

class CustomTransformerSpec extends UnitSpec {

  "CustomTransformer" should {

    "execute inner workflow" in {
      val workflow = InnerWorkflow(simpleGraph, Json.Null)
      val outputDataFrame = mock[DataFrame]

      val innerWorkflowExecutor = mock[InnerWorkflowExecutor]
      when(innerWorkflowExecutor.execute(any(), same(workflow), any())).thenReturn(outputDataFrame)

      val context = mockExecutionContext(innerWorkflowExecutor)
      val transformer = new CustomTransformer(workflow, Seq.empty)
      transformer._transform(context, mock[DataFrame]) shouldBe outputDataFrame
    }

    "infer knowledge" in {
      val inferContext = MockedInferContext()

      val transformer = new CustomTransformer(InnerWorkflow(simpleGraph, Json.Null, Seq.empty))

      val schema = StructType(
        Seq(
          StructField("column1", DoubleType),
          StructField("column2", DoubleType)
        )
      )

      val expectedSchema = StructType(
        Seq(
          StructField("column1", StringType),
          StructField("column2", DoubleType)
        )
      )

      transformer._transformSchema(schema, inferContext) shouldBe Some(expectedSchema)
    }

    "replicate" in {
      val publicParameter = TypeConverter().targetTypeParameter.replicate("public name")
      val publicParametersWithValues = Seq(ParameterWithValues(publicParameter))
      val parameters = publicParametersWithValues.map(_.param).toArray

      val workflow = InnerWorkflow(simpleGraph, Json.Null, List(PublicParameter(innerNodeId, "target type", "public name")))
      val transformer = new CustomTransformer(workflow, publicParametersWithValues)

      val replicated = transformer.replicate()
      replicated.innerWorkflow shouldBe workflow
      replicated.parameters shouldBe parameters
    }

    "replicate with set values" in {
      val publicParameter = TypeConverter().targetTypeParameter.replicate("public name")
      val defaultValue = TargetTypeChoices.IntegerTargetTypeChoice()
      val setValue = TargetTypeChoices.StringTargetTypeChoice()
      val publicParametersWithValues = Seq(ParameterWithValues(publicParameter, Some(defaultValue), Some(setValue)))
      val parameters = publicParametersWithValues.map(_.param).toArray

      val workflow = custom.InnerWorkflow(simpleGraph, Json.Null, List(custom.PublicParameter(innerNodeId, "target type", "public name")))
      val transformer = new CustomTransformer(workflow, publicParametersWithValues)

      val replicated = transformer.replicate()
      replicated.innerWorkflow shouldBe workflow
      replicated.parameters shouldBe parameters
      replicated.getDefault(publicParameter) shouldBe Some(defaultValue)
      replicated.get(publicParameter) shouldBe Some(setValue)
    }

    "set public parameters" when {
      "executing inner workflow" in {
        val innerParameter = TypeConverter().targetTypeParameter
        val publicParameter = TypeConverter().targetTypeParameter.replicate("public name")
        val publicParametersWithValues = Seq(ParameterWithValues(publicParameter))

        val customTargetType = TargetTypeChoices.LongTargetTypeChoice()

        val workflow = custom.InnerWorkflow(
          simpleGraph,
          Json.Null,
          List(custom.PublicParameter(innerNodeId, "target type", "public name"))
        )

        val outputDataFrame = mock[DataFrame]

        val innerWorkflowExecutor = mock[InnerWorkflowExecutor]
        when(innerWorkflowExecutor.execute(any(), any(), any()))
          .thenAnswer((invocation: InvocationOnMock) => {
            val workflow = invocation.getArguments.apply(1).asInstanceOf[InnerWorkflow]
            val innerOp = workflow.graph.nodes
              .find(_.id.toString == innerNodeId)
              .get
              .value
              .asInstanceOf[ConvertType]

            innerOp.get(innerParameter) shouldBe Some(customTargetType)

            outputDataFrame
          })

        val context = mockExecutionContext(innerWorkflowExecutor)
        val transformer = new CustomTransformer(workflow, publicParametersWithValues)
        transformer.set(publicParameter -> customTargetType)
        transformer._transform(context, mock[DataFrame]) shouldBe outputDataFrame
      }

      "inferring schema" in {
        val inferContext = MockedInferContext()

        val publicParameter = TypeConverter().targetTypeParameter.replicate("public name")
        val publicParametersWithValues = Seq(ParameterWithValues(publicParameter))

        val transformer = new CustomTransformer(
          custom.InnerWorkflow(simpleGraph, Json.Null, List(custom.PublicParameter(innerNodeId, "target type", "public name"))),
          publicParametersWithValues
        )

        transformer.set(publicParameter -> TargetTypeChoices.LongTargetTypeChoice())

        val schema = StructType(Seq(StructField("column1", DoubleType), StructField("column2", DoubleType)))
        val expectedSchema = StructType(Seq(StructField("column1", LongType), StructField("column2", DoubleType)))
        transformer._transformSchema(schema, inferContext) shouldBe Some(expectedSchema)
      }
    }
  }

  private def mockExecutionContext(innerWorkflowExecutor: InnerWorkflowExecutor): ExecutionContext =
    ExecutionContext(
      mock[SparkContext],
      mock[SparkSQLSession],
      mock[InferContext],
      ExecutionMode.Batch,
      mock[FileSystemClient],
      "/tmp",
      "/tmp/library",
      innerWorkflowExecutor,
      mock[ContextualDataFrameStorage],
      None,
      mock[ContextualCustomCodeExecutor]
    )

}
