package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.ALS
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsFactory
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.ALS
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.CreateALSInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateALS extends EstimatorAsFactory[ALS]
  with CreateALSInfo
  with SparkActionDocumentation
