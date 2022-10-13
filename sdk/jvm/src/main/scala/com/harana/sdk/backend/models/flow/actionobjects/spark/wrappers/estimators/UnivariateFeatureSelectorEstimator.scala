package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.UnivariateFeatureSelectorModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.UnivariateFeatureSelectorEstimatorInfo
import org.apache.spark.ml.feature.{UnivariateFeatureSelector => SparkUnivariateFeatureSelector, UnivariateFeatureSelectorModel => SparkUnivariateFeatureSelectorModel}

import scala.language.reflectiveCalls

class UnivariateFeatureSelectorEstimator
    extends SparkEstimatorWrapper[SparkUnivariateFeatureSelectorModel, SparkUnivariateFeatureSelector, UnivariateFeatureSelectorModel]
    with UnivariateFeatureSelectorEstimatorInfo

