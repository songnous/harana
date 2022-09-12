package com.harana.ui.external.lazy_log

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import typings.react.mod.CSSProperties
import typings.std

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-lazylog", "LazyLog")
@js.native
object ReactLazyLog extends js.Object

@react object LazyLog extends ExternalComponent {

  case class Props(caseInsensitive: js.UndefOr[Boolean] = js.undefined,
                   containerStyle: js.UndefOr[CSSProperties] = js.undefined,
                   enableSearch: js.UndefOr[Boolean] = js.undefined,
                   extraLines: js.UndefOr[Int] = js.undefined,
                   fetchOptions: js.UndefOr[std.RequestInit] = js.undefined,
                   follow: js.UndefOr[Boolean] = js.undefined,
                   formatPart: js.UndefOr[String => String] = js.undefined,
                   height: js.UndefOr[String | Double] = js.undefined,
                   highlight: js.UndefOr[Double | List[Double]] = js.undefined,
                   highlightLineClassName: js.UndefOr[String] = js.undefined,
                   lineClassName: js.UndefOr[String] = js.undefined,
                   onError: js.UndefOr[js.Any => Unit] = js.undefined,
                   onHighlight: js.UndefOr[std.Range => Unit] = js.undefined,
                   onLoad: js.UndefOr[() => Unit] = js.undefined,
                   overscanRowCount: js.UndefOr[Double] = js.undefined,
                   rowHeight: js.UndefOr[Double] = js.undefined,
                   scrollToLine: js.UndefOr[Int] = js.undefined,
                   selectableLines: js.UndefOr[Boolean] = js.undefined,
                   stream: js.UndefOr[Boolean] = js.undefined,
                   style: js.UndefOr[CSSProperties] = js.undefined,
                   text: js.UndefOr[String] = js.undefined,
                   url: String,
                   width: js.UndefOr[String | Double] = js.undefined)

  override val component = ReactLazyLog
}

@js.native
trait ClientHeight extends js.Object {
  val clientHeight: Double
  val scrollHeight: Double
  val scrollTop: Double
}

@js.native
trait Text extends js.Object {
  val text: String
}