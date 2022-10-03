package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.AFTSurvivalRegression
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsFactory
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.AFTSurvivalRegression
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.CreateAFTSurvivalRegressionInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateAFTSurvivalRegression extends EstimatorAsFactory[AFTSurvivalRegression]
  with CreateAFTSurvivalRegressionInfo
  with SparkActionDocumentation
