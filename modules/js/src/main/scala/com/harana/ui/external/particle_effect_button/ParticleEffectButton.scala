package com.harana.ui.external.particle_effect_button

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-particle-effect-button", JSImport.Default)
@js.native
object ReactParticleEffectButton extends js.Object

@react object ParticleEffectButton extends ExternalComponent {

  case class Props(hidden: Option[Boolean] = None,
                   color: Option[String] = None,
                   duration: Option[Int] = None,
                   easing: Option[String] = None,
                   `type`: Option[String] = None,
                   style: Option[String] = None,
                   direction: Option[String] = None,
                   canvasPadding: Option[Int] = None,
                   size: Option[Int] = None,
                   speed: Option[Int] = None,
                   particlesAmountCoefficient: Option[Int] = None,
                   oscillationCoefficient: Option[Int] = None,
                   onBegin: Option[() => Unit] = None,
                   onComplete: Option[() => Unit] = None,
                   children: List[ReactElement])

  override val component = ReactParticleEffectButton
}