package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.estimators.RandomForestClassifier
import com.harana.sdk.backend.models.designer.flow.actions.EstimatorAsFactory
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.RandomForestClassifier
import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.CreateRandomForestClassifierInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

class CreateRandomForestClassifier extends EstimatorAsFactory[RandomForestClassifier]
  with CreateRandomForestClassifierInfo
  with SparkActionDocumentation
