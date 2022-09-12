package com.harana.sdk.shared.models.flow.actionobjects.multicolumn

import SingleColumnParameters.SingleColumnInPlaceChoice
import SingleColumnParameters.SingleTransformInPlaceChoices.YesInPlaceChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleColumnInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.Parameters
import com.harana.sdk.shared.models.flow.parameters.choice.ChoiceParameter

trait HasSingleInPlaceParameter extends Parameters {

  val singleInPlaceChoiceParameter = ChoiceParameter[SingleColumnInPlaceChoice]("output", Some("Output generation mode."))
  def getSingleInPlaceChoice = $(singleInPlaceChoiceParameter)
  def setSingleInPlaceChoice(value: SingleColumnInPlaceChoice): this.type = set(singleInPlaceChoiceParameter -> value)

  setDefault(singleInPlaceChoiceParameter -> YesInPlaceChoice())

}
