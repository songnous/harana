package com.harana.ui.external.uppy

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait Uppy extends js.Object {
  val id: js.UndefOr[String] = js.undefined
  val autoProceed: js.UndefOr[Boolean] = js.undefined
  val allowMultipleUploads: js.UndefOr[Boolean] = js.undefined
  val debug: js.UndefOr[Boolean] = js.undefined
  val restrictions: js.UndefOr[UppyRestrictions] = js.undefined
  val meta: js.UndefOr[js.Object] = js.undefined
  //val onBeforeFileAdded: js.UndefOr[(currentFile, files) => currentFile
  //val BeforeUpload: js.UndefOr[(files) => {}]
  val locale: js.UndefOr[js.Object] = js.undefined
  val store: js.UndefOr[js.Object] = js.undefined
}

trait UppyRestrictions extends js.Object {
  // val maxFileSize = js.undefOr[Long] = js.undefined
  // val minFileSize = js.undefOr[Long] = js.undefined
  // val maxTotalFileSize = js.undefOr[Long] = js.undefined
  // val maxNumberOfFiles = js.undefOr[Long] = js.undefined
  // val minNumberOfFiles = js.undefOr[Long] = js.undefined
  // val allowedFileTypes = js.Array[String] = js.undefined
}

@JSImport("@uppy/core/dist/style.css", JSImport.Default)
@js.native
object ReactCoreCSS extends js.Object

@JSImport("@uppy/dashboard/dist/style.css", JSImport.Default)
@js.native
object ReactDashboardCSS extends js.Object

@JSImport("@uppy/react", "Dashboard")
@js.native
object ReactUppyDashboard extends js.Object

@react object UppyDashboard extends ExternalComponent {
  case class Props(uppy: Uppy,
                   id: js.UndefOr[String] = js.undefined,
                   target: js.UndefOr[String] = js.undefined,
                   metaFields: js.Array[Metafield] = js.Array(),
                   trigger: js.UndefOr[String] = js.undefined,
                   inline: js.UndefOr[Boolean] = js.undefined,
                   width: js.UndefOr[Int] = js.undefined,
                   height: js.UndefOr[Int] = js.undefined,
                   thumbnailWidth: js.UndefOr[Int] = js.undefined,
                   defaultTabIcon: js.UndefOr[String] = js.undefined,
                   showLinkToFileUploadResult: js.UndefOr[Boolean] = js.undefined,
                   showProgressDetails: js.UndefOr[Boolean] = js.undefined,
                   hideUploadButton: js.UndefOr[Boolean] = js.undefined,
                   hideRetryButton: js.UndefOr[Boolean] = js.undefined,
                   hidePauseResumeButton: js.UndefOr[Boolean] = js.undefined,
                   hideCancelButton: js.UndefOr[Boolean] = js.undefined,
                   hideProgressAfterFinish: js.UndefOr[Boolean] = js.undefined,
                   note: js.UndefOr[String] = js.undefined,
                   closeModalOnClickOutside: js.UndefOr[Boolean] = js.undefined,
                   closeAfterFinish: js.UndefOr[Boolean] = js.undefined,
                   disableStatusBar: js.UndefOr[Boolean] = js.undefined,
                   disableInformer: js.UndefOr[Boolean] = js.undefined,
                   disableThumbnailGenerator: js.UndefOr[Boolean] = js.undefined,
                   disablePageScrollWhenModalOpen: js.UndefOr[Boolean] = js.undefined,
                   animateOpenClose: js.UndefOr[Boolean] = js.undefined,
                   proudlyDisplayPoweredByUppy: js.UndefOr[Boolean] = js.undefined,
                   onRequestCloseModal: js.UndefOr[() => Unit] = js.undefined,
                   showSelectedFiles: js.UndefOr[Boolean] = js.undefined,
                   browserBackButtonClose: js.UndefOr[Boolean] = js.undefined,
                   locale: js.UndefOr[js.Object] = js.undefined)

  override val component = ReactUppyDashboard
}

@JSImport("@uppy/react", "DragDrop")
@js.native
object ReactUppyDragDrop extends js.Object

@react object UppyDragDrop extends ExternalComponent {

  case class Props(uppy: Uppy,
                   id: js.UndefOr[String] = js.undefined,
                   target: js.UndefOr[String] = js.undefined,
                   width: js.UndefOr[String] = js.undefined,
                   height: js.UndefOr[String] = js.undefined,
                   note: js.UndefOr[String] = js.undefined,
                   locale: js.UndefOr[js.Object] = js.undefined)

  override val component = ReactUppyDragDrop
}

@JSImport("@uppy/react", "ProgressBar")
@js.native
object ReactUppyProgressBar extends js.Object

@react object UppyProgressBar extends ExternalComponent {

  case class Props(uppy: Uppy,
                   id: js.UndefOr[String] = js.undefined,
                   target: js.UndefOr[String] = js.undefined,
                   fixed: js.UndefOr[Boolean] = js.undefined,
                   hideAfterFinish: js.UndefOr[Boolean] = js.undefined,
                   replaceTargetContent: js.UndefOr[Boolean] = js.undefined)

  override val component = ReactUppyProgressBar
}

@JSImport("@uppy/react", "StatusBar")
@js.native
object ReactUppyStatusBar extends js.Object

@react object UppyStatusBar extends ExternalComponent {

  case class Props(uppy: Uppy,
                   id: js.UndefOr[String] = js.undefined,
                   target: js.UndefOr[String] = js.undefined,
                   hideAfterFinish: js.UndefOr[Boolean] = js.undefined,
                   showProgressDetails: js.UndefOr[Boolean] = js.undefined,
                   hideUploadButton: js.UndefOr[Boolean] = js.undefined,
                   hideRetryButton: js.UndefOr[Boolean] = js.undefined,
                   hidePauseResumeButton: js.UndefOr[Boolean] = js.undefined,
                   hideCancelButton: js.UndefOr[Boolean] = js.undefined,
                   replaceTargetContent: js.UndefOr[Boolean] = js.undefined,
                   locale: js.UndefOr[js.Object] = js.undefined)

  override val component = ReactUppyStatusBar
}

trait Metafield extends js.Object {
  val id: js.UndefOr[String] = js.undefined
  val name: js.UndefOr[String] = js.undefined 
  val placeholder: js.UndefOr[js.Date] = js.undefined
}