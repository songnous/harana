package com.harana.ui.external.highlight_words

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-highlight-words", JSImport.Namespace)
@js.native
object ReactHighlightWords extends js.Object

@react object HighlightWords extends ExternalComponent {

  case class Props(activeClassName: Option[String] = None,
                   activeIndex: Option[Double] = None,
                   activeStyle: Option[CSSProperties] = None,
                   autoEscape: Option[Boolean] = None,
                   caseSensitive: Option[Boolean] = None,
                   className: Option[String] = None,
                   findChunks: Option[FindChunks => List[Chunk]] = None,
                   highlightClassName: Option[String] = None,
                   highlightStyle: Option[CSSProperties] = None,
                   highlightTag: Option[ReactElement] = None,
                   sanitize: Option[String => String] = None,
                   searchWords: List[String],
                   textToHighlight: String,
                   unhighlightClassName: Option[String] = None,
                   unhighlightStyle: Option[CSSProperties] = None)

  override val component = ReactHighlightWords
}

case class Chunk(end: Double, start: Double)

case class FindChunks(autoEscape: Option[Boolean],
                      caseSensitive: Option[Boolean],
                      sanitize: Option[String => String],
                      searchWords: List[String],
                      textToHighlight: String)