package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.StringIndexerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.StringIndexerModel
import com.harana.sdk.backend.models.flow.actiontypes.{EstimatorAsActionType, MultiColumnEstimatorParametersForwarder}
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.StringIndexerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.StringIndexerModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.StringIndexerEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.StringIndexerModelInfo
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.StringIndexerInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class StringIndexer extends EstimatorAsActionType[StringIndexerEstimator, StringIndexerModel]
  with StringIndexerInfo
  with MultiColumnEstimatorParametersForwarder[StringIndexerEstimator]
  with SparkActionDocumentation