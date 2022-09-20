package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.{LogisticRegressionInfo, MultilayerPerceptronClassifierInfo}
import com.harana.sdk.shared.models.flow.actions.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Classification
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait CreateMultilayerPerceptronClassifierInfo extends EstimatorAsFactoryInfo[MultilayerPerceptronClassifierInfo] with SparkActionDocumentation {

  val id: Id = "860f51aa-627e-4636-a4df-696b79a54efc"
  val name = "Multilayer Perceptron Classifier"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-classification-regression.html#multilayer-perceptron-classifier")
  val category = Classification

  lazy val portO_0: TypeTag[MultilayerPerceptronClassifierInfo] = typeTag

}

object CreateMultilayerPerceptronClassifierInfo extends CreateMultilayerPerceptronClassifierInfo