package com.harana.ui.external.breadcrumbs_provider

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-breadcrumbs-dynamic", "BreadcrumbsProvider")
@js.native
object ReactBreadcrumbsProvider extends js.Object

@react object BreadcrumbsProvider extends ExternalComponent {

  case class Props(shouldBreadcrumbsUpdate: Option[js.Any => Unit] = None)

  override val component = ReactBreadcrumbsProvider
}