package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasMinTermsFrequencyParameter
import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, IntParameter, Parameter}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait CountVectorizerEstimatorInfo extends EstimatorInfo with HasMinTermsFrequencyParameter {

  val id = "5DC0071A-640B-4FFF-AFFB-8EEEE8EF33C9"

  val minDFParameter = DoubleParameter("min different documents", RangeValidator(0.0, Double.MaxValue))
  setDefault(minDFParameter, 1.0)

  val vocabSizeParameter = IntParameter("max vocabulary size", RangeValidator(0, Int.MaxValue, beginIncluded = false, step = Some(1)))
  setDefault(vocabSizeParameter, (1 << 18).toInt)

  val specificParameters = Array[Parameter[_]](
    vocabSizeParameter,
    minDFParameter,
    minTFParameter
  )

}

object CountVectorizerEstimatorInfo extends CountVectorizerEstimatorInfo {
  val parameters = Left(Array.empty[Parameter[_]])
}