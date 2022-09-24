package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow._
import com.harana.sdk.shared.models.flow.ActionInfo.{Id, ReportParameter}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{NumericParameter, ParameterMap}
import org.apache.spark.sql.types.StructType

import scala.reflect.runtime.universe.TypeTag

object MockTransformers extends UnitSpec with TestSupport {

  val DefaultForA = 1

  val inputDataFrame = createDataFrame()
  val inputSchema = inputDataFrame.schema.get

  val outputDataFrame1 = createDataFrame()
  val outputDataFrame2 = createDataFrame()
  val outputSchema1 = outputDataFrame1.schema.get
  val outputSchema2 = outputDataFrame2.schema.get

  class MockTransformer extends Transformer {
    val id = "test"

    val paramA = NumericParameter("a", default = Some(DefaultForA), validator = RangeValidator(0.0, Double.MaxValue))
    val parameters = Left(List(paramA))

    override def report(extended: Boolean = true) = ???

    def applyTransform(ctx: ExecutionContext, df: DataFrame) =
      $(paramA) match {
        case 1      => outputDataFrame1
        case -2 | 2 => outputDataFrame2
      }

    override def applyTransformSchema(schema: StructType): Option[StructType] =
      Some($(paramA) match {
        case 1      => outputSchema1
        case -2 | 2 => outputSchema2
      })

    override def load(ctx: ExecutionContext, path: String) = ???

    override def saveTransformer(ctx: ExecutionContext, path: String) = ???

  }
}

class TransformerAsActionSpec extends UnitSpec {

  import MockTransformers._

  class MockTransformerAsAction extends TransformerAsAction[MockTransformer] {
    val name = ""
    val id: Id = "6d924962-9456-11e5-8994-feff819cdc9f"
      override lazy val tTagTO_1: TypeTag[MockTransformer] = typeTag[MockTransformer]
  }

  "TransformerAsAction" should {
    def action = new MockTransformerAsAction
    val op = action
    val changedParamMap = ParameterMap(op.transformer.paramA -> 2, op.reportTypeParameter -> ReportParameter.Extended())

    "have specific parameters same as Transformer" in {
      op.parameters shouldBe Array(op.transformer.paramA)
    }

    "have report type param set to extended" in {
      op.extractParameterMap().get(op.reportTypeParameter).get shouldBe ReportParameter.Extended()
    }

    "have defaults same as in Transformer" in {
      op.extractParameterMap() shouldBe ParameterMap(op.transformer.paramA -> DefaultForA, ReportTypeDefault(op.reportTypeParameter))
    }

    "execute transform using transformer with properly set parameters and return it" in {
      op.set(op.transformer.paramA -> 2)
      val result = op.executeUntyped(List(mock[DataFrame]))(mock[ExecutionContext])

      (result should have).length(2)
      result(0).asInstanceOf[DataFrame] shouldBe outputDataFrame2
      result(1).asInstanceOf[MockTransformer].extractParameterMap() shouldBe changedParamMap
    }

    "infer types on transformer with properly set parameters and return it" in {
      op.set(op.transformer.paramA -> 2)

      val inputDF = inputDataFrame
      val (result, warnings) = op.inferKnowledgeUntyped(List(Knowledge(inputDF)))(mock[InferContext])

      warnings shouldBe InferenceWarnings.empty

      (result should have).length(2)
      result(0).asInstanceOf[Knowledge[DataFrame]] shouldBe Knowledge(DataFrame.forInference(outputSchema2))
      result(1).single.asInstanceOf[MockTransformer].extractParameterMap() shouldBe changedParamMap
    }
  }
}
