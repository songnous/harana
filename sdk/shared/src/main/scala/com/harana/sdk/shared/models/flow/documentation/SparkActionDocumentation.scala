package com.harana.sdk.shared.models.flow.documentation

import com.harana.sdk.shared.models.flow.ActionInfo

trait SparkActionDocumentation extends ActionDocumentation { self: ActionInfo =>

  val docsGuideLocation: Option[String]

  /** Generates Spark's guide section with a link. Used by docs.generator. */
  override def generateDocs(sparkVersion: String): Option[String] =
    docsGuideLocation.map { guideLocation =>
      val sparkDocsUrl = s"https://spark.apache.org/docs/$sparkVersion/"

      s"""|For a comprehensive introduction, see
          |<a target="_blank" href="${sparkDocsUrl + guideLocation}">Spark documentation</a>.""".stripMargin
    }
}
