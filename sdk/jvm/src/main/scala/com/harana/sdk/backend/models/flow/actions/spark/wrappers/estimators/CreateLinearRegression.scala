package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.LinearRegression
import com.harana.sdk.backend.models.flow.actions.EstimatorAsFactory
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.CreateLinearRegressionInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateLinearRegression extends EstimatorAsFactory[LinearRegression]
  with CreateLinearRegressionInfo
  with SparkActionDocumentation
