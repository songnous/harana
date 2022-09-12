package com.harana.sdk.backend.models.flow.mail.templates

import scala.util.Try

trait Template[T] {

  type TemplateContext = Map[String, Any]

  def loadTemplate(templateName: String): Try[T]

  def renderTemplate(template: T, templateContext: TemplateContext): String

}
