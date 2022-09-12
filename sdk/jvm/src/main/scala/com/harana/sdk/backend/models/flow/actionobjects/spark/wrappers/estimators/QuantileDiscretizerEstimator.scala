package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkSingleColumnParameterEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.QuantileDiscretizerModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.QuantileDiscretizerEstimatorInfo
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameter}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import org.apache.spark.ml.feature.{Bucketizer => SparkQuantileDiscretizerModel, QuantileDiscretizer => SparkQuantileDiscretizer}

import scala.language.reflectiveCalls

class QuantileDiscretizerEstimator
    extends SparkSingleColumnParameterEstimatorWrapper[
      SparkQuantileDiscretizerModel,
      SparkQuantileDiscretizer,
      QuantileDiscretizerModel] with QuantileDiscretizerEstimatorInfo
