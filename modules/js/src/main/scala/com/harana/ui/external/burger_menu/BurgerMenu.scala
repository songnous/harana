package com.harana.ui.external.burger_menu

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.SyntheticKeyboardEvent

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-burger-menu", "slide")
@js.native
object ReactBurgerMenu extends js.Object

@react object BurgerMenu extends ExternalComponent {

  case class Props(pageWrapId: Option[String] = None,
                   outerContainerId: Option[String] = None,
                   right: Option[Boolean] = None,
                   width: Option[Int] = None,
                   isOpen: Option[Boolean] = None,
                   disableCloseOnEsc: Option[Boolean] = None,
                   onStateChange: Option[State => Unit] = None,
                   customOnKeyDown: Option[SyntheticKeyboardEvent[_] => Unit] = None,
                   noOverlay: Option[Boolean] = None,
                   disableOverlayClick: Option[Boolean | Boolean => Unit] = None,
                   customBurgerIcon: Option[ReactElement] = None,
                   customCrossIcon: Option[ReactElement] = None,
                   id: Option[String] = None,
                   className: Option[String] = None,
                   styles: Option[js.Object] = None,
                   burgerButtonClassName: Option[String] = None,
                   burgerBarClassName: Option[String] = None,
                   crossButtonClassName: Option[String] = None,
                   crossClassName: Option[String] = None,
                   menuClassName: Option[String] = None,
                   morphShapeClassName: Option[String] = None,
                   itemListClassName: Option[String] = None,
                   overlayClassName: Option[String] = None,
                   bodyClassName: Option[String] = None)

  override val component = ReactBurgerMenu
}

case class State(isOpen: Option[Boolean])