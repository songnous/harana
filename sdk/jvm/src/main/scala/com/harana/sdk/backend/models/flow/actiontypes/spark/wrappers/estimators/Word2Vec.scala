package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.Word2VecEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.Word2VecModel
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsActionType
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.Word2VecEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.Word2VecModel
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.Word2VecInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class Word2Vec extends EstimatorAsActionType[Word2VecEstimator, Word2VecModel]
  with Word2VecInfo
  with SparkActionDocumentation