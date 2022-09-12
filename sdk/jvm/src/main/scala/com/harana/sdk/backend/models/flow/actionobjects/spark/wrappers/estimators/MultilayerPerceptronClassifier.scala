package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.MultilayerPerceptronClassifierModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.MultilayerPerceptronClassifierInfo
import org.apache.spark.ml.classification.{MultilayerPerceptronClassificationModel => SparkMultilayerPerceptronClassifierModel, MultilayerPerceptronClassifier => SparkMultilayerPerceptronClassifier}

class MultilayerPerceptronClassifier
    extends SparkEstimatorWrapper[
      SparkMultilayerPerceptronClassifierModel,
      SparkMultilayerPerceptronClassifier,
      MultilayerPerceptronClassifierModel
    ]
    with MultilayerPerceptronClassifierInfo