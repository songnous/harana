package com.harana.sdk.backend.models.flow.actionobjects

import org.mockito.Mockito._
import com.harana.sdk.backend.models.flow.{Knowledge, TestSupport}
import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge, TestSupport, UnitSpec}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}

class EstimatorIntegSpec extends UnitSpec with TestSupport {

  private def estimator = {
    val e = mock[Estimator[Transformer]]
    when(e.fit).thenCallRealMethod()
    e
  }

  val transformer = mock[Transformer]

  "Estimator" should {

    "fit to a DataFrame producing a Transfomer" in {
      val dataFrame = mock[DataFrame]
      val e = estimator
      val context: ExecutionContext = mock[ExecutionContext]
      when(e._fit(context, dataFrame)).thenReturn(transformer)
      val outputTransfomer = e.fit(context)(())(dataFrame)
      outputTransfomer shouldBe transformer
    }

    "infer" when {

      "input Knowledge contains exactly one type" in {
        val schema = createSchema()
        val inputKnowledge = Knowledge(DataFrame.forInference(schema))
        val e = estimator
        when(e._fit_infer(Some(schema))).thenReturn(transformer)
        val (outputKnowledge, warnings) = e.fit.infer(mock[InferContext])(())(inputKnowledge)
        outputKnowledge shouldBe Knowledge(transformer)
        warnings shouldBe InferenceWarnings.empty
      }

      "input Knowledge contains more than one type (by taking the first type)" in {
        val schema = createSchema()
        val schema2 = createSchema()
        val inputKnowledge = Knowledge(
          DataFrame.forInference(schema),
          DataFrame.forInference(schema2)
        )
        val e = estimator
        when(e._fit_infer(Some(schema))).thenReturn(transformer)
        val (outputKnowledge, warnings) = e.fit.infer(mock[InferContext])(())(inputKnowledge)
        outputKnowledge shouldBe Knowledge(transformer)
        warnings shouldBe InferenceWarnings.empty
      }
    }
  }
}
