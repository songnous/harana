package com.harana.ui.external.simplemaps

import org.scalablytyped.runtime.StringDictionary
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-simple-maps", "Geographies")
@js.native
object ReactGeographies extends js.Object

@react object Geographies extends ExternalComponent {

  case class Props(children: Option[(List[js.Object], js.Any) => Unit] = None,
                   disableOptimization: Option[Boolean] = None,
                   geography: Option[String | StringDictionary[js.Any] | List[String]] = None)

  override val component = ReactGeographies
}