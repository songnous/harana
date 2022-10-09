package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.MultilayerPerceptronClassifierInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Classification
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait CreateMultilayerPerceptronClassifierInfo extends EstimatorAsFactoryInfo[MultilayerPerceptronClassifierInfo] with SparkActionDocumentation {

  val id: Id = "860f51aa-627e-4636-a4df-696b79a54efc"
  val name = "multilayer-perceptron-classifier"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-classification-regression.html#multilayer-perceptron-classifier")
  val category = Classification

  lazy val portO_0: Tag[MultilayerPerceptronClassifierInfo] = typeTag

}

object CreateMultilayerPerceptronClassifierInfo extends CreateMultilayerPerceptronClassifierInfo