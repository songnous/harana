package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.spark.Linalg.Vectors
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import com.harana.sdk.backend.models.flow.{IntegratedTestSupport, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.evaluators.BinaryClassificationEvaluator
import com.harana.sdk.backend.models.flow.actiontypes.exceptions.{ColumnDoesNotExistError, WrongColumnTypeError}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators.BinaryClassificationEvaluatorInfo._
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class BinaryClassificationEvaluatorIntegSpec extends IntegratedTestSupport {

  "BinaryClassificationEvaluator" should {
    val eval = new BinaryClassificationEvaluator()

    def simpleSchema: StructType = StructType(
      Seq(
        StructField("label", DoubleType),
        StructField("prediction", DoubleType),
        StructField("rawPrediction", new com.harana.spark.Linalg.VectorUDT())
      )
    )

    val simpleData = Seq(
      Seq(0.0, 1.0, Vectors.dense(-0.001, 0.001)),
      Seq(1.0, 1.0, Vectors.dense(-0.001, 0.001)),
      Seq(1.0, 1.0, Vectors.dense(-15.261485, 15.261485))
    )

    val simpleDataFrame = createDataFrame(simpleData.map(Row.fromSeq), simpleSchema)

    "calculate correct values for simple example" in {
      val areaUnderROC = eval.setMetricName(AreaUnderROC()).evaluate(executionContext)(Unit)(simpleDataFrame)
      areaUnderROC.value shouldBe 3.0 / 4.0
      val areaUnderPR = eval.setMetricName(AreaUnderPR()).evaluate(executionContext)(Unit)(simpleDataFrame)
      areaUnderPR.value shouldBe 11.0 / 12.0
      val precision = eval.setMetricName(Precision()).evaluate(executionContext)(Unit)(simpleDataFrame)
      precision.value shouldBe 2.0 / 3.0
      val recall = eval.setMetricName(Recall()).evaluate(executionContext)(Unit)(simpleDataFrame)
      recall.value shouldBe 1.0
      val f1Score = eval.setMetricName(F1Score()).evaluate(executionContext)(Unit)(simpleDataFrame)
      // F1-score calculation formula available at: https://en.wikipedia.org/wiki/F1_score
      f1Score.value shouldBe 0.8
    }

    // Based on "recognizing dogs in scenes from a video" example in:
    // https://en.wikipedia.org/wiki/Precision_and_recall
    def dogSchema: StructType = StructType(Seq(StructField("label", DoubleType), StructField("prediction", DoubleType)))
    val dogData = Seq(
      Seq(1.0, 1.0),
      Seq(1.0, 1.0),
      Seq(1.0, 1.0),
      Seq(1.0, 1.0),
      Seq(0.0, 1.0),
      Seq(0.0, 1.0),
      Seq(0.0, 1.0),
      Seq(1.0, 0.0),
      Seq(1.0, 0.0),
      Seq(1.0, 0.0),
      Seq(1.0, 0.0),
      Seq(1.0, 0.0)
    )
    val dogDataFrame = createDataFrame(dogData.map(Row.fromSeq), dogSchema)

    "calculate correct precision, recall and f1-score values" in {
      val precision = eval.setMetricName(Precision()).evaluate(executionContext)(Unit)(dogDataFrame)
      precision.value shouldBe 4.0 / 7.0
      val recall = eval.setMetricName(Recall()).evaluate(executionContext)(Unit)(dogDataFrame)
      recall.value shouldBe 4.0 / 9.0
      val f1Score = eval.setMetricName(new F1Score()).evaluate(executionContext)(Unit)(dogDataFrame)
      // F1-score calculation formula available at: https://en.wikipedia.org/wiki/F1_score
      f1Score.value shouldBe 0.5
    }

    "calculate correct precision, recall and f1-score values with non-default column names" in {
      def dogSchemaRenamed = StructType(Seq(StructField("labe", DoubleType), StructField("pred", DoubleType)))
      val dogDFRenamed = createDataFrame(dogData.map(Row.fromSeq), dogSchemaRenamed)

      val evalRenamed = new BinaryClassificationEvaluator()
      evalRenamed.setLabelColumn(new NameSingleColumnSelection("labe"))
      val predSel = new NameSingleColumnSelection("pred")
      val precision = evalRenamed.setMetricName(Precision().setPredictionColumn(predSel)).evaluate(executionContext)(Unit)(dogDFRenamed)
      precision.value shouldBe 4.0 / 7.0
      val recall = evalRenamed.setMetricName(Recall().setPredictionColumn(predSel)).evaluate(executionContext)(Unit)(dogDFRenamed)
      recall.value shouldBe 4.0 / 9.0
      val f1Score = evalRenamed.setMetricName(F1Score().setPredictionColumn(predSel)).evaluate(executionContext)(Unit)(dogDFRenamed)
      // F1-score calculation formula from: https://en.wikipedia.org/wiki/F1_score
      f1Score.value shouldBe 2.0 * precision.value * recall.value / (precision.value + recall.value)
    }

    "throw exceptions" when {
      // TODO: Schema cannot be checked in method _infer until DS-3258 is fixed
      // currently schema is checked only in method evaluate
      "label column has invalid type" in {
        def invalidSchema: StructType =
          StructType(Seq(StructField("label", StringType), StructField("prediction", DoubleType)))
        // TODO: DS-3258: Creating DF with dummy data should not be necessary for _infer
        // DataFrame.forInference(invalidSchema) should be sufficient
        val invalidDataFrame = createDataFrame(Seq(Row("label1", 1.0)), invalidSchema)

        intercept[WrongColumnTypeError] {
          eval.setMetricName(new Precision())._infer(Knowledge(invalidDataFrame))
        }
        ()
      }

      "prediction column has invalid type and there is no rawPrediction column" in {
        def invalidSchema: StructType =
          StructType(Seq(StructField("label", DoubleType), StructField("prediction", StringType)))
        // TODO: DS-3258: Creating DF with dummy data should not be necessary for _infer
        // DataFrame.forInference(invalidSchema) should be sufficient
        val invalidDataFrame = createDataFrame(Seq(Row(0.0, "pred1")), invalidSchema)

        intercept[ColumnDoesNotExistError] { eval.setMetricName(AreaUnderROC())._infer(Knowledge(invalidDataFrame))}
        intercept[ColumnDoesNotExistError] { eval.setMetricName(AreaUnderPR())._infer(Knowledge(invalidDataFrame)) }
        intercept[WrongColumnTypeError] { eval.setMetricName(Precision())._infer(Knowledge(invalidDataFrame)) }
        intercept[WrongColumnTypeError] { eval.setMetricName(Recall())._infer(Knowledge(invalidDataFrame)) }
        intercept[WrongColumnTypeError] { eval.setMetricName(F1Score())._infer(Knowledge(invalidDataFrame)) }
        ()
      }

      "rawPrediction column has invalid type and there is no prediction column" in {
        def invalidSchema: StructType =
          StructType(Seq(StructField("label", DoubleType), StructField("rawPrediction", DoubleType))) // rawPrediction should be vector column
        // TODO: DS-3258: Creating DF with dummy data should not be necessary for _infer
        // DataFrame.forInference(invalidSchema) should be sufficient
        val invalidDataFrame = createDataFrame(Seq(Row(0.0, 1.0)), invalidSchema)

        intercept[WrongColumnTypeError] { eval.setMetricName(AreaUnderROC())._infer(Knowledge(invalidDataFrame)) }
        intercept[WrongColumnTypeError] { eval.setMetricName(AreaUnderPR())._infer(Knowledge(invalidDataFrame)) }
        intercept[ColumnDoesNotExistError] { eval.setMetricName(Precision())._infer(Knowledge(invalidDataFrame)) }
        intercept[ColumnDoesNotExistError] { eval.setMetricName(Recall())._infer(Knowledge(invalidDataFrame)) }
        intercept[ColumnDoesNotExistError] { eval.setMetricName(F1Score())._infer(Knowledge(invalidDataFrame)) }
        ()
      }
    }
  }
}
