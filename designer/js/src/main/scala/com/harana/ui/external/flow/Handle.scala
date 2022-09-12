package com.harana.ui.external.flow

import com.harana.ui.external.flow.types.{HandleType, Position}
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-flow-renderer", "Handle")
@js.native
object ReactHandle extends js.Object

@react object Handle extends ExternalComponent {

  case class Props(`type`: HandleType,
                   position: Position,
                   isConnectable: js.UndefOr[Boolean] = js.undefined,
                   onConnect: js.UndefOr[Connection => Unit] = js.undefined,
                   isValidConnection: js.UndefOr[Connection => Boolean] = js.undefined,
                   id: js.UndefOr[String] = js.undefined,
                   style: js.UndefOr[js.Dynamic] = js.undefined,
                   className: js.UndefOr[String] = js.undefined)

  override val component = ReactHandle
}