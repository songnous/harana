package com.harana.sdk.backend.models.flow.actions.readwritedataframe

import com.harana.sdk.shared.models.flow.exceptions.FlowError
import io.circe.generic.JsonCodec

sealed abstract class FileScheme(val scheme: String) {

  def pathPrefix = scheme + "://"

}

object FileScheme {

  case object HTTP    extends FileScheme("http")
  case object HTTPS   extends FileScheme("https")
  case object FTP     extends FileScheme("ftp")
  case object HDFS    extends FileScheme("hdfs")
  case object File    extends FileScheme("file")
  case object Library extends FileScheme("library")

  // TODO Autoderive values. There is macro-library for extracting sealed case objects.
  val values = Seq(HTTP, HTTPS, FTP, HDFS, File, Library)

  val supportedByParquet = Seq(HDFS)

  def fromPath(path: String): FileScheme = {
    val matchingFileSchema = values.find(schema => path.startsWith(schema.pathPrefix))
    matchingFileSchema.getOrElse(throw UnknownFileSchemaForPath(path).toException)
  }
}

case class FilePath(fileScheme: FileScheme, pathWithoutScheme: String) {

  def fullPath = fileScheme.pathPrefix + pathWithoutScheme

  def verifyScheme(assertedFileScheme: FileScheme) = assert(fileScheme == assertedFileScheme)

}

object FilePath {

  def apply(fullPath: String): FilePath = {
    val schema            = FileScheme.fromPath(fullPath)
    val pathWithoutSchema = fullPath.substring(schema.pathPrefix.length)
    FilePath(schema, pathWithoutSchema)
  }

  def unapply(fullPath: String): Option[(FileScheme, String)] = unapply(FilePath(fullPath))

}

@JsonCodec
case class UnknownFileSchemaForPath(path: String) extends FlowError {
  val allSchemes = FileScheme.values.map(_.scheme).mkString("(", ", ", ")")
  val message = s"Unknown file scheme for path $path. Known file schemes: $allSchemes"
}