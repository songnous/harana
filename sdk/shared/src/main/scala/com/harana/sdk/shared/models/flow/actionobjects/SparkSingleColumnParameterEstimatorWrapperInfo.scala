package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.{HasSingleInPlaceParameter, HasSpecificParameters}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasInputColumnParameter
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup

abstract class SparkSingleColumnParameterEstimatorWrapperInfo
    extends HasInputColumnParameter
    with HasSingleInPlaceParameter
    with HasSpecificParameters {

  override lazy val parameterGroups = {
    val parameters =
      if (specificParameters == null) List(inputColumnParameter, singleInPlaceChoiceParameter)
      else List(inputColumnParameter, singleInPlaceChoiceParameter) ++ specificParameters

    List(ParameterGroup("", parameters: _*))
  }

  def setNoInPlace(outputColumn: String): this.type = setSingleInPlaceChoice(NoInPlaceChoice().setOutputColumn(outputColumn))
}