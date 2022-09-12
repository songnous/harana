package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.{HasSingleInPlaceParameter, HasSpecificParameters}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasInputColumnParameter

abstract class SparkSingleColumnParameterEstimatorWrapperInfo
    extends HasInputColumnParameter
    with HasSingleInPlaceParameter
    with HasSpecificParameters {

  override lazy val parameters =
    if (specificParameters == null) Array(inputColumnParameter, singleInPlaceChoiceParameter)
    else Array(inputColumnParameter, singleInPlaceChoiceParameter) ++ specificParameters

  def setNoInPlace(outputColumn: String): this.type = setSingleInPlaceChoice(NoInPlaceChoice().setOutputColumn(outputColumn))
}