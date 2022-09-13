package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge, Method1To1}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.serialization.ParametersSerialization
import com.harana.sdk.backend.models.flow.actions.exceptions.WriteFileError
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.exceptions.HaranaError
import com.harana.sdk.shared.models.flow.report.ReportType
import org.apache.hadoop.fs.Path
import org.apache.spark.SparkException
import org.apache.spark.sql.types.StructType

trait Transformer extends TransformerInfo with ParametersSerialization {

  def applyTransform(ctx: ExecutionContext, df: DataFrame): DataFrame

  final def _transform(ctx: ExecutionContext, df: DataFrame) = applyTransform(ctx, df)

  def applyTransformSchema(schema: StructType): Option[StructType] = None
  def applyTransformSchema(schema: StructType, inferContext: InferContext): Option[StructType] = applyTransformSchema(schema)

  final def _transformSchema(schema: StructType): Option[StructType] = applyTransformSchema(schema)
  final def _transformSchema(schema: StructType, inferContext: InferContext): Option[StructType] = applyTransformSchema(schema, inferContext)

  def transform: Method1To1[Unit, DataFrame, DataFrame] = {
    new Method1To1[Unit, DataFrame, DataFrame] {
      def apply(ctx: ExecutionContext)(p: Unit)(df: DataFrame) = applyTransform(ctx, df)

      override def infer(ctx: InferContext)(p: Unit)(k: Knowledge[DataFrame]) = {
        val df = DataFrame.forInference(k.single.schema.flatMap(s => applyTransformSchema(s, ctx)))
        (Knowledge(df), InferenceWarnings.empty)
      }
    }
  }

  override def report(extended: Boolean = true) =
    super
      .report(extended)
      .withReportName(s"$transformerName Report")
      .withReportType(ReportType.Model)
      .withAdditionalTable(CommonTablesGenerators.parameters(extractParameterMap()))

   def transformerName = this.getClass.getSimpleName

  def save(ctx: ExecutionContext, path: String) =
    try {
      saveObjectWithParameters(ctx, path)
      saveTransformer(ctx, path)
    } catch {
      case e: SparkException =>
        println(s"Saving Transformer error: Spark problem. Unable to write file to $path", e)
        throw WriteFileError(path, HaranaError.throwableToDetails(Some(e))).toException
    }

  def load(ctx: ExecutionContext, path: String) = loadTransformer(ctx, path).loadAndSetParameters(ctx, path)
  def saveTransformer(ctx: ExecutionContext, path: String) = {}
  def loadTransformer(ctx: ExecutionContext, path: String): this.type = this
}

object Transformer extends Logging {
  def load(ctx: ExecutionContext, path: String) = {
    println("Loading transformer from: {}", path)
    ParametersSerialization.load(ctx, path).asInstanceOf[Transformer]
  }

  def modelFilePath(path: String) = new Path(path, "haranaModel").toString
  def parentEstimatorFilePath(path: String) = new Path(path, "haranaParentEstimator").toString
  def stringIndexerPipelineFilePath(path: String) = new Path(modelFilePath(path), "haranaPipeline").toString
  def stringIndexerWrappedModelFilePath(path: String) = new Path(modelFilePath(path), "haranaWrappedModel").toString
  def transformerSparkTransformerFilePath(path: String) = new Path(path, "haranaTransformer").toString
}
