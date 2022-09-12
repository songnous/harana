package com.harana.sdk.backend.models.flow.actionobjects.stringindexingwrapper

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.{SparkModelWrapper, Transformer}
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.shared.models.flow.parameters.ParameterMap
import org.apache.spark.ml
import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.types.StructType

/** Model wrapper adding 'string indexing' behaviour.
  *
  * Concrete models (like GBTClassificationModel) must be concrete classes (leaves in hierarchy). That's why this class must be abstract.
  */
abstract class StringIndexingWrapperModel[M <: ml.Model[M], E <: ml.Estimator[M]](
    private var wrappedModel: SparkModelWrapper[M, E]
) extends Transformer {

  private var pipelinedModel: PipelineModel = null

  def setPipelinedModel(pipelinedModel: PipelineModel): this.type = {
    this.pipelinedModel = pipelinedModel
    this
  }

  def setWrappedModel(wrappedModel: SparkModelWrapper[M, E]): this.type = {
    this.wrappedModel = wrappedModel
    this
  }

  override final def replicate(extra: ParameterMap): this.type = {
    val newWrappedModel = wrappedModel.replicate(extra)
    super
      .replicate(extra)
      .setPipelinedModel(pipelinedModel)
      .setWrappedModel(newWrappedModel)
      .asInstanceOf[this.type]
  }

  def applyTransform(ctx: ExecutionContext, df: DataFrame) =
    DataFrame.fromSparkDataFrame(pipelinedModel.transform(df.sparkDataFrame))

  override def applyTransformSchema(schema: StructType, inferContext: InferContext): Option[StructType] =
    wrappedModel._transformSchema(schema, inferContext)

  override def applyTransformSchema(schema: StructType): Option[StructType] =
    wrappedModel._transformSchema(schema)

  override def report(extended: Boolean = true) = wrappedModel.report(extended)

  val parameters = wrappedModel.parameters

  override def loadTransformer(ctx: ExecutionContext, path: String) = {
    val pipelineModelPath   = Transformer.stringIndexerPipelineFilePath(path)
    val wrappedModelPath    = Transformer.stringIndexerWrappedModelFilePath(path)
    val loadedPipelineModel = PipelineModel.load(pipelineModelPath)
    val loadedWrappedModel  = Transformer.load(ctx, wrappedModelPath)
    this
      .setPipelinedModel(loadedPipelineModel)
      .setWrappedModel(loadedWrappedModel.asInstanceOf[SparkModelWrapper[M, E]])
      .setParametersFromJson(loadedWrappedModel.parameterValuesToJson)
  }

  override def saveTransformer(ctx: ExecutionContext, path: String) = {
    val pipelineModelPath = Transformer.stringIndexerPipelineFilePath(path)
    val wrappedModelPath  = Transformer.stringIndexerWrappedModelFilePath(path)
    pipelinedModel.save(pipelineModelPath)
    wrappedModel.save(ctx, wrappedModelPath)
  }

  override def paramMap: ParameterMap = wrappedModel.paramMap

  override def defaultParamMap: ParameterMap = wrappedModel.defaultParamMap

}
