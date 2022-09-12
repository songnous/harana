package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasNumberOfClustersParameter extends Parameters {

  val kParameter = IntParameter("k", Some("The number of clusters to create."),
    validator = RangeValidator(begin = 2, end = Int.MaxValue, step = Some(1))
  )

  setDefault(kParameter, 2)
}