package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkSingleColumnParameterEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.MinMaxScalerModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.MinMaxScalerEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasInputColumnParameter, HasOutputColumnParameter, MinMaxParameters}
import com.harana.sdk.shared.models.flow.parameters.Parameter
import org.apache.spark.ml.feature.{MinMaxScaler => SparkMinMaxScaler, MinMaxScalerModel => SparkMinMaxScalerModel}

class MinMaxScalerEstimator
    extends SparkSingleColumnParameterEstimatorWrapper[SparkMinMaxScalerModel, SparkMinMaxScaler, MinMaxScalerModel]
    with MinMaxScalerEstimatorInfo {

  override def convertInputNumericToVector = true
  override def convertOutputVectorToDouble = true
}