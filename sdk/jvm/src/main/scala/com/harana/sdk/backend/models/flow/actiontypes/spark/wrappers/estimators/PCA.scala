package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.PCAEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.PCAModel
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsActionType
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.PCAInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class PCA extends EstimatorAsActionType[PCAEstimator, PCAModel]
  with PCAInfo
  with SparkActionDocumentation {

}