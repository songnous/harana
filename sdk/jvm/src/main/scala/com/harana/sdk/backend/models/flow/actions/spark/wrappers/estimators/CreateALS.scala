package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.ALS
import com.harana.sdk.backend.models.flow.actions.EstimatorAsFactory
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.ALS
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.CreateALSInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateALS extends EstimatorAsFactory[ALS]
  with CreateALSInfo
  with SparkActionDocumentation
