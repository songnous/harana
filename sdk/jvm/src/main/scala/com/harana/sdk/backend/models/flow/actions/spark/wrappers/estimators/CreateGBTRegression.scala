package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.estimators.GBTRegression
import com.harana.sdk.backend.models.designer.flow.actions.EstimatorAsFactory
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.GBTRegression
import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.CreateGBTRegressionInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

class CreateGBTRegression extends EstimatorAsFactory[GBTRegression]
  with CreateGBTRegressionInfo
  with SparkActionDocumentation