package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameter, ParameterGroup, Parameters}

trait IDFEstimatorInfo extends EstimatorInfo with Parameters {

  val id = "CED630D7-8E2C-4FFE-B869-4C28E8D7E375"

  val minDocFreqParameter = IntParameter("min-documents-frequency", default = Some(0), validator = RangeValidator(begin = 0, end = Int.MaxValue, step = Some(1)))

  val specificParameters = Array[Parameter[_]](minDocFreqParameter)

}

object IDFEstimatorInfo extends IDFEstimatorInfo {
  override val parameterGroups = List.empty[ParameterGroup]
}