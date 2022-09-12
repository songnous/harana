package com.harana.ui.external.breadcrumbs_item

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-breadcrumbs-dynamic", "BreadcrumbsItem")
@js.native
object ReactBreadcrumbsItem extends js.Object

@react object BreadcrumbsItem extends ExternalComponent {

  case class Props(to: String)

  override val component = ReactBreadcrumbsItem
}