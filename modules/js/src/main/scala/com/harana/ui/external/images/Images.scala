package com.harana.ui.external.images

import org.scalajs.dom.HTMLImageElement
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import typings.react.mod.{MouseEvent, NativeMouseEvent}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-images", JSImport.Default)
@js.native
object ReactImages extends js.Object {
  def onClose(): Unit = js.native
}

@react object Images extends ExternalComponent {

  case class Props(backdropClosesModal: Option[Boolean] = None,
                   closeButtonTitle: Option[String] = None,
                   currentImage: Option[Double] = None,
                   customControls: List[ReactElement] = List(),
                   enableKeyboardInput: Option[Boolean] = None,
                   imageCountSeparator: Option[String] = None,
                   images: List[Image],
                   isOpen: Option[Boolean] = None,
                   leftArrowTitle: Option[String] = None,
                   onClickImage: Option[MouseEvent[HTMLImageElement, NativeMouseEvent] => Unit] = None,
                   onClickNext: Option[() => Unit] = None,
                   onClickPrev: Option[() => Unit] = None,
                   onClickThumbnail: Option[Double => Unit] = None,
                   preloadNextImage: Option[Boolean] = None,
                   preventScroll: Option[Boolean] = None,
                   rightArrowTitle: Option[String] = None,
                   showCloseButton: Option[Boolean] = None,
                   showImageCount: Option[Boolean] = None,
                   showThumbnails: Option[Boolean] = None,
                   spinner: Option[() => ReactElement] = None,
                   spinnerColor: Option[String] = None,
                   spinnerSize: Option[Double] = None,
                   width: Option[Double] = None)

  override val component = ReactImages
}

case class Image(alt: Option[String], caption: Option[String], src: String, srcSet: Option[String | List[String]])