package com.harana.ui.external.markdown

import com.harana.ui.external.markdown.Types._
import org.scalablytyped.runtime.StringDictionary
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-markdown", JSImport.Default)
@js.native
object ReactMarkdown extends js.Object {
  val renderers: Renderers = js.native
  val types: List[NodeType] = js.native
  val uriTransformer: String => String = js.native
}

@react object Markdown extends ExternalComponent {

  case class Props(source: String,
                   allowNode: Option[(MarkdownAbstractSyntaxTree, Double, NodeType) => Boolean] = None,
                   className: Option[String] = None,
                   disallowedTypes: Option[List[NodeType]] = None,
                   escapeHtml: Option[Boolean] = None,
                   includeNodeIndex: Option[Boolean] = None,
                   linkTarget: Option[String | LinkTargetResolver] = None,
                   plugins: Option[js.Object] = None,
                   rawSourcePos: Option[Boolean] = None,
                   renderers: Option[js.Any] = None,
                   skipHtml: Option[Boolean] = None,
                   sourcePos: Option[Boolean] = None,
                   transformImageUri: Option[(String, ReactElement, String, String) => String] = None,
                   transformLinkUri: Option[(String, ReactElement, String) => String] = None,
                   unwrapDisallowed: Option[Boolean] = None)

  override val component = ReactMarkdown
}

object Types {
  type AlignType = String | Null
  type LinkTargetResolver = (String, String, String) => String
  type NodeType = String
  type ReferenceType = String
  type Renderer[T] = T => ReactElement
}

@js.native
trait MarkdownAbstractSyntaxTree extends js.Object {
  val align: List[AlignType]
  val alt: Option[String | Null]
  val checked: Option[Boolean | Null]
  val children: List[MarkdownAbstractSyntaxTree]
  val data: Option[StringDictionary[js.Any]]
  val depth: Option[Double]
  val height: Option[Double]
  val identifier: Option[String]
  val index: Option[Double]
  val lang: Option[String | Null]
  val loose: Option[Boolean]
  val ordered: Option[Boolean]
  val position: Option[Position]
  val referenceType: Option[ReferenceType]
  val start: Option[Double | Null]
  val title: Option[String | Null]
  val `type`: String
  val url: Option[String]
  val value: Option[String]
  val width: Option[Double]
}

@js.native
trait Point extends js.Object {
  val column: Double
  val line: Double
  val offset: Option[Double]
}

@js.native
trait Position extends js.Object {
  val end: Point
  val indent: List[Double]
  val start: Point
}

trait Renderers extends StringDictionary[String | Renderer[_]]