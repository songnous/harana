package com.harana.ui.external.popconfirm

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-popconfirm", JSImport.Default)
@js.native
object ReactPopconfirm extends js.Object

@react object Popconfirm extends ExternalComponent {

  case class Props(element: ReactElement,
                   confirmation: String,
                   placement: Option[String] = None,
                   okLabbel: Option[String] = None,
                   cancelLabel: Option[String] = None,
                   positionLeft: Option[Boolean] = None,
                   positionTop: Option[Boolean] = None,
                   width: Option[Int] = None,
                   height: Option[Int] = None,
                   confirmationColor: Option[String] = None,
                   okStyle: Option[String] = None,
                   cancelStyle: Option[String] = None)

  override val component = ReactPopconfirm
}