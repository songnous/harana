package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasElasticNetParameter extends Parameters {

  val elasticNetParameter = DoubleParameter("elastic net param", Some("The ElasticNet mixing parameter. For alpha = 0, the penalty is an L2 penalty. For alpha = 1, it is an L1 penalty."),
    validator = RangeValidator(0.0, 1.0)
  )

  setDefault(elasticNetParameter, 0.0)
}