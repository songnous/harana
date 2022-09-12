package com.harana.ui.external.cartographer

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-cartographer/lib/components/Map", JSImport.Default)
@js.native
object ReactCartographer extends js.Object

@react object Cartographer extends ExternalComponent {

  case class Props(addressLine1: Option[String] = None,
                   city: Option[String] = None,
                   country: Option[String] = None,
                   height: Double,
                   latitude: Option[Double] = None,
                   longitude: Option[Double] = None,
                   mapId: String,
                   provider: String,
                   providerKey: String,
                   state: Option[String] = None,
                   useBackgroundImageStyle: Boolean,
                   width: Double,
                   zoom: Double)

  override val component = ReactCartographer
}

case class MapData(locationLink: js.Any, locationText: String)
case class MapObject(data: MapData, mapId: String)