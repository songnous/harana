package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.KMeansInfo.{KMeansInitMode, ParallelInitMode}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common._
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameter}

trait KMeansInfo
    extends ActionObjectInfo
    with EstimatorInfo
    with PredictorParameters
    with HasNumberOfClustersParameter
    with HasMaxIterationsParameter
    with HasSeedParameter
    with HasToleranceParameter {

  val id = "CE33AA95-7523-4626-AFB4-B21FBCF98783"

  override val maxIterationsDefault = 20
  override val toleranceDefault = 1e-4

  val initModeParameter = ChoiceParameter[KMeansInitMode]("init mode")
  setDefault(initModeParameter, ParallelInitMode())

  val initStepsParameter = IntParameter("init steps", validator = RangeValidator(begin = 1, end = Int.MaxValue, step = Some(1)))
  setDefault(initStepsParameter, 5)

  val parameters = Array(
    kParameter,
    maxIterationsParameter,
    seedParameter,
    toleranceParameter,
    initModeParameter,
    initStepsParameter,
    featuresColumnParameter,
    predictionColumnParameter
  )
}

object KMeansInfo extends KMeansInfo {
  sealed trait KMeansInitMode extends Choice {
    val choiceOrder: List[ChoiceOption] = List(classOf[RandomInitMode], classOf[ParallelInitMode])
    val parameters = Array.empty[Parameter[_]]
  }

  case class RandomInitMode() extends KMeansInitMode {
    val name = "random"
  }

  case class ParallelInitMode() extends KMeansInitMode {
    val name = "k-means||"
  }
}