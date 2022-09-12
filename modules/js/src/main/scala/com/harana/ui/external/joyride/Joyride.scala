package com.harana.ui.external.joyride

import org.scalablytyped.runtime.StringDictionary
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-joyride", JSImport.Default)
@js.native
object ReactJoyride extends js.Object

@react object Joyride extends ExternalComponent {

  case class Props(steps: List[Step],
                   beaconComponent: Option[ReactElement] = None,
                   callback: Option[State => Unit] = None,
                   continuous: Option[Boolean] = None,
                   debug: Option[Boolean] = None,
                   disableCloseOnEsc: Option[Boolean] = None,
                   disableOverlay: Option[Boolean] = None,
                   disableOverlayClose: Option[Boolean] = None,
                   disableScrolling: Option[Boolean] = None,
                   disableScrollParentFix: Option[Boolean] = None,
                   floaterProps: Option[js.Object] = None,
                   getHelpers: Option[js.Object => Unit] = None,
                   showSkipButton: Option[Boolean] = None,
                   spotlightClicks: Option[Boolean] = None,
                   spotlightPadding: Option[Double] = None,
                   stepIndex: Option[Int] = None,
                   styles: Option[StepStyles] = None,
                   tooltipComponent: Option[ReactElement] = None)

  override val component = ReactJoyride
}

case class Step(target: String,
                content: ReactElement,
                disableBoolean: Option[Boolean],
                event: Option[String],
                isFixed: Option[Boolean],
                offset: Option[String],
                placement: Option[String],
                placementBeacon: Option[String],
                styles: Option[js.Object],
                title: Option[ReactElement])

case class State(action: String,
                 controlled: Boolean,
                 index: Int,
                 lifecycle: String,
                 size: Int,
                 status: String,
                 step: js.Object,
                 `type`: String)

case class StepStyles(options: Option[StringDictionary[js.Any]])