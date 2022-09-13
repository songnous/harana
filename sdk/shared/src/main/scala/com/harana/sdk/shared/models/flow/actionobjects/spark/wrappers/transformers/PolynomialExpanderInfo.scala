package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.{ActionObjectInfo, parameters}
import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

trait PolynomialExpanderInfo extends ActionObjectInfo
  with TransformerInfo
  with Parameters {

  val id = "AA63DE9C-B825-4E5A-AB54-1244BD3E3AEA"

  val degreeParameter = IntParameter("degree", Some("The polynomial degree to expand."),
    validator = RangeValidator(2, Int.MaxValue, step = Some(1))
  )

  def getDegree = $(degreeParameter)
  setDefault(degreeParameter, 2)

  val specificParameters = Array[Parameter[_]](degreeParameter)

}

object PolynomialExpanderInfo extends PolynomialExpanderInfo {
  val parameters = Array.empty
}