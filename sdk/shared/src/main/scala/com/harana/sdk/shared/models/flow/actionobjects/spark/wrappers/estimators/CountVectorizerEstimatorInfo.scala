package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasMinTermsFrequencyParameter
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, IntParameter, Parameter, ParameterGroup}

import scala.language.reflectiveCalls

trait CountVectorizerEstimatorInfo extends EstimatorInfo with HasMinTermsFrequencyParameter {

  val id = "5DC0071A-640B-4FFF-AFFB-8EEEE8EF33C9"

  val minDFParameter = DoubleParameter("min-different-documents", default = Some(1.0), validator = RangeValidator(0.0, Double.MaxValue))
  val vocabSizeParameter = IntParameter("max-vocabulary-size", default = Some(1 << 18), validator = RangeValidator(0, Int.MaxValue, beginIncluded = false, step = Some(1)))

  val specificParameters = Array[Parameter[_]](
    vocabSizeParameter,
    minDFParameter,
    minTFParameter
  )

}

object CountVectorizerEstimatorInfo extends CountVectorizerEstimatorInfo {
  override val parameterGroups = List.empty[ParameterGroup]
}