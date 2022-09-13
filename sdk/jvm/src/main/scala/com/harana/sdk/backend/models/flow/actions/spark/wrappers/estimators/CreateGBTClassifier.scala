package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.GBTClassifier
import com.harana.sdk.backend.models.flow.actions.EstimatorAsFactory
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.GBTClassifier
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.CreateGBTClassifierInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateGBTClassifier extends EstimatorAsFactory[GBTClassifier]
  with CreateGBTClassifierInfo
  with SparkActionDocumentation