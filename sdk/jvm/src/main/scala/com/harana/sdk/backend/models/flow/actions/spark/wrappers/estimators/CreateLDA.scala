package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.LDA
import com.harana.sdk.backend.models.flow.actions.EstimatorAsFactory
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.LDA
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.CreateLDAInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateLDA extends EstimatorAsFactory[LDA]
  with CreateLDAInfo
  with SparkActionDocumentation