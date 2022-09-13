package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.DecisionTreeClassifier
import com.harana.sdk.backend.models.flow.actions.EstimatorAsFactory
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.DecisionTreeClassifier
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.CreateDecisionTreeClassifierInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateDecisionTreeClassifier extends EstimatorAsFactory[DecisionTreeClassifier]
  with CreateDecisionTreeClassifierInfo
  with SparkActionDocumentation

