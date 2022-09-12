package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.evaluators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEvaluatorWrapper
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators.MulticlassClassificationEvaluatorInfo
import org.apache.spark.ml.evaluation.{MulticlassClassificationEvaluator => SparkMulticlassClassificationEvaluator}

class MulticlassClassificationEvaluator
    extends SparkEvaluatorWrapper[SparkMulticlassClassificationEvaluator]
    with MulticlassClassificationEvaluatorInfo
