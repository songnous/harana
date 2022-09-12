package com.harana.ui.external.jvectormaps

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-jvectormap", JSImport.Default)
@js.native
object ReactJVectorMap extends js.Object

@react object JVectorMap extends ExternalComponent {

  case class Props(containerStyle: Option[js.Object] = None,
                   containerClassName: Option[String] = None,
                   map: Option[js.Object] = None,
                   backgroundColor: Option[String] = None,
                   zoomOnScroll: Option[Boolean] = None,
                   zoomOnScrollSpeed: Option[Boolean] = None,
                   panOnDrag: Option[Boolean] = None,
                   zoomMax: Option[Int] = None,
                   zoomMin: Option[Int] = None,
                   zoomStep: Option[Int] = None,
                   zoomAnimate: Option[Boolean] = None,
                   regionsSelectable: Option[Boolean] = None,
                   regionsSelectableOne: Option[Boolean] = None,
                   markersSelectable: Option[Boolean] = None,
                   markersSelectableOne: Option[Boolean] = None,
                   regionStyle: Option[js.Object] = None,
                   regionLabelStyle: Option[js.Object] = None,
                   markerStyle: Option[js.Object] = None,
                   markerLabelStyle: Option[js.Object] = None,
                   markers: List[String] = List(),
                   series: Option[js.Object] = None,
                   focusOn: Option[String] = None,
                   labels: Option[js.Object] = None,
                   selectedRegions: List[String] = List(),
                   selectedMarkers: List[String] = List(),
                   onRegionTipShow: Option[() => Unit] = None,
                   onRegionOver: Option[() => Unit] = None,
                   onRegionOut: Option[() => Unit] = None,
                   onRegionClick: Option[() => Unit] = None,
                   onRegionSelected: Option[() => Unit] = None,
                   onMarkerTipShow: Option[() => Unit] = None,
                   onMarkerOver: Option[() => Unit] = None,
                   onMarkerOut: Option[() => Unit] = None,
                   onMarkerClick: Option[() => Unit] = None,
                   onMarkerSelected: Option[() => Unit] = None,
                   onViewportChange: Option[() => Unit] = None)

  override val component = ReactJVectorMap
}