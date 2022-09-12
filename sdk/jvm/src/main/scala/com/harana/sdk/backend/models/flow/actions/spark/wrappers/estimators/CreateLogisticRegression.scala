package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.estimators.LogisticRegression
import com.harana.sdk.backend.models.designer.flow.actions.EstimatorAsFactory
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.LogisticRegression
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.CreateLogisticRegressionInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateLogisticRegression extends EstimatorAsFactory[LogisticRegression]
  with CreateLogisticRegressionInfo
  with SparkActionDocumentation