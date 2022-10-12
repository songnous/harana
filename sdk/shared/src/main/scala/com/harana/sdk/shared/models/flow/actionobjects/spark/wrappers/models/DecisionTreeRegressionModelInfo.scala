package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.SparkModelWrapperInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasFeaturesColumnParameter, HasPredictionColumnCreatorParameter}
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup

trait DecisionTreeRegressionModelInfo
    extends SparkModelWrapperInfo
    with HasFeaturesColumnParameter
    with HasPredictionColumnCreatorParameter {

  val id = "324FFAB6-4EB0-4B14-8106-99C294400C3C"

  override val parameterGroups = List(ParameterGroup("", featuresColumnParameter, predictionColumnParameter))

}

object DecisionTreeRegressionModelInfo extends DecisionTreeRegressionModelInfo