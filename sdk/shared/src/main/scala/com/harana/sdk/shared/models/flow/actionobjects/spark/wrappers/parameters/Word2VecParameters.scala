package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasMaxIterationsParameter, HasSeedParameter, HasStepSizeParameter}
import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait Word2VecParameters extends Parameters
  with HasMaxIterationsParameter
  with HasStepSizeParameter
  with HasSeedParameter {

  val vectorSizeParameter = IntParameter("vector size", validator = RangeValidator.positiveIntegers)
  setDefault(vectorSizeParameter -> 100)
  def setVectorSize(value: Int): this.type = set(vectorSizeParameter -> value)

  val numPartitionsParameter = IntParameter("num partitions", validator = RangeValidator.positiveIntegers)
  setDefault(numPartitionsParameter -> 1)

  val minCountParameter = IntParameter("min count", validator = RangeValidator.positiveIntegers)
  setDefault(minCountParameter -> 5)
  def setMinCount(value: Int): this.type = set(minCountParameter -> value)

}