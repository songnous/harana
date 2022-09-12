package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkSingleColumnParameterEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.StandardScalerModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.StandardScalerEstimatorInfo
import com.harana.sdk.shared.models.flow.parameters.Parameter
import org.apache.spark.ml.feature.{StandardScaler => SparkStandardScaler, StandardScalerModel => SparkStandardScalerModel}

class StandardScalerEstimator
  extends SparkSingleColumnParameterEstimatorWrapper[SparkStandardScalerModel, SparkStandardScaler, StandardScalerModel]
  with StandardScalerEstimatorInfo {

  override def convertInputNumericToVector: Boolean = true
  override def convertOutputVectorToDouble: Boolean = true

}
