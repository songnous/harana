package com.harana.ui.external.filepond

import org.scalablytyped.runtime.StringDictionary
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSImport, JSName}
import scala.scalajs.js.|

@JSImport("filepond/dist/filepond.min.css", JSImport.Default)
@js.native
object ReactFilePondCSS extends js.Object

@JSImport("react-filepond", "FilePond")
@js.native
object ReactFilePond extends js.Object {
  def create(): Unit = js.native
  def create(element: js.Any): Unit = js.native
  def destroy(element: js.Any): Unit = js.native
  def parse(context: js.Any): Unit = js.native
  def registerPlugin(plugins: js.Any*): Unit = js.native
  def supported(): Boolean = js.native
  def addFile(source: UploadedFile): Unit = js.native
  def addFiles(source: List[UploadedFile]): Unit = js.native
  def browse(): Unit = js.native

  @JSName("context")
  def context_MFilePond(): Unit = js.native
  def getFile(): UploadedFile = js.native
  def getFiles(): List[UploadedFile] = js.native
  def processFile(query: String): Unit = js.native
  def processFiles(): Unit = js.native
  def removeFile(query: String): Unit = js.native
  def removeFiles(): Unit = js.native
}

@react object FilePond extends ExternalComponent {

  case class Props(acceptedFileTypes: js.Array[String] = js.Array(),
                   allowBrowse: js.UndefOr[Boolean] = js.undefined,
                   allowDrop: js.UndefOr[Boolean] = js.undefined,
                   allowMultiple: js.UndefOr[Boolean] = js.undefined,
                   allowPaste: js.UndefOr[Boolean] = js.undefined,
                   allowReplace: js.UndefOr[Boolean] = js.undefined,
                   allowRevert: js.UndefOr[Boolean] = js.undefined,
                   captureMethod: js.UndefOr[js.Any] = js.undefined,
                   children: js.UndefOr[ReactElement | List[ReactElement]] = js.undefined,
                   className: js.UndefOr[String] = js.undefined,
                   files: js.Array[UploadedFile] = js.Array(),
                   id: js.UndefOr[String] = js.undefined,
                   maxFiles: js.UndefOr[Double] = js.undefined,
                   maxParallelUploads: js.UndefOr[Double] = js.undefined,
                   metadata: js.UndefOr[StringDictionary[js.Any]] = js.undefined,
                   name: js.UndefOr[String] = js.undefined,
                   required: js.UndefOr[Boolean] = js.undefined,
                   beforeRemoveFile: js.UndefOr[File => Boolean] = js.undefined,
                   dropOnElement: js.UndefOr[Boolean] = js.undefined,
                   dropOnPage: js.UndefOr[Boolean] = js.undefined,
                   dropValidation: js.UndefOr[Boolean] = js.undefined,
                   ignoredFiles: js.Array[String] = js.Array(),
                   instantUpload: js.UndefOr[Boolean] = js.undefined,
                   iconProcess: js.UndefOr[String] = js.undefined,
                   iconRemove: js.UndefOr[String] = js.undefined,
                   iconRetry: js.UndefOr[String] = js.undefined,
                   iconUndo: js.UndefOr[String] = js.undefined,
                   server: js.UndefOr[String | Fetch] = js.undefined,
                   labelButtonAbortItemLoad: js.UndefOr[String] = js.undefined,
                   labelButtonAbortItemProcessing: js.UndefOr[String] = js.undefined,
                   labelButtonProcessItem: js.UndefOr[String] = js.undefined,
                   labelButtonRemoveItem: js.UndefOr[String] = js.undefined,
                   labelButtonRetryItemLoad: js.UndefOr[String] = js.undefined,
                   labelButtonRetryItemProcessing: js.UndefOr[String] = js.undefined,
                   labelButtonUndoItemProcessing: js.UndefOr[String] = js.undefined,
                   labelDecimalSeparator: js.UndefOr[String] = js.undefined,
                   labelFileLoadError: js.UndefOr[String] = js.undefined,
                   labelFileLoading: js.UndefOr[String] = js.undefined,
                   labelFileProcessing: js.UndefOr[String] = js.undefined,
                   labelFileProcessingAborted: js.UndefOr[String] = js.undefined,
                   labelFileProcessingComplete: js.UndefOr[String] = js.undefined,
                   labelFileProcessingError: js.UndefOr[String] = js.undefined,
                   labelFileSizeNotAvailable: js.UndefOr[String] = js.undefined,
                   labelFileWaitingForSize: js.UndefOr[String] = js.undefined,
                   labelIdle: js.UndefOr[String] = js.undefined,
                   labelTapToCancel: js.UndefOr[String] = js.undefined,
                   labelTapToRetry: js.UndefOr[String] = js.undefined,
                   labelTapToUndo: js.UndefOr[String] = js.undefined,
                   labelThousandsSeparator: js.UndefOr[String] = js.undefined,
                   onaddfile: js.UndefOr[(File, FilePondErrorDescription) => Unit] = js.undefined,
                   onaddfileprogress: js.UndefOr[(File, Double) => Unit] = js.undefined,
                   onaddfilestart: js.UndefOr[File => Unit] = js.undefined,
                   onerror: js.UndefOr[(File, FilePondErrorDescription, js.Any) => Unit] = js.undefined,
                   oninit: js.UndefOr[() => Unit] = js.undefined,
                   onpreparefile: js.UndefOr[(File, js.Any) => Unit] = js.undefined,
                   onprocessfile: js.UndefOr[(File, FilePondErrorDescription) => Unit] = js.undefined,
                   onprocessfileabort: js.UndefOr[File => Unit] = js.undefined,
                   onprocessfileprogress: js.UndefOr[(File, Double) => Unit] = js.undefined,
                   onprocessfilestart: js.UndefOr[File => Unit] = js.undefined,
                   onprocessfileundo: js.UndefOr[File => Unit] = js.undefined,
                   onremovefile: js.UndefOr[File => Unit] = js.undefined,
                   onupdatefiles: js.UndefOr[js.Array[File] => Unit] = js.undefined,
                   onwarning: js.UndefOr[(js.Any, File, js.Any) => Unit] = js.undefined,
                   stylePanelLayout: js.UndefOr[String] = js.undefined)

