package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge, Method1To1}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.parameters.Parameters
import com.harana.sdk.shared.models.flow.report.ReportType
import org.apache.spark.sql.types.StructType

import scala.reflect.runtime.universe.TypeTag

abstract class Estimator[+T <: Transformer]()(implicit typeTag: TypeTag[T]) extends ActionObjectInfo with Parameters {

  def convertInputNumericToVector: Boolean = false
  def convertOutputVectorToDouble: Boolean = false

  def _fit(ctx: ExecutionContext, df: DataFrame): T
  def _fit_infer(schema: Option[StructType]): T

  def fit: Method1To1[Unit, DataFrame, T] = {
    new Method1To1[Unit, DataFrame, T] {
      def apply(ctx: ExecutionContext)(p: Unit)(df: DataFrame): T = _fit(ctx, df)

      override def infer(ctx: InferContext)(p: Unit)(k: Knowledge[DataFrame]) = {
        val transformer = _fit_infer(k.single.schema)
        (Knowledge(transformer), InferenceWarnings.empty)
      }
    }
  }

  override def report(extended: Boolean = true) =
    super
      .report(extended)
      .withReportName(s"$estimatorName Report")
      .withReportType(ReportType.Estimator)
      .withAdditionalTable(CommonTablesGenerators.parameters(extractParameterMap()))

  def estimatorName = this.getClass.getSimpleName
}