package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.IsotonicRegression
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsFactory
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.IsotonicRegression
import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.CreateIsotonicRegressionInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

class CreateIsotonicRegression extends EstimatorAsFactory[IsotonicRegression]
  with CreateIsotonicRegressionInfo
  with SparkActionDocumentation
