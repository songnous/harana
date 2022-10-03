package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.LinearRegression
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsFactory
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.CreateLinearRegressionInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateLinearRegression extends EstimatorAsFactory[LinearRegression]
  with CreateLinearRegressionInfo
  with SparkActionDocumentation
