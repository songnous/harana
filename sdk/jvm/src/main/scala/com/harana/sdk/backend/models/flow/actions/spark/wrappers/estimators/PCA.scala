package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.PCAEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.PCAModel
import com.harana.sdk.backend.models.flow.actions.EstimatorAsAction
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.PCAInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class PCA extends EstimatorAsAction[PCAEstimator, PCAModel]
  with PCAInfo
  with SparkActionDocumentation {

}