package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.RandomForestRegression
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsFactory
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.CreateRandomForestRegressionInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateRandomForestRegression extends EstimatorAsFactory[RandomForestRegression]
  with CreateRandomForestRegressionInfo
  with SparkActionDocumentation