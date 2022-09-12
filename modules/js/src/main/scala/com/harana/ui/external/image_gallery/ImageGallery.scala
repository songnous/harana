package com.harana.ui.external.image_gallery

import org.scalajs.dom.{HTMLAnchorElement, HTMLDivElement, HTMLElement, HTMLImageElement}
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import typings.react.mod.{MouseEventHandler, ReactEventHandler, TouchEventHandler}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-image-gallery", JSImport.Namespace)
@js.native
object ReactImageGallery extends js.Object {
  def exitFullScreen(): Unit = js.native
  def fullScreen(): Unit = js.native
  def getCurrentIndex(): Double = js.native
  def pause(): Unit = js.native
  def pause(callback: Boolean): Unit = js.native
  def play(): Unit = js.native
  def play(callback: Boolean): Unit = js.native
  def slideToIndex(index: Double): Unit = js.native
}

@react object ImageGallery extends ExternalComponent {

  case class Props(autoPlay: Option[Boolean] = None,
                   defaultImage: Option[String] = None,
                   disableArrowKeys: Option[Boolean] = None,
                   disableSwipe: Option[Boolean] = None,
                   disableThumbnailScroll: Option[Boolean] = None,
                   flickThreshold: Option[Double] = None,
                   indexSeparator: Option[String] = None,
                   infinite: Option[Boolean] = None,
                   items: List[ReactImageGalleryItem],
                   lazyLoad: Option[Boolean] = None,
                   onClick: Option[MouseEventHandler[HTMLDivElement] => Unit] = None,
                   onImageError: Option[ReactEventHandler[HTMLImageElement] => Unit] = None,
                   onImageLoad: Option[ReactEventHandler[HTMLImageElement] => Unit] = None,
                   onMouseLeave: Option[MouseEventHandler[HTMLDivElement] => Unit] = None,
                   onMouseOver: Option[MouseEventHandler[HTMLDivElement] => Unit] = None,
                   onPause: Option[Double => Unit] = None,
                   onPlay: Option[Double => Unit] = None,
                   onScreenChange: Option[Element => Unit] = None,
                   onSlide: Option[Double => Unit] = None,
                   onThumbnailClick: Option[(MouseEventHandler[HTMLAnchorElement], Double) => Unit] = None,
                   onThumbnailError: Option[ReactEventHandler[HTMLImageElement] => Unit] = None,
                   onTouchEnd: Option[TouchEventHandler[HTMLDivElement] => Unit] = None,
                   onTouchMove: Option[TouchEventHandler[HTMLDivElement] => Unit] = None,
                   onTouchStart: Option[TouchEventHandler[HTMLDivElement] => Unit] = None,
                   preventDefaultTouchmoveEvent: Option[Boolean] = None,
                   renderCustomControls: Option[() => ReactElement] = None,
                   renderFullscreenButton: Option[(MouseEventHandler[HTMLElement], Boolean) => ReactElement] = None,
                   renderItem: Option[ReactImageGalleryItem => ReactElement] = None,
                   renderLeftNav: Option[(MouseEventHandler[HTMLElement], Boolean) => ReactElement] = None,
                   renderPlayPauseButton: Option[(MouseEventHandler[HTMLElement], Boolean) => ReactElement] = None,
                   renderRightNav: Option[(MouseEventHandler[HTMLElement], Boolean) => ReactElement] = None,
                   renderThumbInner: Option[ReactImageGalleryItem => ReactElement] = None,
                   showBullets: Option[Boolean] = None,
                   showFullscreenButton: Option[Boolean] = None,
                   showIndex: Option[Boolean] = None,
                   showNav: Option[Boolean] = None,
                   showPlayButton: Option[Boolean] = None,
                   showThumbnails: Option[Boolean] = None,
                   slideDuration: Option[Double] = None,
                   slideInterval: Option[Double] = None,
                   slideOnThumbnailHover: Option[Boolean] = None,
                   startIndex: Option[Double] = None,
                   stopPropagation: Option[Boolean] = None,
                   swipeThreshold: Option[Double] = None,
                   swipingTransitionDuration: Option[Double] = None,
                   thumbnailPosition: Option[String] = None,
                   useBrowserFullscreen: Option[Boolean] = None)

  override val component = ReactImageGallery
}

@js.native
trait ReactImageGalleryItem extends js.Object {
  val description: Option[String] = js.native
  val original: Option[String] = js.native
  val originalAlt: Option[String] = js.native
  val originalClass: Option[String] = js.native
  val originalTitle: Option[String] = js.native
  val renderItem: Option[js.Object => ReactElement] = js.native
  val renderThumbInner: Option[js.Object => ReactElement] = js.native
  val sizes: Option[String] = js.native
  val srcSet: Option[String] = js.native
  val thumbnail: Option[String] = js.native
  val thumbnailAlt: Option[String] = js.native
  val thumbnailClass: Option[String] = js.native
  val thumbnailLabel: Option[String] = js.native
  val thumbnailTitle: Option[String] = js.native
}