package com.harana.ui.external.avatar_editor

import org.scalajs.dom.{DragEvent, HTMLCanvasElement}
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.std
import typings.std.File

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-avatar-editor", JSImport.Default)
@js.native
object ReactAvatarEditor extends js.Object {
  def getCroppingRect(): CroppedRect = js.native
  def getImage(): HTMLCanvasElement = js.native
  def getImageScaledToCanvas(): HTMLCanvasElement = js.native
}

@react object AvatarEditor extends ExternalComponent {

  case class Props(border: Option[Double | List[Double]] = None,
                   borderRadius: Option[Double] = None,
                   className: Option[String] = None,
                   color: List[Double] = List(),
                   crossOrigin: Option[String] = None,
                   disableDrop: Option[Boolean] = None,
                   height: Option[Double] = None,
                   image: String | File,
                   onDropFile: Option[DragEvent => Unit] = None,
                   onImageChange: Option[() => Unit] = None,
                   onImageReady: Option[js.Any => Unit] = None,
                   onLoadFailure: Option[js.Any => Unit] = None,
                   onLoadSuccess: Option[ImageState => Unit] = None,
                   onMouseMove: Option[js.Any => Unit] = None,
                   onMouseUp: Option[() => Unit] = None,
                   onPositionChange: Option[() => Unit] = None,
                   position: Option[js.Object] = None,
                   rotate: Option[Double] = None,
                   scale: Option[Double] = None,
                   style: Option[js.Object] = None,
                   width: Option[Double] = None)

  override val component = ReactAvatarEditor
}

@js.native
trait CroppedRect extends js.Object {
  val height: Double
  val width: Double
  val x: Double
  val y: Double
}

@js.native
trait ImageState extends js.Object {
  val height: Double
  val resource: std.ImageData
  val width: Double
  val x: Double
  val y: Double
}