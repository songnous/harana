package com.harana.ui.external.dynamic_number

import org.scalajs.dom.HTMLInputElement
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.ChangeEvent

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-dynamic-number", JSImport.Default)
@js.native
object ReactDynamicNumber extends js.Object

@react object DynamicNumber extends ExternalComponent {

  case class Props(fraction: Option[Double] = None,
                   integer: Option[Double] = None,
                   negative: Option[Boolean] = None,
                   onChange: Option[(ChangeEvent[HTMLInputElement], Double, String) => Unit] = None,
                   placeholder: Option[String] = None,
                   positive: Option[Boolean] = None,
                   separator: Option[String] = None,
                   thousand: Option[Boolean | String] = None,
                   value: Option[Double | String] = None)

  override val component = ReactDynamicNumber
}