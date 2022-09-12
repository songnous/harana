package com.harana.sdk.backend.models.flow.mail.templates.instances

import com.harana.sdk.backend.models.designer.flow.utils.ConfigWithDirListsImplicits.ConfigWithDirLists
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

class ScalateTemplateConfig(val masterConfig: Config) {
  import ScalateTemplateConfig._

  def this() = this(ConfigFactory.load())
  val templatesDirs = masterConfig.getConfig(scalateTemplateSubConfigPath).getDirList("templates-dirs")

}

object ScalateTemplateConfig {

  def apply(masterConfig: Config): ScalateTemplateConfig = new ScalateTemplateConfig(masterConfig)

  def apply(): ScalateTemplateConfig = new ScalateTemplateConfig()

  val scalateTemplateSubConfigPath = "scalate-templates"

}
