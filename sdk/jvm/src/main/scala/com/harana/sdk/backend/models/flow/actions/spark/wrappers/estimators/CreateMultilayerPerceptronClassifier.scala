package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.MultilayerPerceptronClassifier
import com.harana.sdk.backend.models.flow.actions.EstimatorAsFactory
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.CreateMultilayerPerceptronClassifierInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateMultilayerPerceptronClassifier extends EstimatorAsFactory[MultilayerPerceptronClassifier]
  with CreateMultilayerPerceptronClassifierInfo
  with SparkActionDocumentation