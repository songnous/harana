package com.harana.ui.external.slick

import org.scalajs.dom.HTMLElement
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-slick", JSImport.Default)
@js.native
object ReactSlick extends js.Object {
  def slickGoTo(slideNumber: Double): Unit = js.native
  def slickGoTo(slideNumber: Double, dontAnimate: Boolean): Unit = js.native
  def slickNext(): Unit = js.native
  def slickPause(): Unit = js.native
  def slickPlay(): Unit = js.native
  def slickPrev(): Unit = js.native
}

@react object Slick extends ExternalComponent {

  case class Props(accessibility: Option[Boolean] = None,
                   adaptiveHeight: Option[Boolean] = None,
                   afterChange: Option[Double => Unit] = None,
                   appendDots: Option[ReactElement => HTMLElement] = None,
                   arrows: Option[Boolean] = None,
                   asNavFor: Option[ReactElement] = None,
                   autoplay: Option[Boolean] = None,
                   autoplaySpeed: Option[Double] = None,
                   beforeChange: Option[(Double, Double) => Unit] = None,
                   centerMode: Option[Boolean] = None,
                   centerPadding: Option[String] = None,
                   className: Option[String] = None,
                   cssEase: Option[String] = None,
                   customPaging: Option[Double => HTMLElement] = None,
                   dots: Option[Boolean] = None,
                   dotsClass: Option[String] = None,
                   draggable: Option[Boolean] = None,
                   easing: Option[String] = None,
                   edgeFriction: Option[Double] = None,
                   fade: Option[Boolean] = None,
                   focusOnSelect: Option[Boolean] = None,
                   infinite: Option[Boolean] = None,
                   initialSlide: Option[Double] = None,
                   lazyLoad: Option[String] = None,
                   nextArrow: Option[HTMLElement] = None,
                   onEdge: Option[String => Unit] = None,
                   onInit: Option[() => Unit] = None,
                   onLazyLoad: Option[Double => Unit] = None,
                   onReInit: Option[() => Unit] = None,
                   onSwipe: Option[String => Unit] = None,
                   pauseOnDotsHover: Option[Boolean] = None,
                   pauseOnFocus: Option[Boolean] = None,
                   pauseOnHover: Option[Boolean] = None,
                   prevalrow: Option[HTMLElement] = None,
                   responsive: List[ResponsiveObject] = List(),
                   rows: Option[Double] = None,
                   rtl: Option[Boolean] = None,
                   slide: Option[String] = None,
                   slidesPerRow: Option[Double] = None,
                   slidesToScroll: Option[Double] = None,
                   slidesToShow: Option[Double] = None,
                   speed: Option[Double] = None,
                   swipe: Option[Boolean] = None,
                   swipeEvent: Option[String => Unit] = None,
                   swipeToSlide: Option[Boolean] = None,
                   touchMove: Option[Boolean] = None,
                   touchThreshold: Option[Double] = None,
                   useCSS: Option[Boolean] = None,
                   useTransform: Option[Boolean] = None,
                   valiableWidth: Option[Boolean] = None,
                   vertical: Option[Boolean] = None,
                   waitForAnimate: Option[Boolean] = None)

  override val component = ReactSlick
}

case class ResponsiveObject(breakpoint: Double, settings: String | Settings)

case class Settings(slidesToShow: Option[Int],
                    slidesToScroll: Option[Int],
                    infinite: Option[Boolean],
                    dots: Option[Boolean])