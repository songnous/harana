package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import NaiveBayesInfo.{ModelType, Multinomial}
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.actionobjects.SparkEstimatorWrapperInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasLabelColumnParameter, ProbabilisticClassifierParameters}
import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameter, ParameterGroup}
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait NaiveBayesInfo
  extends SparkEstimatorWrapperInfo
    with ProbabilisticClassifierParameters
    with HasLabelColumnParameter {

  val id = "D0B14CE9-4DDF-4A74-B86C-63D9872C95F1"

  val smoothingParameter = DoubleParameter("smoothing", default = Some(1.0), validator = RangeValidator(begin = 0.0, end = Double.MaxValue))
  val modelTypeParameter = ChoiceParameter[ModelType]("modelType", default = Some(Multinomial()))

  val parameterGroups = List(ParameterGroup(None,
    smoothingParameter,
    modelTypeParameter,
    labelColumnParameter,
    featuresColumnParameter,
    probabilityColumnParameter,
    rawPredictionColumnParameter,
    predictionColumnParameter
  ))
}

object NaiveBayesInfo extends NaiveBayesInfo {
  sealed abstract class ModelType(val name: String) extends Choice {
    val choiceOrder: List[ChoiceOption] = List(classOf[Multinomial], classOf[Bernoulli])
    val parameterGroups = List.empty[ParameterGroup]
  }

  case class Multinomial() extends ModelType("multinomial")
  case class Bernoulli() extends ModelType("bernoulli")
}