package com.harana.sdk.shared.components.maps

import com.harana.sdk.shared.models.common.Component
import io.circe.generic.JsonCodec
import enumeratum._

@JsonCodec
case class GoogleMap(name: String,
                     zoom: Option[Int] = None,
                     center: Option[(Double, Double)] = None,
                     centerToGeolocation: Boolean = false,
                     tilt: Boolean = false,
                     heading: Option[Int] = None,
                     disableDefaultUI: Boolean = false,
                     mapType: Option[MapType] = None,
                     mapTypeControl: Boolean = false,
                     mapTypePosition: Option[MapPosition] = None,
                     panControl: Boolean = false,
                     panPosition: Option[MapPosition] = None,
                     zoomControl: Boolean = false,
                     zoomPosition: Option[MapPosition] = None,
                     scaleControl: Boolean = false,
                     scalePosition: Option[MapPosition] = None,
                     streetViewControl: Boolean = false,
                     streetViewPosition: Option[MapPosition] = None,
                     rotateControl: Boolean = false,
                     rotatePosition: Option[MapPosition] = None,
                     fullScreenControl: Boolean = false,
                     fullScreenPosition: Option[MapPosition] = None,
                     markers: List[Marker] = List.empty) extends Component

@JsonCodec
case class Marker(position: (Double, Double),
                  draggable: Boolean = false,
                  dropAnimation: Boolean = false)

sealed trait MapType extends EnumEntry
case object MapType extends Enum[MapType] with CirceEnum[MapType] {
  case object Roadmap extends MapType
  case object Satellite extends MapType
  case object Hybrid extends MapType
  case object Terrain extends MapType
  val values = findValues
}

sealed trait MapPosition extends EnumEntry
case object MapPosition extends Enum[MapPosition] with CirceEnum[MapPosition] {
  case object TopLeft extends MapPosition
  case object TopCenter extends MapPosition
  case object TopRight extends MapPosition
  case object LeftTop extends MapPosition
  case object LeftCenter extends MapPosition
  case object LeftBottom extends MapPosition
  case object RightTop extends MapPosition
  case object RightCenter extends MapPosition
  case object RightBottom extends MapPosition
  case object BottomLeft extends MapPosition
  case object BottomCenter extends MapPosition
  case object BottomRight extends MapPosition
  val values = findValues
}

sealed trait MapTypeControlSize extends EnumEntry
case object MapTypeControlSize extends Enum[MapTypeControlSize] with CirceEnum[MapTypeControlSize] {
  case object HorizontalBar extends MapTypeControlSize
  case object DropdownMenu extends MapTypeControlSize
  val values = findValues
}

sealed trait MapDropAnimation extends EnumEntry
case object MapDropAnimation extends Enum[MapDropAnimation] with CirceEnum[MapDropAnimation] {
  case object Bounce extends MapDropAnimation
  case object Drop extends MapDropAnimation
  val values = findValues
}

sealed trait MapLayer extends EnumEntry
case object MapLayer extends Enum[MapLayer] with CirceEnum[MapLayer] {
  case object Bike extends MapLayer
  case object Traffic extends MapLayer
  case object Transit extends MapLayer
  val values = findValues
}