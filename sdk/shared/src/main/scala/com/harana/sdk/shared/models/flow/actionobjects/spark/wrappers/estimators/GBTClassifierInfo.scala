package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.GBTClassifierInfo.{Logistic, LossType}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.GBTParameters
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasClassificationImpurityParameter
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}

trait GBTClassifierInfo extends EstimatorInfo with GBTParameters with HasClassificationImpurityParameter {

  val id = "11A20465-F5F4-4EE4-83BE-5C08642EC65B"

  override val maxIterationsDefault = 10

  val lossTypeParameter = ChoiceParameter[LossType]("loss-function", default = Some(Logistic()))

  override val parameterGroups = List(ParameterGroup("",
    impurityParameter,
    lossTypeParameter,
    maxBinsParameter,
    maxDepthParameter,
    maxIterationsParameter,
    minInfoGainParameter,
    minInstancesPerNodeParameter,
    seedParameter,
    stepSizeParameter,
    subsamplingRateParameter,
    labelColumnParameter,
    featuresColumnParameter,
    predictionColumnParameter))
}

object GBTClassifierInfo extends GBTClassifierInfo {
  sealed abstract class LossType(val name: String) extends Choice {
    val choiceOrder: List[ChoiceOption] = List(classOf[Logistic])
    override val parameterGroups = List.empty[ParameterGroup]
  }

  case class Logistic() extends LossType("logistic")
}
