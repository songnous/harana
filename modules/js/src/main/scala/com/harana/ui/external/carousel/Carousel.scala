package com.harana.ui.external.carousel

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-bootstrap-carousel", JSImport.Default)
@js.native
object ReactBootstrapCarousel extends js.Object

@react object Carousel extends ExternalComponent {

  case class Props(indicators: Option[Boolean] = None,
                   controls: Option[Boolean] = None,
                   slideshowSpeed: Option[Int] = None,
                   defaultActiveIndex: Option[Int] = None,
                   wrap: Option[Boolean] = None,
                   autoplay: Option[Boolean] = None,
                   children: List[ReactElement] = List(),
                   animation: Option[Boolean] = None,
                   className: Option[String] = None,
                   version: Option[Int] = None,
                   pauseOnVisibility: Option[Boolean] = None,
                   hidden: Option[Boolean] = None,
                   onSelect: Option[() => Unit] = None)

  override val component = ReactBootstrapCarousel
}