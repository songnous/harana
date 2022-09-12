package com.harana.ui.external.ace_editor

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSImport, JSName}
import scala.scalajs.js.|

@JSImport("react-ace", JSImport.Default)
@js.native
object ReactAceEditor extends js.Object

@JSImport("ace-builds/src-noconflict/ace", JSImport.Default)
@js.native
object ReactAce extends js.Object

@JSImport("ace-builds/src-noconflict/mode-python", JSImport.Default)
@js.native
object ReactAcePython extends js.Object

@react object AceEditor extends ExternalComponent {

  case class Props(annotations: List[Annotation] = List(),
                   className: Option[String] = None,
                   commands: List[Command] = List(),
                   cursorStart: Option[Double] = None,
                   debounceChangePeriod: Option[Double] = None,
                   defaultValue: Option[String] = None,
                   editorProps: Option[Editor] = None,
                   enableBasicAutocompletion: Option[Boolean] = None,
                   enableLiveAutocompletion: Option[Boolean] = None,
                   focus: Option[Boolean] = None,
                   fontSize: Option[Double] = None,
                   height: Option[String] = None,
                   highlightActiveLine: Option[Boolean] = None,
                   keyboardHandler: Option[String] = None,
                   markers: List[Marker] = List(),
                   maxLines: Option[Double] = None,
                   minLines: Option[Double] = None,
                   mode: Option[String] = None,
                   name: Option[String] = None,
                   onBeforeLoad: Option[js.Any => Unit] = None,
                   onBlur: Option[js.Any => Unit] = None,
                   onChange: Option[(String, js.Any) => Unit] = None,
                   onCopy: Option[String=> Unit] = None,
                   onCursorChange: Option[js.Object => Unit] = None,
                   onFocus: Option[js.Any => Unit] = None,
                   onInput: Option[(String, js.Any) => Unit] = None,
                   onLoad: Option[Editor => Unit] = None,
                   onPaste: Option[String=> Unit] = None,
                   onScroll: Option[Editor => Unit] = None,
                   onSelectionChange: Option[(String, js.Any) => Unit] = None,
                   onValidate: Option[Annotation => Unit] = None,
                   readOnly: Option[Boolean] = None,
                   scrollMargin: List[Double] = List(),
                   setOptions: Option[AceOptions] = None,
                   showGutter: Option[Boolean] = None,
                   showPrintMargin: Option[Boolean] = None,
                   style: Option[CSSProperties] = None,
                   tabSize: Option[Double] = None,
                   theme: Option[String] = None,
                   value: Option[String] = None,
                   width: Option[String] = None,
                   wrapEnabled: Option[Boolean] = None)

  override val component = ReactAceEditor
}

case class AceOptions(animatedScroll: Option[Boolean],
                      autoScrollEditorIntoView: Option[Boolean],
                      behavioursEnabled: Option[Boolean],
                      cursorStyle: Option[String],
                      displayIndentGuides: Option[Boolean],
                      dragDelay: Option[Double],
                      dragEnabled: Option[Boolean],
                      enableBasicAutocompletion: Option[Boolean],
                      enableEmmet: Option[Boolean],
                      enableLiveAutocompletion: Option[Boolean],
                      enableMultiselect: Option[Boolean],
                      enableSnippets: Option[Boolean],
                      fadeFoldWidgets: Option[Boolean],
                      firstLineNumber: Option[Double],
                      fixedWidthGutter: Option[Boolean],
                      focusTimout: Option[Double],
                      foldStyle: Option[Boolean],
                      fontFamily: Option[String],
                      fontSize: Option[Double | String],
                      hScrollBarAlwaysVisible: Option[Boolean],
                      highlightActiveLine: Option[Boolean],
                      highlightGutterLine: Option[Boolean],
                      highlightSelectedWord: Option[Boolean],
                      maxLines: Option[Double],
                      mergeUndoDeltas: Option[Boolean | String],
                      minLines: Option[Double],
                      mode: Option[String],
                      newLineMode: Option[Boolean],
                      overwrite: Option[Boolean],
                      printMargin: Option[Boolean],
                      printMarginColumn: Option[Boolean],
                      readOnly: Option[Boolean],
                      scrollPastEnd: Option[Boolean],
                      scrollSpeed: Option[Double],
                      selectionStyle: Option[String],
                      showFoldWidgets: Option[Boolean],
                      showGutter: Option[Boolean],
                      showInvisibles: Option[Boolean],
                      showLineNumbers: Option[Boolean],
                      showPrintMargin: Option[Boolean],
                      spellcheck: Option[Boolean],
                      tabSize: Option[Double],
                      theme: Option[String],
                      tooltipFollowsMouse: Option[Boolean],
                      useElasticTabstops: Option[Boolean],
                      useSoftTabs: Option[Boolean],
                      useWorker: Option[Boolean],
                      vScrollBarAlwaysVisible: Option[Boolean],
                      wrap: Option[Boolean],
                      wrapBehavioursEnabled: Option[Boolean])

@js.native
trait Annotation extends js.Object {
  val column: Double
  val row: Double
  val text: String
  val `type`: String
}

@js.native
trait Command extends js.Object {
  val bindKey: CommandBindKey
  val name: String
}

@js.native
trait CommandBindKey extends js.Object {
  val mac: String
  val win: String
}

@js.native
trait Editor extends js.Object {
  val $blockScrolling: Option[Double | Boolean]
  val $blockSelectEnabled: Option[Boolean]
  val $enableBlockSelect: Option[Boolean]
  val $enableMultiselect: Option[Boolean]
  val $highlightPending: Option[Boolean]
  val $highlightTagPending: Option[Boolean]
  val $multiselectOnSessionChange: Option[js.Any => Unit]
  val $onAddRange: Option[js.Any => Unit]
  val $onChangeAnnotation: Option[js.Any => Unit]
  val $onChangeBackMarker: Option[js.Any => Unit]
  val $onChangeBreakpoint: Option[js.Any => Unit]
  val $onChangeFold: Option[js.Any => Unit]
  val $onChangeFrontMarker: Option[js.Any => Unit]
  val $onChangeMode: Option[js.Any => Unit]
  val $onChangeTabSize: Option[js.Any => Unit]
  val $onChangeWrapLimit: Option[js.Any => Unit]
  val $onChangeWrapMode: Option[js.Any => Unit]
  val $onCursorChange: Option[js.Any => Unit]
  val $onDocumentChange: Option[js.Any => Unit]
  val $onMultiSelect: Option[js.Any => Unit]
  val $onRemoveRange: Option[js.Any => Unit]
  val $onScrollLeftChange: Option[js.Any => Unit]
  val $onScrollTopChange: Option[js.Any => Unit]
  val $onSelectionChange: Option[js.Any => Unit]
  val $onSingleSelect: Option[js.Any => Unit]
  val $onTokenizerUpdate: Option[js.Any => Unit]
}

@js.native
trait Marker extends js.Object {
  val className: String
  val endCol: Double
  val endRow: Double
  val startCol: Double
  val startRow: Double
  val `type`: String
}