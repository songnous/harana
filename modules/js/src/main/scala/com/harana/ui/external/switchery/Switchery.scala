package com.harana.ui.external.switchery

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-switchery", JSImport.Default)
@js.native
object ReactSwitchery extends js.Object

@react object Switchery extends ExternalComponent {

  case class Props(className: String = "",
                   label: Option[String] = None,
                   checked: Boolean = false,
                   required: Boolean = false,
                   options: Options = Options(),
                   onChange: Boolean => Unit = Boolean => {})

  override val component = ReactSwitchery
}

case class Options(color: Option[String] = None,
                   secondaryColor: Option[String] = None,
                   jackColor: Option[String] = None,
                   jackSecondaryColor: Option[String] = None,
                   className: Option[String] = None,
                   disabled: Boolean = false,
                   disabledOpacity: Option[Int] = None,
                   speed: Option[String] = None,
                   size: Option[String] = None)