  override val component = ReactFilePond
}

@js.native
trait Fetch extends js.Object {
  val fetch: String | ServerUrl = js.native
  val load: String | ServerUrl = js.native
  val process: String | ServerUrl = js.native
  val restore: String | ServerUrl = js.native
  val revert: String | ServerUrl = js.native
}

@js.native
trait File extends js.Object {
  val archived: Boolean = js.native
  val file: UploadedFile = js.native
  val fileExtension: String = js.native
  val fileSize: Double = js.native
  val fileType: String = js.native
  val filename: String = js.native
  val filenameWithoutExtension: String = js.native
  val id: String = js.native
  val serverId: String = js.native
  val status: Double = js.native

  def abortLoad(): Unit = js.native
  def abortProcessing(): Unit = js.native
  def getMetadata(): js.Any = js.native
  def getMetadata(key: String): js.Any = js.native
  def setMetadata(key: String, value: js.Any): Unit = js.native
}

@js.native
trait UploadedFile extends js.Object {
  val name: String = js.native
  val size: Int = js.native
  val lastModifiedDate: js.Date = js.native
  val `type`: String = js.native
}


@js.native
trait FilePondErrorDescription extends js.Object {
  val main: String = js.native
  val sub: String = js.native
}

@js.native
trait LastModified extends js.Object {
  val lastModified: Double = js.native
  val name: String = js.native
}

@js.native
trait ServerUrl extends js.Object {
  val headers: js.UndefOr[StringDictionary[String | Boolean | Double]] = js.native
  val method: js.UndefOr[String] = js.native
  val ondata: js.UndefOr[js.Any => Unit] = js.native
  val onerror: js.UndefOr[js.Any => Unit] = js.native 
  val onload: js.UndefOr[() => Unit] = js.native
  val timeout: js.UndefOr[Double] = js.native
  val url: String = js.native
  val withCredentials: js.UndefOr[Boolean] = js.native
}