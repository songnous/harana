package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import NaiveBayesInfo.{ModelType, Multinomial}
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.actionobjects.SparkEstimatorWrapperInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasLabelColumnParameter, ProbabilisticClassifierParameters}
import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameter}
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait NaiveBayesInfo
  extends SparkEstimatorWrapperInfo
    with ProbabilisticClassifierParameters
    with HasLabelColumnParameter {

  val id = "D0B14CE9-4DDF-4A74-B86C-63D9872C95F1"

  val smoothingParameter = DoubleParameter("smoothing", Some("The smoothing parameter."),
    validator = RangeValidator(begin = 0.0, end = Double.MaxValue)
  )
  setDefault(smoothingParameter, 1.0)

  val modelTypeParameter = ChoiceParameter[ModelType]("modelType", Some("The model type."))
  setDefault(modelTypeParameter, Multinomial())

  val parameters = Array(
    smoothingParameter,
    modelTypeParameter,
    labelColumnParameter,
    featuresColumnParameter,
    probabilityColumnParameter,
    rawPredictionColumnParameter,
    predictionColumnParameter
  )
}

object NaiveBayesInfo extends NaiveBayesInfo {
  sealed abstract class ModelType(val name: String) extends Choice {
    val choiceOrder: List[ChoiceOption] = List(classOf[Multinomial], classOf[Bernoulli])
    val parameters = Array.empty[Parameter[_]]
  }

  case class Multinomial() extends ModelType("multinomial")
  case class Bernoulli() extends ModelType("bernoulli")
}