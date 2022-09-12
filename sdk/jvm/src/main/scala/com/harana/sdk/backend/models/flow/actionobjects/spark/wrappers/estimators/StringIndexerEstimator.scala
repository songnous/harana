package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.{MultiColumnStringIndexerModel, SingleColumnStringIndexerModelParameter, StringIndexerModel}
import com.harana.sdk.backend.models.flow.actionobjects.{SparkMultiColumnEstimatorWrapper, SparkSingleColumnParameterEstimatorWrapper}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.{SingleStringIndexerInfo, StringIndexerEstimatorInfo}
import org.apache.spark.ml.feature.{StringIndexer => SparkStringIndexer, StringIndexerModel => SparkStringIndexerModel}

import scala.language.reflectiveCalls

class StringIndexerEstimator
    extends SparkMultiColumnEstimatorWrapper[
      SparkStringIndexerModel,
      SparkStringIndexer,
      StringIndexerModel,
      SingleColumnStringIndexerModelParameter,
      SingleStringIndexer,
      MultiColumnStringIndexerModel
    ] with StringIndexerEstimatorInfo


class SingleStringIndexer
    extends SparkSingleColumnParameterEstimatorWrapper[
      SparkStringIndexerModel,
      SparkStringIndexer,
      SingleColumnStringIndexerModelParameter
    ] with SingleStringIndexerInfo
