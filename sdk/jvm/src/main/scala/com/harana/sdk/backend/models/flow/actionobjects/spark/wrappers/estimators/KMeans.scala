package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.KMeansModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.KMeansInfo
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.models.flow.parameters.choice.Choice
import org.apache.spark.ml.clustering.{KMeans => SparkKMeans, KMeansModel => SparkKMeansModel}

class KMeans
    extends SparkEstimatorWrapper[SparkKMeansModel, SparkKMeans, KMeansModel]
    with KMeansInfo