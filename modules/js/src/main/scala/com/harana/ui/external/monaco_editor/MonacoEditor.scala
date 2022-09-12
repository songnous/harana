package com.harana.ui.external.monaco_editor

import com.harana.ui.external.monaco_editor.Types._
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-monaco-editor", JSImport.Namespace)
@js.native
object ReactMonacoEditor extends js.Object

@react object MonacoEditor extends ExternalComponent {

  case class Props(context: Option[js.Any] = None,
                   defaultValue: Option[String] = None,
                   height: Option[String | Double] = None,
                   language: Option[String] = None,
                   requireConfig: Option[js.Any] = None,
                   theme: Option[String | Null] = None,
                   width: Option[String | Double] = None,
                   editorDidMount: Option[EditorDidMount] = None,
                   editorWillMount: Option[EditorWillMount] = None,
                   onChange: Option[ChangeHandler] = None,
                   options:  Option[js.Any] = None,
                   value: Option[String | Null] = None)

  override val component = ReactMonacoEditor
}

object Types {
  type ChangeHandler = (String, js.Any) => Unit
  type EditorDidMount = (js.Any, js.Any) => Unit
  type EditorWillMount = js.Any => Unit
}