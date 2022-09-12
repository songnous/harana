package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

trait VectorIndexerEstimatorInfo extends EstimatorInfo with Parameters {

  val id = "8A163FA6-170D-4953-A0D1-89259DA59CEA"

  val maxCategoriesParameter = IntParameter(
    name = "max categories",
    description = Some("""The threshold for the number of values a categorical feature can take.
                         |If a feature is found to have more values, then it is declared continuous.""".stripMargin),
    validator = RangeValidator(begin = 2, end = Int.MaxValue, step = Some(1))
  )

  setDefault(maxCategoriesParameter, 20)
  def setMaxCategories(value: Int): this.type = set(maxCategoriesParameter -> value)

  val specificParameters = Array[Parameter[_]](maxCategoriesParameter)

}

object VectorIndexerEstimatorInfo extends VectorIndexerEstimatorInfo {
  val parameters = Array.empty[Parameter[_]]
}