package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.MinMaxParameters
import com.harana.sdk.shared.models.flow.parameters.Parameter

trait MinMaxScalerModelInfo extends TransformerInfo with MinMaxParameters {

  val id = "3F1C87D3-3B6F-4E5C-92C6-60CB214D75DF"

  val specificParameters = Array[Parameter[_]](
    minParameter,
    maxParameter
  )
}

object MinMaxScalerModelInfo extends MinMaxScalerModelInfo {
  val parameters = Left(Array.empty[Parameter[_]])
}