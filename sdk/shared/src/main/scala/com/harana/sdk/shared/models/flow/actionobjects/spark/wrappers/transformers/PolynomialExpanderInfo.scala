package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.actionobjects.{ActionObjectInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameter, ParameterGroup, Parameters}

trait PolynomialExpanderInfo extends ActionObjectInfo with TransformerInfo with Parameters {

  val id = "AA63DE9C-B825-4E5A-AB54-1244BD3E3AEA"

  val degreeParameter = IntParameter("degree", default = Some(2), validator = RangeValidator(2, Int.MaxValue, step = Some(1)))
  def getDegree = $(degreeParameter)

  val specificParameters = Array[Parameter[_]](degreeParameter)

}

object PolynomialExpanderInfo extends PolynomialExpanderInfo {
  override val parameterGroups = List.empty[ParameterGroup]
}