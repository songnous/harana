package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.MultilayerPerceptronClassifier
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsFactory
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.CreateMultilayerPerceptronClassifierInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateMultilayerPerceptronClassifier extends EstimatorAsFactory[MultilayerPerceptronClassifier]
  with CreateMultilayerPerceptronClassifierInfo
  with SparkActionDocumentation