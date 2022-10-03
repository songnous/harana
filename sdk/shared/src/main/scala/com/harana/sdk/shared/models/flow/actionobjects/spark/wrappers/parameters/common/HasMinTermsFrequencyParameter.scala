package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.DoubleParameter
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasMinTermsFrequencyParameter extends HasInputColumnParameter with HasOutputColumnParameter {

  val minTFParameter = DoubleParameter("min-term-frequency", default = Some(1.0), validator = RangeValidator(0.0, Double.MaxValue))
  def setMinTF(value: Double): this.type = set(minTFParameter, value)

}
