package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.Word2VecEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.Word2VecModel
import com.harana.sdk.backend.models.flow.actions.EstimatorAsAction
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.Word2VecEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.Word2VecModel
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.Word2VecInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class Word2Vec extends EstimatorAsAction[Word2VecEstimator, Word2VecModel]
  with Word2VecInfo
  with SparkActionDocumentation