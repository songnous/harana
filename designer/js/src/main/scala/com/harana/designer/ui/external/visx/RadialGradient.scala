package com.harana.ui.external.visx

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("@visx/gradient", "RadialGradient")
@js.native
object ReactRadialGradient extends js.Object

@react object RadialGradient extends ExternalComponent {

  case class Props(id: String,
                   from: js.UndefOr[String] = js.undefined,
                   fromOffset: js.UndefOr[Int | String] = js.undefined,
                   fromOpacity: js.UndefOr[Int | String] = js.undefined,
                   rotate: js.UndefOr[Int | String] = js.undefined,
                   to: js.UndefOr[Int | String] = js.undefined,
                   toOffset: js.UndefOr[Int | String] = js.undefined,
                   toOpacity: js.UndefOr[Int | String] = js.undefined,
                   transform: js.UndefOr[String] = js.undefined)

  override val component = ReactRadialGradient
}