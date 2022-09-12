package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasMaxDepthParameter extends Parameters {

  val maxDepthParameter = IntParameter("max depth", Some("The maximum depth of the tree. e.g. depth 0 means 1 leaf node; depth 1 means 1 internal node + 2 leaf nodes."),
    RangeValidator(0, 30, step = Some(1))
  )

  setDefault(maxDepthParameter, 5)
}