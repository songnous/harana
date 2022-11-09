package com.harana.ui.external.diagrams

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("@projectstorm/react-canvas-core", "CanvasWidget")
object ReactCanvasWidget extends js.Object

@react object CanvasWidget extends ExternalComponent {

  case class Props(engine: CanvasEngine[_ <: CanvasEngineListener, _ <: CanvasModel[_,_]],
                   className: Option[String] = None)

  override val component = ReactCanvasWidget
}