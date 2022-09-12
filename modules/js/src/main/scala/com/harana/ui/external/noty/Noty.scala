package com.harana.ui.external.noty

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@kiyasov/noty", JSImport.Default)
@js.native
object ReactNoty extends js.Object

@react object Noty extends ExternalComponent {

  case class Props(maxVisible: Option[Int] = None,
                   `type`: Option[String] = None,
                   ttl: Option[Int] = None,
                   template: Option[String] = None,
                   position: Option[String] = None,
                   isProgressBar: Option[Boolean] = None,
                   isCloseButton: Option[Boolean] = None,
                   isButton: Option[Boolean] = None,
                   animate: Option[Animate] = None,
                   text: Option[String] = None,
                   title: Option[String] = None,
                   isVisibility: Option[Boolean] = None,
                   props: Option[js.Object] = None,
                   theme: Option[String] = None)

  override val component = ReactNoty
}

@js.native
trait Animate extends js.Object {
  val open: Option[String] = js.native
  val close: Option[String] = js.native
}