package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.NaiveBayes
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsFactory
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.CreateNaiveBayesInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateNaiveBayes extends EstimatorAsFactory[NaiveBayes]
  with CreateNaiveBayesInfo
  with SparkActionDocumentation