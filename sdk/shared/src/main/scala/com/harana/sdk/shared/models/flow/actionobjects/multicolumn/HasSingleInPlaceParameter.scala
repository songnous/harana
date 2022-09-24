package com.harana.sdk.shared.models.flow.actionobjects.multicolumn

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleColumnInPlaceChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.YesInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.Parameters
import com.harana.sdk.shared.models.flow.parameters.choice.ChoiceParameter

trait HasSingleInPlaceParameter extends Parameters {

  val singleInPlaceChoiceParameter = ChoiceParameter[SingleColumnInPlaceChoice]("output", default = Some(YesInPlaceChoice()))
  def getSingleInPlaceChoice = $(singleInPlaceChoiceParameter)
  def setSingleInPlaceChoice(value: SingleColumnInPlaceChoice): this.type = set(singleInPlaceChoiceParameter -> value)

}
