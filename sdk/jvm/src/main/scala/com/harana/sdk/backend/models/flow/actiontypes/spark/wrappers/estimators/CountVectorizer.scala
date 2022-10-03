package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.CountVectorizerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.CountVectorizerModel
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsActionType
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.CountVectorizerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.CountVectorizerModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.CountVectorizerEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.CountVectorizerModelInfo
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.CountVectorizerInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CountVectorizer extends EstimatorAsActionType[CountVectorizerEstimator, CountVectorizerModel]
  with CountVectorizerInfo
  with SparkActionDocumentation