package com.harana.ui.external.flow

import com.harana.ui.external.flow.types.{HandleType, Position}
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-flow-renderer", "Controls")
@js.native
object ReactControls extends js.Object

@react object Controls extends ExternalComponent {

  case class Props(showZoom: js.UndefOr[Boolean] = js.undefined,
                   showFitView: js.UndefOr[Boolean] = js.undefined,
                   showInteractive: js.UndefOr[Boolean] = js.undefined,
                   onZoomIn: js.UndefOr[() => Unit] = js.undefined,
                   onZoomOut: js.UndefOr[() => Unit] = js.undefined,
                   onFitView: js.UndefOr[() => Unit] = js.undefined,
                   onInteractiveChange: js.UndefOr[Boolean => Unit] = js.undefined,
                   style: js.UndefOr[js.Dynamic] = js.undefined,
                   className: js.UndefOr[String] = js.undefined)

  override val component = ReactControls
}