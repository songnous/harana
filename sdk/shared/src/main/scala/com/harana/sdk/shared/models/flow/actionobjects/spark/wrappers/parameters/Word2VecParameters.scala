package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasMaxIterationsParameter, HasSeedParameter, HasStepSizeParameter}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}

import scala.language.reflectiveCalls

trait Word2VecParameters extends Parameters
  with HasMaxIterationsParameter
  with HasStepSizeParameter
  with HasSeedParameter {

  val vectorSizeParameter = IntParameter("vector-size", default = Some(100), validator = RangeValidator.positiveIntegers)
  def setVectorSize(value: Int): this.type = set(vectorSizeParameter -> value)

  val numPartitionsParameter = IntParameter("num-partitions", default = Some(1), validator = RangeValidator.positiveIntegers)

  val minCountParameter = IntParameter("min-count", default = Some(5), validator = RangeValidator.positiveIntegers)
  def setMinCount(value: Int): this.type = set(minCountParameter -> value)

}