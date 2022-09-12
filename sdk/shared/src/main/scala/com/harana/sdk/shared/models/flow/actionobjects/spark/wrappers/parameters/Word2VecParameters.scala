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

  val vectorSizeParameter = IntParameter("vector size", Some("The dimension of codes after transforming from words."),
    validator = RangeValidator.positiveIntegers
  )
  setDefault(vectorSizeParameter -> 100)
  def setVectorSize(value: Int): this.type = set(vectorSizeParameter -> value)

  val numPartitionsParameter = IntParameter("num partitions", Some("The number of partitions for sentences of words."),
    validator = RangeValidator.positiveIntegers
  )
  setDefault(numPartitionsParameter -> 1)

  val minCountParameter = IntParameter(
    name = "min count",
    description = Some("The minimum number of occurences of a token to be included in the model's vocabulary."),
    validator = RangeValidator.positiveIntegers
  )
  setDefault(minCountParameter -> 5)
  def setMinCount(value: Int): this.type = set(minCountParameter -> value)
}
