package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.estimators.KMeans
import com.harana.sdk.backend.models.designer.flow.actions.EstimatorAsFactory
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.KMeans
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.CreateKMeansInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateKMeans extends EstimatorAsFactory[KMeans]
  with CreateKMeansInfo
  with SparkActionDocumentation
