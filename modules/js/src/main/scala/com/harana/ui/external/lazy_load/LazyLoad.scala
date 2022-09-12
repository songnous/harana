package com.harana.ui.external.lazy_load

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.std.HTMLElement
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-lazyload", JSImport.Default)
@js.native
object ReactLazyLoad extends js.Object

@react object LazyLoad extends ExternalComponent {

  case class Props(children: ReactElement,
                   debounce: js.UndefOr[Double | Boolean] = js.undefined,
                   height: js.UndefOr[Double | String] = js.undefined,
                   offset: js.UndefOr[Double | List[Double]] = js.undefined,
                   once: js.UndefOr[Boolean] = js.undefined,
                   overflow: js.UndefOr[Boolean] = js.undefined,
                   placeholder: js.UndefOr[js.Any] = js.undefined,
                   resize: js.UndefOr[Boolean] = js.undefined,
                   scroll: js.UndefOr[Boolean] = js.undefined,
                   scrollContainer: js.UndefOr[String] = js.undefined,
                   throttle: js.UndefOr[Double | Boolean] = js.undefined,
                   unmountIfInvisible: js.UndefOr[Boolean] = js.undefined)

  override val component = ReactLazyLoad
}

