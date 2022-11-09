package com.harana.ui.external.flow

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-flow-renderer", "MiniMap")
@js.native
object ReactMiniMap extends js.Object

@react object MiniMap extends ExternalComponent {

  case class Props(nodeColor: js.UndefOr[String] = js.undefined,
                   nodeStrokeColor: js.UndefOr[String] = js.undefined,
                   nodeBorderRadius: js.UndefOr[String] = js.undefined,
                   nodeClassName: js.UndefOr[String] = js.undefined,
                   maskColor: js.UndefOr[String] = js.undefined,
                   style: js.UndefOr[js.Dynamic] = js.undefined,
                   className: js.UndefOr[String] = js.undefined)

  override val component = ReactMiniMap
}