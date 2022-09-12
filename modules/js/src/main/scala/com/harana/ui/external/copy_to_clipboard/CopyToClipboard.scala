package com.harana.ui.external.copy_to_clipboard

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-copy-to-clipboard", JSImport.Namespace)
@js.native
object ReactCopyToClipboard extends js.Object

@react object CopyToClipboard extends ExternalComponent {

  case class Props(onCopy: Option[(String, Boolean) => Unit] = None,
                   options: Option[Options] = None,
                   text: String)

  override val component = ReactCopyToClipboard
}

case class Options(debug: Boolean, message: String)