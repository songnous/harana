package com.harana.sdk.shared.models.flow.actionobjects.stringindexingwrapper

import com.harana.sdk.shared.models.flow.actionobjects.{SparkModelWrapperInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.parameters.ParameterMap

/** Model wrapper adding 'string indexing' behaviour.
  *
  * Concrete models (like GBTClassificationModel) must be concrete classes (leaves in hierarchy). That's why this class must be abstract.
  */
abstract class StringIndexingWrapperModelInfo(private var wrappedModel: SparkModelWrapperInfo) extends TransformerInfo {

  override val parameterGroups = wrappedModel.parameterGroups

  override def paramMap: ParameterMap = wrappedModel.paramMap
  override def defaultParamMap: ParameterMap = wrappedModel.defaultParamMap

}