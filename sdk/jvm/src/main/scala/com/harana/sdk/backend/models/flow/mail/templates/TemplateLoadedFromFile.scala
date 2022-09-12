package com.harana.sdk.backend.models.flow.mail.templates

import java.io.File
import scala.util.Try

trait TemplateLoadedFromFile[T] extends Template[T] {

  def resolveTemplateName(templateName: String): Try[File]

  def loadTemplateFromFile(file: File): Try[T]

  def loadTemplate(templateName: String): Try[T] =
    resolveTemplateName(templateName).flatMap(loadTemplateFromFile)

}
