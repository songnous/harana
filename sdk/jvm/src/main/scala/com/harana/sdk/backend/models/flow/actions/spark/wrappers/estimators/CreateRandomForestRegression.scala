package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.RandomForestRegression
import com.harana.sdk.backend.models.flow.actions.EstimatorAsFactory
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.CreateRandomForestRegressionInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateRandomForestRegression extends EstimatorAsFactory[RandomForestRegression]
  with CreateRandomForestRegressionInfo
  with SparkActionDocumentation