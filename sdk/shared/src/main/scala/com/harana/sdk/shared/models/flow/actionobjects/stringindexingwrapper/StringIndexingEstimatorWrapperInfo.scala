package com.harana.sdk.shared.models.flow.actionobjects.stringindexingwrapper

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasLabelColumnParameter, HasPredictionColumnCreatorParameter}
import com.harana.sdk.shared.models.flow.actionobjects.{EstimatorInfo, SparkEstimatorWrapperInfo}
import com.harana.sdk.shared.models.flow.parameters.ParameterMap

/** Some spark action assume their input was string-indexed. User-experience suffers from this requirement. We can work around it by wrapping
  * estimation in `StringIndexerEstimatorWrapper`. `StringIndexerEstimatorWrapper` plugs in StringIndexer before action. It also makes it
  * transparent for clients' components by reverting string indexing with labelConverter.
  */
abstract class StringIndexingEstimatorWrapperInfo(private var wrappedEstimator: SparkEstimatorWrapperInfo with HasLabelColumnParameter with HasPredictionColumnCreatorParameter) extends EstimatorInfo {

  final override val parameterGroups = wrappedEstimator.parameterGroups

  override def paramMap: ParameterMap = wrappedEstimator.paramMap

  override def defaultParamMap: ParameterMap = wrappedEstimator.defaultParamMap

}