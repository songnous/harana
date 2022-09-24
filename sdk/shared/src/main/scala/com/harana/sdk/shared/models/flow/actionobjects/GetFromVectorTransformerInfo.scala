package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameter}

trait GetFromVectorTransformerInfo extends MultiColumnTransformerInfo {

  override val id = "32BAD033-9534-45EF-B6BF-DB1340C8B930"

  val indexParameter = IntParameter("index", default = Some(0), validator = RangeValidator.positiveIntegers)
  def getIndex = $(indexParameter)
  def setIndex(value: Int): this.type = set(indexParameter, value)

  val specificParameters = Array[Parameter[_]](indexParameter)

}

object GetFromVectorTransformerInfo extends GetFromVectorTransformerInfo