package com.harana.ui.external.flip_move

import com.harana.ui.external.flip_move.Types._
import org.scalablytyped.runtime.StringDictionary
import org.scalajs.dom.HTMLElement
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import typings.std.{CSSStyleDeclaration, Partial}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-flip-move", JSImport.Namespace)
@js.native
object ReactFlipMove extends js.Object

@react object FlipMove extends ExternalComponent {

  case class Props(appearAnimation: Option[AnimationProp] = None,
                   className: Option[String] = None,
                   delay: Option[Double | String] = None,
                   disableAllAnimations: Option[Boolean] = None,
                   duration: Option[Double | String] = None,
                   easing: Option[String] = None,
                   enterAnimation: Option[AnimationProp] = None,
                   getPosition: Option[HTMLElement => ClientRect] = None,
                   leaveAnimation: Option[AnimationProp] = None,
                   maintainContainerHeight: Option[Boolean] = None,
                   onFinish: Option[(ReactElement, HTMLElement) => Unit] = None,
                   onFinishAll: Option[(List[ReactElement], List[HTMLElement]) => Unit] = None,
                   onStart: Option[(ReactElement, HTMLElement) => Unit] = None,
                   onStartAll: Option[(List[ReactElement], List[HTMLElement]) => Unit] = None,
                   staggerDelayBy: Option[Double | String] = None,
                   staggerDurationBy: Option[Double | String] = None,
                   style: Option[Styles] = None,
                   typeName: Option[String | Null] = None,
                   verticalAlignment: Option[String] = None)

  override val component = ReactFlipMove
}

object Types {
  type AnimationPreset = String
  type AnimationProp = AnimationPreset | Animation | Boolean
  type Styles = StringDictionary[String | Double]
}

case class ClientRect(bottom: Double,
                      height: Double,
                      left: Double,
                      right: Double,
                      top: Double,
                      width: Double)

case class Animation(from: Partial[CSSStyleDeclaration], to: Partial[CSSStyleDeclaration])