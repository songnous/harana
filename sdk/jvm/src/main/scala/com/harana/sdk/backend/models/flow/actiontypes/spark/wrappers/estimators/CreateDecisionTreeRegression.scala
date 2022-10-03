package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.DecisionTreeRegression
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsFactory
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.CreateDecisionTreeRegressionInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateDecisionTreeRegression extends EstimatorAsFactory[DecisionTreeRegression]
  with CreateDecisionTreeRegressionInfo
  with SparkActionDocumentation
