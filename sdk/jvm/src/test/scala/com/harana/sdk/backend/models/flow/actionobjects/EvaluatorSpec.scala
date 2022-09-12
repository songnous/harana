package com.harana.sdk.backend.models.flow.actionobjects

import org.mockito.Mockito._
import com.harana.sdk.backend.models.designer.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.designer.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.designer.flow.UnitSpec
import com.harana.sdk.backend.models.designer.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.actionobjects.MetricValue

class EvaluatorSpec extends UnitSpec {

  private def evaluator = {
    val e = mock[Evaluator]
    when(e.evaluate).thenCallRealMethod()
    e
  }

  val dataFrame = mock[DataFrame]
  val metricValue = mock[MetricValue]
  val execCtx = mock[ExecutionContext]
  val inferCtx = mock[InferContext]
  val emptyWarnings = InferenceWarnings.empty

  "Evaluator" should {

    "evaluate DataFrame" in {
      val e = evaluator

      when(e._evaluate(execCtx, dataFrame)).thenReturn(metricValue)
      e.evaluate(execCtx)(())(dataFrame) shouldBe metricValue
    }

    "infer knowledge" in {
      val e = evaluator
      when(e._infer(Knowledge(dataFrame))).thenReturn(metricValue)

      val (knowledge, warnings) = e.evaluate.infer(inferCtx)(())(Knowledge(dataFrame))
      knowledge.single shouldBe metricValue
      warnings shouldBe emptyWarnings
    }
  }
}
