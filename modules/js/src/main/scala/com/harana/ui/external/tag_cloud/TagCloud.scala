package com.harana.ui.external.tag_cloud

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-tagcloud", JSImport.Default)
@js.native
object ReactTagCloud extends js.Object

@react object TagCloud extends ExternalComponent {

  case class Props(className: Option[String] = None,
                   disableRandomColor: Option[Boolean] = None,
                   maxSize: Double,
                   minSize: Double,
                   onClick: Option[js.Function] = None,
                   renderer: Option[js.Function] = None,
                   shuffle: Option[Boolean] = None,
                   tags: List[js.Any])

  override val component = ReactTagCloud
}