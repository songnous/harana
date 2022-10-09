package com.harana.sdk.backend.models.flow.actionobjects.stringindexingwrapper

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.serialization.{SerializableSparkEstimator, SerializableSparkModel}
import com.harana.sdk.backend.models.flow.actionobjects.{Estimator, ParametersWithSparkWrappers, SparkEstimatorWrapper, SparkModelWrapper}
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasLabelColumnParameter, HasPredictionColumnCreatorParameter}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterMap}
import com.harana.sdk.shared.models.flow.utils.TypeUtils
import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import scala.reflect.ClassTag
import izumi.reflect.Tag

/** Some spark action assume their input was string-indexed. User-experience suffers from this requirement. We can work around it by wrapping
  * estimation in `StringIndexerEstimatorWrapper`. `StringIndexerEstimatorWrapper` plugs in StringIndexer before action. It also makes it
  * transparent for clients' components by reverting string indexing with labelConverter.
  */
abstract class StringIndexingEstimatorWrapper[M <: ml.Model[M], E <: ml.Estimator[M], MW <: SparkModelWrapper[
  M,
  E
], SIWP <: StringIndexingWrapperModel[M, E]](
    private var wrappedEstimator: SparkEstimatorWrapper[M, E, MW] with HasLabelColumnParameter with HasPredictionColumnCreatorParameter
)(implicit
    val sparkModelClassTag: ClassTag[M],
    val modelWrapperTag: Tag[MW],
    val estimatorTag: Tag[E],
    val sparkModelTag: Tag[M],
    val stringIndexingWrapperModelTag: Tag[SIWP]
) extends Estimator[SIWP] with ParametersWithSparkWrappers {

  // FIXME
  //  final val parameters = wrappedEstimator.parameters

  final override def report(extended: Boolean = true) = wrappedEstimator.report(extended)

  final def sparkClassCanonicalName = wrappedEstimator.serializableEstimator.sparkEstimator.getClass.getCanonicalName

  private def setWrappedEstimator(wrappedEstimator: SparkEstimatorWrapper[M, E, MW] with HasLabelColumnParameter with HasPredictionColumnCreatorParameter) = {
    this.wrappedEstimator = wrappedEstimator
    this
  }

  override final def replicate(extra: ParameterMap) = {
    val newWrappedEstimator = wrappedEstimator.replicate(extra)
    super
      .replicate(extra)
      .setWrappedEstimator(newWrappedEstimator)
      .asInstanceOf[this.type]
  }

  override def _fit(ctx: ExecutionContext, df: DataFrame): SIWP = {
    val labelColumnName = df.getColumnName(wrappedEstimator.getLabelColumn)
    val predictionColumnName = wrappedEstimator.getPredictionColumn
    val serializableSparkEstimator = new SerializableSparkEstimator[M, E](wrappedEstimator.sparkEstimator)
    val pipeline = StringIndexingPipeline(df, serializableSparkEstimator, labelColumnName, predictionColumnName)
    val sparkDataFrame = df.sparkDataFrame
    val paramMap = sparkParamMap(wrappedEstimator.sparkEstimator, sparkDataFrame.schema)
    val pipelineModel = pipeline.fit(sparkDataFrame, paramMap)

    val sparkModel = {
      val transformer = pipelineModel.stages.find {
        case s: SerializableSparkModel[_] => sparkModelClassTag.runtimeClass.isInstance(s.sparkModel)
        case t => sparkModelClassTag.runtimeClass.isInstance(t)
      }.get
      transformer.asInstanceOf[SerializableSparkModel[M]]
    }

    val sparkModelWrapper = TypeUtils
      .instanceOfType(modelWrapperTag)
      .setParent(wrappedEstimator.replicate(extractParameterMap()))
      .setModel(sparkModel)

    val stringIndexingModelWrapper = TypeUtils
      .instanceOfType(stringIndexingWrapperModelTag)
      .setPipelinedModel(pipelineModel)
      .setWrappedModel(sparkModelWrapper)

    stringIndexingModelWrapper
  }

  override def _fit_infer(schemaOpt: Option[StructType]): SIWP = {
    validateSparkEstimatorParameters(wrappedEstimator.sparkEstimator, schemaOpt)
    val model = wrappedEstimator.createModelWrapperInstance().setParent(wrappedEstimator.replicate(extractParameterMap()))
    TypeUtils.instanceOfType(stringIndexingWrapperModelTag).setWrappedModel(model)
  }

  override def paramMap: ParameterMap = wrappedEstimator.paramMap

  override def defaultParamMap: ParameterMap = wrappedEstimator.defaultParamMap

}
