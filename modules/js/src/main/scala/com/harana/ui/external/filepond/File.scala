package com.harana.ui.external.filepond

import org.scalablytyped.runtime.StringDictionary
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-filepond", "File")
@js.native
object ReactFile extends js.Object {
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