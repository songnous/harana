package com.harana.sdk.backend.models.flow.actionobjects

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import com.harana.sdk.backend.models.designer.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.designer.flow.{Knowledge, TestSupport}
import com.harana.sdk.backend.models.designer.flow.ExecutionContext
import com.harana.sdk.backend.models.designer.flow.UnitSpec
import com.harana.sdk.backend.models.designer.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}

class TransformerSpec extends UnitSpec with TestSupport {

  private def transformer = {
    val t = mock[Transformer]
    when(t.transform).thenCallRealMethod()
    when(t._transformSchema(any(), any())).thenCallRealMethod()
    t
  }

  val inputDF = createDataFrame()
  val outputDF = createDataFrame()

  val inputSchema = inputDF.schema.get
  val outputSchema = outputDF.schema.get

  val execCtx = mock[ExecutionContext]
  val inferCtx = mock[InferContext]

  val emptyWarnings = InferenceWarnings.empty

  "Transformer" should {

    "transform DataFrame" in {
      val t = transformer
      when(t._transform(execCtx, inputDF)).thenReturn(outputDF)
      t.transform(execCtx)(())(inputDF) shouldBe outputDF
    }

    "infer schema" when {

      "it's implemented" when {
        val t = transformer
        when(t._transformSchema(inputSchema)).thenReturn(Some(outputSchema))
        val expectedOutputKnowledge = Knowledge(DataFrame.forInference(outputSchema))

        "input Knowledge contains exactly one type" in {
          val inputKnowledge = Knowledge(DataFrame.forInference(inputSchema))
          val output = t.transform.infer(inferCtx)(())(inputKnowledge)
          output shouldBe (expectedOutputKnowledge, emptyWarnings)
        }

        "input Knowledge contains more than one type" in {
          val inputKnowledge = Knowledge(
            DataFrame.forInference(inputSchema),
            DataFrame.forInference(inputSchema)
          )
          val output = t.transform.infer(inferCtx)(())(inputKnowledge)
          output shouldBe (expectedOutputKnowledge, emptyWarnings)
        }
      }
    }

    "not infer schema" when {

      "it's not implemented" in {
        val t = transformer
        when(t._transformSchema(inputSchema)).thenReturn(None)
        val inputKnowledge = Knowledge(DataFrame.forInference(inputSchema))
        val expectedOutputKnowledge = Knowledge(DataFrame.forInference(None))
        val output = t.transform.infer(inferCtx)(())(inputKnowledge)
        output shouldBe (expectedOutputKnowledge, emptyWarnings)
      }
    }
  }
}
