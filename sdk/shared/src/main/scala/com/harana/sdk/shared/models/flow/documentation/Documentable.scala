package com.harana.sdk.shared.models.flow.documentation

import com.harana.sdk.shared.models.common.Version

trait Documentable {

  val since: Version

  def generateDocs(sparkVersion: String): Option[String] = None

}
