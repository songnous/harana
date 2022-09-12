package typings.std

import scala.scalajs.js
import scala.scalajs.js.`|`
import scala.scalajs.js.annotation._

trait AnimationEventInit extends EventInit {
  var animationName: js.UndefOr[java.lang.String] = js.undefined
  var elapsedTime: js.UndefOr[Double] = js.undefined
  var pseudoElement: js.UndefOr[java.lang.String] = js.undefined
}

object AnimationEventInit {
  @scala.inline
  def apply(
    animationName: java.lang.String = null,
    bubbles: js.UndefOr[scala.Boolean] = js.undefined,
    cancelable: js.UndefOr[scala.Boolean] = js.undefined,
    composed: js.UndefOr[scala.Boolean] = js.undefined,
    elapsedTime: js.UndefOr[Double] = js.undefined,
    pseudoElement: java.lang.String = null
  ): AnimationEventInit = {
    val __obj = js.Dynamic.literal()
    if (animationName != null) __obj.updateDynamic("animationName")(animationName.asInstanceOf[js.Any])
    if (!js.isUndefined(bubbles)) __obj.updateDynamic("bubbles")(bubbles.get.asInstanceOf[js.Any])
    if (!js.isUndefined(cancelable)) __obj.updateDynamic("cancelable")(cancelable.get.asInstanceOf[js.Any])
    if (!js.isUndefined(composed)) __obj.updateDynamic("composed")(composed.get.asInstanceOf[js.Any])
    if (!js.isUndefined(elapsedTime)) __obj.updateDynamic("elapsedTime")(elapsedTime.get.asInstanceOf[js.Any])
    if (pseudoElement != null) __obj.updateDynamic("pseudoElement")(pseudoElement.asInstanceOf[js.Any])
    __obj.asInstanceOf[AnimationEventInit]
  }
}

