package com.harana.ui.external.nivo

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("@nivo/waffle", "ResponsiveWaffleCanvas")
@js.native
object ReactWaffle extends js.Object

@react object Waffle extends ExternalComponent {

  case class Props(total: js.UndefOr[Int] = js.undefined,
                   data: js.UndefOr[List[WaffleEntry]] = js.undefined,
                   rows: js.UndefOr[Int] = js.undefined,
                   columns: js.UndefOr[Int] = js.undefined,
                   fillDirection: js.UndefOr[String] = js.undefined,
                   padding: js.UndefOr[Int] = js.undefined,
                   width: js.UndefOr[Int] = js.undefined,
                   height: js.UndefOr[Int] = js.undefined,
                   pixelRatio: js.UndefOr[Int] = js.undefined,
                   margin: js.UndefOr[Margin] = js.undefined,
                   colors: js.UndefOr[Colors] = js.undefined,
                   emptyColor: js.UndefOr[String] = js.undefined,
                   emptyOpacity: js.UndefOr[Int] = js.undefined,
                   borderWidth: js.UndefOr[Int] = js.undefined,
                   borderColor: js.UndefOr[BorderColor | String] = js.undefined,
                   isInteractive: js.UndefOr[Boolean] = js.undefined,
                   onClick: js.UndefOr[(Int, String) => Unit] = js.undefined,
                   tooltip: js.UndefOr[js.Object => Any] = js.undefined,
                   legends: js.UndefOr[List[Legend]] = js.undefined,
                   animate: js.UndefOr[Boolean] = js.undefined,
                   motionStiffness: js.UndefOr[Int] = js.undefined,
                   motionDamping: js.UndefOr[Int] = js.undefined)

  override val component = ReactWaffle
}