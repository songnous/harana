package com.harana.ui.external.monaco_diff_editor

import com.harana.ui.external.monaco_diff_editor.Types._
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-monaco-editor", "MonacoDiffEditor")
@js.native
object ReactMonacoDiffEditor extends js.Object

@react object MonacoDiffEditor extends ExternalComponent {

  case class Props(context: Option[js.Any] = None,
                   defaultValue: Option[String] = None,
                   height: Option[String | Double] = None,
                   language: Option[String] = None,
                   requireConfig: Option[js.Any] = None,
                   theme: Option[String | Null] = None,
                   width: Option[String | Double] = None,
                   editorDidMount: Option[DiffEditorDidMount] = None,
                   editorWillMount: Option[DiffEditorWillMount] = None,
                   onChange: Option[DiffChangeHandler] = None,
                   options:  Option[js.Any] = None,
                   original: Option[String] = None,
                   value: Option[String] = None)

  override val component = ReactMonacoDiffEditor
}

object Types {
  type DiffChangeHandler = String => Unit
  type DiffEditorDidMount = (js.Any, js.Any) => Unit
  type DiffEditorWillMount = js.Any => Unit
}