package com.harana.ui.external.breadcrumbs

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-breadcrumbs-dynamic", "Breadcrumbs")
@js.native
object ReactBreadcrumbs extends js.Object

@react object Breadcrumbs extends ExternalComponent {

  case class Props(container: Option[String | ReactElement | js.Object] = None,
                   containerProps: Option[js.Object] = None,
                   duplicateProps: Option[js.Object] = None,
                   finalItem: Option[String | ReactElement | js.Object] = None,
                   finalProps: Option[js.Object] = None,
                   item: Option[String | ReactElement | js.Object] = None,
                   renameProps: Option[js.Object] = None,
                   separator: Option[String | ReactElement | js.Object] = None)

  override val component = ReactBreadcrumbs
}