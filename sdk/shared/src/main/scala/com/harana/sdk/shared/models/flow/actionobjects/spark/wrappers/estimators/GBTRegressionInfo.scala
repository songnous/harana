package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.GBTRegressionInfo.{LossType, Squared}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.GBTParameters
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasRegressionImpurityParameter
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}

import scala.language.reflectiveCalls

trait GBTRegressionInfo
  extends GBTParameters
    with EstimatorInfo
    with HasRegressionImpurityParameter {

  val id = "31A1E8F4-64FE-498A-9452-DF90D7F03B79"

  override val maxIterationsDefault = 20

  val lossTypeParameter = ChoiceParameter[LossType]("loss-function", default = Some(Squared()))

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

object GBTRegressionInfo extends GBTRegressionInfo {
  sealed abstract class LossType(val name: String) extends Choice {
    override val parameterGroups = List.empty[ParameterGroup]
    val choiceOrder: List[ChoiceOption] = List(classOf[Squared], classOf[Absolute])
  }

  case class Squared() extends LossType("squared")
  case class Absolute() extends LossType("absolute")
}