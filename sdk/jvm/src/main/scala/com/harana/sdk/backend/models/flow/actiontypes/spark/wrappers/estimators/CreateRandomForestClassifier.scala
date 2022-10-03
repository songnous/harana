package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.RandomForestClassifier
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsFactory
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.CreateRandomForestClassifierInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateRandomForestClassifier extends EstimatorAsFactory[RandomForestClassifier]
  with CreateRandomForestClassifierInfo
  with SparkActionDocumentation
