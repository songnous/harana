package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.{HasSingleInPlaceParameter, HasSpecificParameters}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasInputColumnParameter
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup

import scala.language.reflectiveCalls

trait SparkSingleColumnParameterModelWrapperInfo extends SparkModelWrapperInfo
    with ActionObjectInfo
    with HasInputColumnParameter
    with HasSingleInPlaceParameter
    with HasSpecificParameters {

   def convertInputNumericToVector: Boolean = false
   def convertOutputVectorToDouble: Boolean = false

  private var outputColumnValue: Option[String] = None

  lazy override val parameterGroups = {
    val parameters =
      if (specificParameters == null)
        List(inputColumnParameter, singleInPlaceChoiceParameter)
      else
        List(inputColumnParameter, singleInPlaceChoiceParameter) ++ specificParameters

    List(ParameterGroup("", parameters: _*))
  }
}