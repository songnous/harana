package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameter}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

trait GetFromVectorTransformerInfo extends MultiColumnTransformerInfo {

  override val id = "32BAD033-9534-45EF-B6BF-DB1340C8B930"

  val indexParameter = IntParameter("index", Some("Index of value to extract (starting from 0)."), validator = RangeValidator.positiveIntegers)
  setDefault(indexParameter, 0)
  def getIndex = $(indexParameter)
  def setIndex(value: Int): this.type = set(indexParameter, value)

  val specificParameters = Array[Parameter[_]](indexParameter)

}

object GetFromVectorTransformerInfo extends GetFromVectorTransformerInfo