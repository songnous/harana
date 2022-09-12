package com.harana.sdk.backend.models.flow.actions

import org.apache.spark.ml
import org.apache.spark.ml.evaluation
import org.apache.spark.ml.tuning.CrossValidator
import org.apache.spark.ml.tuning.ParamGridBuilder
import com.harana.sdk.backend.models.flow.{IntegratedTestSupport, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.LinearRegression
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.evaluators.RegressionEvaluator
import com.harana.sdk.backend.models.flow.actions.exceptions.ColumnDoesNotExistError
import com.harana.sdk.shared.models.flow.parameters.ParameterType.GridSearch
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.utils.DoubleUtils
import com.harana.spark.Linalg
import com.harana.spark.Linalg.Vectors
import io.circe.syntax.EncoderOps

class GridSearchIntegSpec extends IntegratedTestSupport {

  private val regularizationParameters = Array(0.01, 0.5, 5.0)

  private val estimatorParameters = (
    "regularization param" -> seqParameter(Seq(0.01, 0.5, 5.0)),
    "features column" -> (
                            "type"  -> "column",
                            "value" -> "features"
                          ),
    "max iterations" -> 10.0
  ).asJson

  private def seqParameter(values: Seq[Double]) =
    (
    "values" ->
      (
        "type"  -> "seq",
        "value" -> "sequence" -> values
      )
    ).asJson

  "GridSearch" should {

    "find best parameters" in {
      val gridSearch = new GridSearch()
      val estimator = new LinearRegression()
      val dataFrame = buildDataFrame()
      val evaluator = new RegressionEvaluator()
      gridSearch.setEstimatorParameters(estimatorParameters)
      gridSearch.setNumberOfFolds(2)

      val results = gridSearch.executeUntyped(Vector(estimator, dataFrame, evaluator))(executionContext)
      val report  = results.head.asInstanceOf[Report]
      val tables = report.content.tables

      val expectedMetrics = pureSparkImplementation()
      val expectedBestMetric = expectedMetrics.toList.min

      val bestMetricsTable = tables.head
      bestMetricsTable.values.size shouldBe 1
      bestMetricsTable.values shouldBe List(List(Some("10.0"), Some("5.0"), doubleToCell(expectedBestMetric)))

      val expectedMetricsTable = List(
        List(Some("10.0"), Some("5.0"), doubleToCell(expectedMetrics(2))),
        List(Some("10.0"), Some("0.5"), doubleToCell(expectedMetrics(1))),
        List(Some("10.0"), Some("0.01"), doubleToCell(expectedMetrics(0)))
      )
      val metricsTable = tables(1)
      metricsTable.values.size shouldBe 3
      metricsTable.values shouldBe expectedMetricsTable
    }

    "throw an exception in inference" when {
      "estimator parameters are invalid" in {
        val gridSearch = new GridSearch
        val estimator = new LinearRegression()
        val dataFrame = buildDataFrame()
        val evaluator = new RegressionEvaluator()
        val parameters = Json(
          estimatorParameters.fields.updated(
            "features column",
            Json(
              "type"  -> "column",
              "value" -> "invalid"
            )
          )
        )
        gridSearch.setEstimatorParameters(parameters)
        gridSearch.setNumberOfFolds(2)

        a[ColumnDoesNotExistError] should be thrownBy {
          gridSearch.inferKnowledgeUntyped(Vector(Knowledge(estimator), Knowledge(dataFrame), Knowledge(evaluator)))(
            executionContext.inferContext
          )
        }
      }

      "evaluator parameters are invalid" in {
        val gridSearch = new GridSearch()
        val estimator = new LinearRegression()
        val dataFrame = buildDataFrame()
        val evaluator = new RegressionEvaluator()
        val parameters = Json(
          evaluator.parameterValuesToJson.fields.updated(
            "label column",
            Json(
              "type"  -> "column",
              "value" -> "invalid"
            )
          )
        )
        gridSearch.setEvaluatorParameters(parameters)
        gridSearch.setNumberOfFolds(2)

        a[ColumnDoesNotExistError] should be thrownBy {
          gridSearch.inferKnowledgeUntyped(Vector(Knowledge(estimator), Knowledge(dataFrame), Knowledge(evaluator)))(
            executionContext.inferContext
          )
        }
      }
    }
  }


  private def buildDataFrame() = {
    val districtFactors = Seq(0.6, 0.8, 1.0)
    val priceForMeterSq = 7000
    val apartments = Range(40, 300, 5).map { flatSize =>
      val districtFactor = districtFactors(flatSize % districtFactors.length)
      Apartment(Vectors.dense(flatSize, districtFactor), (flatSize * districtFactor * priceForMeterSq).toLong)
    }
    DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(apartments))
  }

  private def pureSparkImplementation(): Array[Double] = {
    val lr = new ml.regression.LinearRegression()
    val paramGrid = new ParamGridBuilder().addGrid(lr.regParam, regularizationParameters).build()
    val cv = new CrossValidator()
      .setEstimator(lr)
      .setEvaluator(new evaluation.RegressionEvaluator())
      .setEstimatorParamMaps(paramGrid)
      .setNumFolds(2)
    val cvModel = cv.fit(buildDataFrame().sparkDataFrame)
    cvModel.avgMetrics
  }

  private def doubleToCell(d: Double) = Some(DoubleUtils.double2String(d))
}

private case class Apartment(features: Linalg.Vector, label: Double)
