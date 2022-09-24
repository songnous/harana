package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.{HasSingleInPlaceParameter, HasSpecificParameters}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasInputColumnParameter

import scala.language.reflectiveCalls

trait SparkSingleColumnParameterModelWrapperInfo extends SparkModelWrapperInfo
    with ActionObjectInfo
    with HasInputColumnParameter
    with HasSingleInPlaceParameter
    with HasSpecificParameters {

   def convertInputNumericToVector: Boolean = false
   def convertOutputVectorToDouble: Boolean = false

  private var outputColumnValue: Option[String] = None

  lazy val parameters =
    Left(
      if (specificParameters == null) List(inputColumnParameter, singleInPlaceChoiceParameter)
      else List(inputColumnParameter, singleInPlaceChoiceParameter) ++ specificParameters
    )
}
