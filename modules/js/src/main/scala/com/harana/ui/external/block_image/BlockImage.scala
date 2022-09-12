package com.harana.ui.external.block_image

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-block-image", JSImport.Default)
@js.native
object ReactBlockImage extends js.Object

@react object BlockImage extends ExternalComponent {

  case class Props(src: String,
                   fallback: Option[String] = None,
                   showPreview: Option[Boolean] = None,
                   loader: Option[ReactElement] = None,
                   backgroundSize: Option[String] = None,
                   backgroundPosition: Option[String] = None,
                   backgroundRepeat: Option[String] = None,
                   style: Option[js.Object] = None,
                   className: Option[String] = None,
                   children: List[ReactElement])

  override val component = ReactBlockImage
}