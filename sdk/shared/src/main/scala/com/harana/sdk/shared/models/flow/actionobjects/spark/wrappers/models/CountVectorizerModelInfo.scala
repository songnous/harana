package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasMinTermsFrequencyParameter
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup}

trait CountVectorizerModelInfo extends TransformerInfo with HasMinTermsFrequencyParameter {

  val id = "48F5810E-8497-43E2-94FD-331FCC500465"

  val specificParameters = Array[Parameter[_]](minTFParameter)

}

object CountVectorizerModelInfo extends CountVectorizerModelInfo {
  override val parameterGroups = List.empty[ParameterGroup]
}