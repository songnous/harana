package com.harana.ui.external.places_autocomplete

import org.scalajs.dom.Element
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.web.SyntheticMouseEvent
import typings.react.mod.MouseEventHandler

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSImport, JSName}
import scala.scalajs.js.|

@JSImport("react-places-autocomplete", JSImport.Default)
@js.native
object ReactPlacesAutocomplete extends js.Object {
  def geocodeByAddress(address: String): js.Promise[js.Array[GeocoderResult]] = js.native
  def geocodeByPlaceId(placeId: String): js.Promise[js.Array[GeocoderResult]] = js.native
  def getLatLng(results: GeocoderResult): js.Promise[LatLngLiteral] = js.native
}

@react object PlacesAutocomplete extends ExternalComponent {

  case class Props(debounce: Option[Double] = None,
                   googleCallbackName: Option[String] = None,
                   highlightFirstSuggestion: Option[Boolean] = None,
                   onChange: Option[String => Unit] = None,
                   onError: Option[(String, () => Unit) => Unit] = None,
                   onSelect: Option[(String, String) => Unit] = None,
                   searchOptions: Option[Bounds] = None,
                   shouldFetchSuggestions: Option[Boolean] = None,
                   value: Option[String] = None)

  override val component = ReactPlacesAutocomplete
}

case class Bounds(bounds: Option[LatLngBoundsLiteral],
                  componentRestrictions: Option[GeocoderComponentRestrictions],
                  location: Option[LatLngLiteral],
                  offset: Option[Double | String],
                  radius: Option[Double | String],
                  types: List[String])

@js.native
trait Event extends js.Object {
  val id: String | Null = js.native
  val key: Double = js.native
  @JSName("onMouseDown")
  val onMouseDown_Original: MouseEventHandler[Element] = js.native
  val role: String = js.native
  def onClick(): Unit = js.native
  def onClick(event: Event): Unit = js.native
  def onMouseDown(event: SyntheticMouseEvent[_]): Unit = js.native
  def onMouseEnter(): Unit = js.native
  def onMouseLeave(): Unit = js.native
  def onMouseUp(): Unit = js.native
  def onTouchEnd(): Unit = js.native
  def onTouchStart(): Unit = js.native
}

case class GeocoderAddressComponent(long_name: String, short_name: String, types: List[String])

case class GeocoderComponentRestrictions(administrativeArea: Option[String],
                                         country: Option[String | List[String]],
                                         locality: Option[String],
                                         postalCode: Option[String],
                                         route: Option[String])

case class GeocoderGeometry(bounds: LatLngBoundsLiteral,
                            location: LatLngLiteral,
                            location_type: String,
                            viewport: LatLngBoundsLiteral)

case class GeocoderResult(address_components: List[GeocoderAddressComponent],
                          formatted_address: String,
                          geometry: GeocoderGeometry,
                          partial_match: Boolean,
                          place_id: String,
                          postcode_localities: List[String],
                          types: List[String])

case class LatLngLiteral(lat: Double, lng: Double)
case class LatLngBoundsLiteral(east: Double, north: Double, south: Double, west: Double)
case class MainText(mainText: String, secondaryText: String)
case class PredictionSubstring(length: Double, offset: Double)
case class PredictionTerm(offset: Double, value: String)

case class Suggestion(active: Boolean,
                      description: String,
                      formattedSuggestion: MainText,
                      id: String,
                      index: Double,
                      matchedSubstrings: List[PredictionSubstring],
                      placeId: String,
                      terms: List[PredictionTerm],
                      types: List[String])

case class Target(target: Value)
case class Value(value: String)