package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.GBTRegression
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsFactory
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.CreateGBTRegressionInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateGBTRegression extends EstimatorAsFactory[GBTRegression]
  with CreateGBTRegressionInfo
  with SparkActionDocumentation