package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.{DecisionTreeRegressionInfo, LDAInfo}
import com.harana.sdk.shared.models.flow.actions.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Clustering
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait CreateLDAInfo extends EstimatorAsFactoryInfo[LDAInfo] with SparkActionDocumentation {

  val id: Id = "a385f8fe-c64e-4d71-870a-9d5048747a3c"
  val name = "LDA"
  val description = """Latent Dirichlet Allocation (LDA), a topic model designed for text documents. LDA is given a
      |collection of documents as input data, via the `features column` parameter. Each document is
      |specified as a vector of length equal to the vocabulary size, where each entry is the count
      |for the corresponding term (word) in the document. Feature transformers such as Tokenize and
      |Count Vectorizer can be useful for converting text to word count vectors.""".stripMargin
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-clustering.html#latent-dirichlet-allocation-lda")
  val category = Clustering

  lazy val portO_0: TypeTag[LDAInfo] = typeTag

}

object CreateLDAInfo extends CreateLDAInfo
