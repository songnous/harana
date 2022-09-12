package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.{DecisionTreeRegressionInfo, NaiveBayesInfo}
import com.harana.sdk.shared.models.flow.actions.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Classification
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait CreateNaiveBayesInfo extends EstimatorAsFactoryInfo[NaiveBayesInfo] with SparkActionDocumentation {

  val id: Id = "63de675b-b4ec-41a4-985f-2e0bafafe3c4"
  val name = "Naive Bayes"
  val description = """Creates a naive Bayes model.
      |It supports Multinomial NB which can handle finitely supported discrete data.
      |For example, by converting documents into TF-IDF vectors,
      |it can be used for document classification.
      |By making every vector a binary (0/1) data, it can also be used as Bernoulli NB.
      |The input feature values must be nonnegative.""".stripMargin
  val since = Version(1,0,0)
  val docsGuideLocation = Some("mllib-naive-bayes.html")
  val category = Classification

  lazy val portO_0: TypeTag[NaiveBayesInfo] = typeTag

}

object CreateNaiveBayesInfo extends CreateNaiveBayesInfo