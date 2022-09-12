package com.harana.ui.external.gravatar

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-gravatar", JSImport.Default)
@js.native
object ReactGravatar extends js.Object

@react object Gravatar extends ExternalComponent {

  case class Props(className: Option[String] = None,
                   default: Option[String] = None,
                   email: Option[String] = None,
                   md5: Option[String] = None,
                   protocol: Option[String] = None,
                   rating: Option[String] = None,
                   size: Option[Double] = None,
                   style: Option[CSSProperties] = None)

  override val component = ReactGravatar
}