package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.NaiveBayes
import com.harana.sdk.backend.models.flow.actions.EstimatorAsFactory
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.CreateNaiveBayesInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateNaiveBayes extends EstimatorAsFactory[NaiveBayes]
  with CreateNaiveBayesInfo
  with SparkActionDocumentation