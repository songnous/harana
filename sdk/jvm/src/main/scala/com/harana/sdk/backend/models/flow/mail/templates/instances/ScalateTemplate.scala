package com.harana.sdk.backend.models.flow.mail.templates.instances

import com.harana.sdk.backend.models.designer.flow.mail.templates.TemplateLoadedFromFile
import com.harana.sdk.backend.models.designer.flow.utils.DirectoryListFileFinder
import com.harana.sdk.backend.models.flow.mail.templates.TemplateLoadedFromFile
import org.apache.commons.io.FilenameUtils
import org.fusesource.scalate.{Template, TemplateEngine}

import java.io.File
import scala.util.Try

class ScalateTemplate(val scalateTemplateConfig: ScalateTemplateConfig) extends TemplateLoadedFromFile[Template] {

  def this() = this(ScalateTemplateConfig())

  object ScalateTemplateFinder extends DirectoryListFileFinder(scalateTemplateConfig.templatesDirs) {

    def filePredicate(f: File, desc: Option[String]) = {
      require(desc.isDefined, "No template name given")
      val filename = f.getName
      filename.startsWith(desc.get) &&
      ScalateTemplate.scalateExtensions.contains(FilenameUtils.getExtension(filename))
    }

  }

  def loadTemplateFromFile(f: File): Try[Template] = Try {
    ScalateTemplate.engine.load(f)
  }

  def renderTemplate(template: Template, attributes: Map[String, Any]) =
    ScalateTemplate.engine.layout("", template, attributes)

  def resolveTemplateName(templateName: String): Try[File] =
    ScalateTemplateFinder.findFile(templateName)

}

object ScalateTemplate {

  def apply(scalateTemplateConfig: ScalateTemplateConfig): ScalateTemplate =
    new ScalateTemplate(scalateTemplateConfig)

  def apply(): ScalateTemplate = new ScalateTemplate()

  val engine = new TemplateEngine()

  val scalateExtensions = Set("scaml", "haml", "ssp", "jade", "mustache")

}
