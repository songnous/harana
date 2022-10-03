package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.GBTClassifier
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsFactory
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.GBTClassifier
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.CreateGBTClassifierInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateGBTClassifier extends EstimatorAsFactory[GBTClassifier]
  with CreateGBTClassifierInfo
  with SparkActionDocumentation