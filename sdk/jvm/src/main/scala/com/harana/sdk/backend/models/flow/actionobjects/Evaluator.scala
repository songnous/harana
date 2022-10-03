package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge, Method1To1}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.actionobjects.{ActionObjectInfo, EvaluatorInfo, MetricValue}
import com.harana.sdk.shared.models.flow.parameters.Parameters
import com.harana.sdk.shared.models.flow.report.ReportType

abstract class Evaluator extends ActionObjectInfo with EvaluatorInfo with Parameters {

  def _evaluate(context: ExecutionContext, dataFrame: DataFrame): MetricValue

  def _infer(k: Knowledge[DataFrame]): MetricValue

  def evaluate: Method1To1[Unit, DataFrame, MetricValue] = {
    new Method1To1[Unit, DataFrame, MetricValue] {
      def apply(ctx: ExecutionContext)(p: Unit)(dataFrame: DataFrame): MetricValue = _evaluate(ctx, dataFrame)
      override def infer(ctx: InferContext)(p: Unit)(k: Knowledge[DataFrame]) = (Knowledge[MetricValue](_infer(k)), InferenceWarnings.empty)
    }
  }

  override def report(extended: Boolean = true) =
    super
      .report(extended)
      .withReportName(s"${this.getClass.getSimpleName} Report")
      .withReportType(ReportType.Evaluator)
      .withAdditionalTable(CommonTablesGenerators.parameters(extractParameterMap()))

  def isLargerBetter: Boolean
}