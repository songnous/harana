package com.harana.ui.external.image_crop

import org.scalajs.dom.HTMLImageElement
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import typings.react.mod.{CSSProperties, SyntheticEvent}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-image-crop", JSImport.Namespace)
@js.native
object ReactImageCrop extends js.Object {
  def containCrop(crop: Crop, imageAspect: Double): Crop = js.native
  def getPixelCrop(image: HTMLImageElement, percentCrop: Crop): Crop = js.native
  def makeAspectCrop(crop: Crop, imageAspect: Double): Crop = js.native
}

@react object ImageCrop extends ExternalComponent {

  case class Props(className: Option[String] = None,
                   crop: Option[Crop] = None,
                   crossorigin: Option[String] = None,
                   disabled: Option[Boolean] = None,
                   imageAlt: Option[String] = None,
                   imageStyle: Option[CSSProperties] = None,
                   keepSelection: Option[Boolean] = None,
                   locked: Option[Boolean] = None,
                   maxHeight: Option[Double] = None,
                   maxWidth: Option[Double] = None,
                   minHeight: Option[Double] = None,
                   minWidth: Option[Double] = None,
                   onComplete: Option[(Crop, PixelCrop) => Unit] = None,
                   onDragEnd: Option[() => Unit] = None,
                   onDragStart: Option[() => Unit] = None,
                   onImageError: Option[SyntheticEvent[HTMLImageElement, js.Any] => Unit] = None,
                   onImageLoaded: Option[(HTMLImageElement, PixelCrop) => Unit] = None,
                   src: String,
                   style: Option[CSSProperties] = None,
                   children: List[ReactElement])

  override val component = ReactImageCrop
}

object Types {
  type Formatter = js.Function4[Int, String, String, Int, Unit]
}

case class Crop(aspect: Option[Double], height: Option[Double], width: Option[Double], x: Double, y: Double)
case class PixelCrop(height: Double, width: Double, x: Double, y: Double)
case class Height(height: Double, width: Double)