package com.harana.ui.external.fancybox

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-fancybox/lib/fancybox.css", JSImport.Default)
@js.native
object ReactFancyboxCSS extends js.Object

@JSImport("react-fancybox", JSImport.Default)
@js.native
object ReactFancybox extends js.Object

@react object Fancybox extends ExternalComponent {

  case class Props(thumbnail: String,
                   image: String,
                   caption: Option[String] = None,
                   animation: Option[String] = None,
                   defaultThumbnailWidth: Option[String] = None,
                   defaultThumbnailHeight: Option[String] = None,
                   onOpen: Option[() => Unit] = None,
                   onClose: Option[() => Unit] = None,
                   showCloseBtn: Option[Boolean] = None)

  override val component = ReactFancybox
}