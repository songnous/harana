package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.LDAModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.LDAInfo
import org.apache.spark.ml.clustering.{LDA => SparkLDA, LDAModel => SparkLDAModel}

class LDA
    extends SparkEstimatorWrapper[SparkLDAModel, SparkLDA, LDAModel]
    with LDAInfo