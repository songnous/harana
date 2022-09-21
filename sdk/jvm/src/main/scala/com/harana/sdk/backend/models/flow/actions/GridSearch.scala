package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.actionobjects._
import com.harana.sdk.backend.models.flow.{ActionType3To1, ExecutionContext}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.wrappers.{EstimatorWrapper, EvaluatorWrapper}
import com.harana.sdk.backend.models.flow.actionobjects.{Estimator, Evaluator, Transformer}
import com.harana.sdk.backend.models.flow.parameters.wrappers.ParamWrapper
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.actions.GridSearchInfo
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.report.{ReportContent, ReportType, Table}
import com.harana.sdk.shared.models.flow.utils.{ColumnType, DoubleUtils}
import org.apache.spark.ml
import org.apache.spark.ml.param._
import org.apache.spark.ml.tuning.{CrossValidator, ParamGridBuilder}

import scala.reflect.runtime.universe.TypeTag

class GridSearch()
  extends ActionType3To1[Estimator[Transformer], DataFrame, Evaluator, Report]
  with GridSearchInfo {

  def execute(estimator: Estimator[Transformer], dataFrame: DataFrame, evaluator: Evaluator)(context: ExecutionContext) = {
    val estimatorParameters = estimator.parameterPairsFromJson(getEstimatorParameters)
    val estimatorWithParameters = createEstimatorWithParameters(estimator, estimatorParameters)
    val evaluatorWithParameters = createEvaluatorWithParameters(evaluator)
    validateDynamicParameters(estimatorWithParameters, evaluatorWithParameters)

    val estimatorWrapper = new EstimatorWrapper(context, estimatorWithParameters)
    val gridSearchParameters = createGridSearchParameters(estimatorWrapper.uid, estimatorParameters)
    val cv = new CrossValidator()
      .setEstimator(estimatorWrapper)
      .setEvaluator(new EvaluatorWrapper(context, evaluatorWithParameters))
      .setEstimatorParamMaps(gridSearchParameters)
      .setNumFolds(getNumberOfFolds)
    val cvModel = cv.fit(dataFrame.sparkDataFrame)
    createReport(gridSearchParameters, cvModel.avgMetrics, evaluator.isLargerBetter)
  }

  private def createEstimatorWithParameters(estimator: Estimator[Transformer], estimatorParams: Seq[ParameterPair[_]]): Estimator[Transformer] = {
    estimator.replicate().set(estimatorParams: _*)
  }

  private def createEvaluatorWithParameters(evaluator: Evaluator): Evaluator = {
    evaluator.replicate().setParametersFromJson($(evaluatorParameters), ignoreNulls = true)
  }

  private def createReport(gridSearchParams: Array[ParamMap], metrics: Array[Double], isLargerMetricBetter: Boolean): Report = {
    val paramsWithOrder: Seq[Param[_]] = gridSearchParams.head.toSeq.map(_.param).sortBy(_.name)
    val sortedMetrics: Seq[Metric] = sortParamsByMetricValue(gridSearchParams, metrics, isLargerMetricBetter)
    Report(ReportContent("Grid Search", ReportType.GridSearch, tables = Seq(
      bestParamsMetricsTable(paramsWithOrder, sortedMetrics.head),
      cvParamsMetricsTable(paramsWithOrder, sortedMetrics)
    )))
  }

  private def sortParamsByMetricValue(gridSearchParams: Array[ParamMap], metricValues: Array[Double], isLargerMetricBetter: Boolean): Seq[Metric] = {
    val metrics = gridSearchParams.zip(metricValues).map(new Metric(_)).toIndexedSeq
    val sorted = metrics.sortBy(_.metricValue)
    if (isLargerMetricBetter) {
      sorted.reverse
    } else {
      sorted
    }
  }

  private def bestParamsMetricsTable(paramsWithOrder: Seq[Param[_]], bestMetric: Metric): Table = {
    Table(
      name = "Best Params",
      description = "Best Parameters Values",
      columnNames = metricsTableColumnNames(paramsWithOrder),
      columnTypes = metricsTableColumnTypes(paramsWithOrder),
      rowNames = None,
      values = List(metricsTableRow(bestMetric.params, paramsWithOrder, bestMetric.metricValue))
    )
  }

  private def cvParamsMetricsTable(paramsWithOrder: Seq[Param[_]], params: Seq[Metric]): Table = {
    Table(
      name = "Params",
      description = "Parameters Values",
      columnNames = metricsTableColumnNames(paramsWithOrder),
      columnTypes = metricsTableColumnTypes(paramsWithOrder),
      rowNames = None,
      values = params.map { case metric =>
        metricsTableRow(metric.params, paramsWithOrder, metric.metricValue)
      }.toList
    )
  }

  private def metricsTableRow(cvParamMap: ParamMap, paramsWithOrder: Seq[Param[_]], metric: Double): List[Option[String]] = {
    paramMapToTableRow(cvParamMap, paramsWithOrder) :+ Some(DoubleUtils.double2String(metric))
  }

  private def metricsTableColumnTypes(paramsWithOrder: Seq[Param[_]]): List[ColumnType] = {
    (paramsWithOrder.map(_ => ColumnType.Numeric) :+ ColumnType.Numeric).toList
  }

  private def metricsTableColumnNames(paramsWithOrder: Seq[Param[_]]): Some[List[String]] = {
    Some((paramsWithOrder.map(_.name) :+ "Metric").toList)
  }

  private def paramMapToTableRow(paramMap: ParamMap, orderedParams: Seq[ml.param.Param[_]]): List[Option[String]] = {
    orderedParams.map(paramMap.get(_).map(_.toString)).toList
  }

  private def createGridSearchParameters(estimatorUID: String, params: Seq[ParameterPair[_]]): Array[ml.param.ParamMap] =
    params.filter(paramPair => paramPair.param.isGriddable).foldLeft(new ParamGridBuilder()) {
      case (builder, paramPair) =>
        val sparkParameter = new ParamWrapper(estimatorUID, paramPair.param)
        builder.addGrid(sparkParameter, paramPair.values)
    }.build()

  private case class Metric(params: ParamMap, metricValue: Double) {
    def this(pair: (ParamMap, Double)) = this(pair._1, pair._2)
  }

  lazy val tTagTO_0: TypeTag[Report] = typeTag
}