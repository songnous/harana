package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.StringIndexerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.StringIndexerModel
import com.harana.sdk.backend.models.flow.actions.{EstimatorAsAction, MultiColumnEstimatorParametersForwarder}
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.StringIndexerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.StringIndexerModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.StringIndexerEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.StringIndexerModelInfo
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.StringIndexerInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class StringIndexer extends EstimatorAsAction[StringIndexerEstimator, StringIndexerModel]
  with StringIndexerInfo
  with MultiColumnEstimatorParametersForwarder[StringIndexerEstimator]
  with SparkActionDocumentation