package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.{MultiColumnTransformer, MultiColumnTransformerTestSupport}

trait MultiColumnTransformerWrapperTestSupport extends MultiColumnTransformerTestSupport {
  self: AbstractTransformerWrapperSmokeTest[MultiColumnTransformer] =>

  def transformerName = className

  def transformer: MultiColumnTransformer = transformerWithParameters

}
