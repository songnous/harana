package com.harana.ui.external.flow

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-flow-renderer", "Background")
@js.native
object ReactBackground extends js.Object

@react object Background extends ExternalComponent {

  case class Props(variant: js.UndefOr[String] = js.undefined,
                   gap: js.UndefOr[Int | Double] = js.undefined,
                   size: js.UndefOr[Int | Double] = js.undefined,
                   color: js.UndefOr[String] = js.undefined,
                   style: js.UndefOr[js.Dynamic] = js.undefined,
                   className: js.UndefOr[String] = js.undefined)

  override val component = ReactBackground
}