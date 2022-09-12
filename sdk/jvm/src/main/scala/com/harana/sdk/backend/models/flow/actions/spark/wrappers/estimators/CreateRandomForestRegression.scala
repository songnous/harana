package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.estimators.RandomForestRegression
import com.harana.sdk.backend.models.designer.flow.actions.EstimatorAsFactory
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.RandomForestRegression
import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.CreateRandomForestRegressionInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

class CreateRandomForestRegression extends EstimatorAsFactory[RandomForestRegression]
  with CreateRandomForestRegressionInfo
  with SparkActionDocumentation