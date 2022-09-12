package com.harana.ui.external.cache_router

import slinky.core._
import slinky.core.annotations.react
import slinky.web.html.a
import org.scalajs.dom.History

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-router-cache-route", "CacheSwitch")
@js.native
object ReactCacheSwitch extends js.Object

object CacheSwitch extends ExternalComponentNoProps {
  override val component = ReactCacheSwitch
}

@JSImport("react-router-cache-route", "CacheRoute")
@js.native
object ReactCacheRoute extends js.Object

@react object CacheRoute extends ExternalComponent {
  case class Props(path: String, component: ReactComponentClass[_], exact: Boolean = false)
  override val component = ReactCacheRoute
}