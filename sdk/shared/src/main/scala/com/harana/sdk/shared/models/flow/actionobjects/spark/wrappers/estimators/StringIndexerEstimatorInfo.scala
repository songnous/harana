package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.{ActionObjectInfo, EstimatorInfo, SparkMultiColumnEstimatorWrapperInfo}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, Parameters}
import com.harana.sdk.shared.models.flow.utils.Id.fromString

import scala.language.reflectiveCalls

trait StringIndexerEstimatorInfo extends EstimatorInfo with SparkMultiColumnEstimatorWrapperInfo with Parameters {

  override val id = "6FE60894-A0A3-4790-AAFE-57A8EF780B40"

  setDefault(singleOrMultiChoiceParameter, SingleColumnChoice())

  override val specificParameters = Array.empty[Parameter[_]]
}

trait SingleStringIndexerInfo extends ActionObjectInfo with Parameters {

  val id = "EA600609-AC9D-4FA3-A1AD-D1D2A0089750"

  val specificParameters = Array.empty[Parameter[_]]
}

object StringIndexerEstimatorInfo extends StringIndexerEstimatorInfo