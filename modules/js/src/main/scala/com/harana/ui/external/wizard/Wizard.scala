package com.harana.ui.external.wizard

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-bootstrap-wizard", JSImport.Default)
@js.native
object ReactBootstrapWizard extends js.Object

@react object Wizard extends ExternalComponent {

  case class Props(steps: List[Step],
                   color: Option[String] = None,
                   previousButtonClasses: Option[String] = None,
                   finishButtonClasses: Option[String] = None,
                   nextButtonClasses: Option[String] = None,
                   headerTextCenter: Option[Boolean] = None,
                   navSteps: Option[Boolean] = None,
                   validate: Option[Boolean] = None,
                   finishButtonClick: Option[() => Unit] = None,
                   previousButtonText: Option[String] = None,
                   finishButtonText: Option[String] = None,
                   nextButtonText: Option[String] = None,
                   title: Option[String] = None,
                   description: Option[String] = None,
                   progressbar: Option[Boolean] = None)

    override val component = ReactBootstrapWizard
}

@js.native
trait Step extends js.Object {
  val stepName: String = js.native
  val stepIcon: Option[String] = None
  val component: () => Unit = js.native
  val stepProps: js.Object = js.native
}