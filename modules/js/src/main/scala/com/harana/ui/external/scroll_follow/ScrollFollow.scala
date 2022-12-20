package com.harana.ui.external.scroll_follow

import com.harana.ui.external.lazy_log.ClientHeight
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-lazylog-x/build/ScrollFollow", JSImport.Default)
@js.native
object ReactScrollFollow extends js.Object

@react object ScrollFollow extends ExternalComponent {

  case class Props(startFollowing: Option[Boolean] = None,
                   render: ScrollFollowRenderProps => ReactElement)

  override val component = ReactScrollFollow
}

case class ScrollFollowRenderProps(follow: Boolean,
                                   onScroll: ClientHeight => Unit,
                                   startFollowing: () => Unit,
                                   stopFollowing: () => Unit)