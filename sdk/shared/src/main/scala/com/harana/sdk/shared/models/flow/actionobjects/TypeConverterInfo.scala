package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.choice.ChoiceParameter

trait TypeConverterInfo extends MultiColumnTransformerInfo {

  override val id = "BC8E4362-4A81-4205-A5C1-2C6D650F7790"

  val targetTypeParameter = ChoiceParameter[TargetTypeChoice]("target-type")
  def getTargetType = $(targetTypeParameter)
  def setTargetType(value: TargetTypeChoice): this.type = set(targetTypeParameter, value)

  val specificParameters = Array(targetTypeParameter)

}

object TypeConverterInfo extends TypeConverterInfo