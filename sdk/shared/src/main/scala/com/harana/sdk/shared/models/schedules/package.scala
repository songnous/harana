package com.harana.sdk.shared.models

import com.harana.sdk.shared.models.common.{Parameter, ParameterValue}
import enumeratum.{CirceEnum, Enum, EnumEntry}

package object schedules {

  sealed trait ActionCompressionType extends EnumEntry
  case object ActionCompressionType extends Enum[ActionCompressionType] with CirceEnum[ActionCompressionType] {
    case object Bzip2 extends ActionCompressionType
    case object Gzip extends ActionCompressionType
    case object Jar extends ActionCompressionType
    case object Rar extends ActionCompressionType
    case object Tar extends ActionCompressionType
    case object TarGzip extends ActionCompressionType
    case object Xz extends ActionCompressionType
    case object Zip extends ActionCompressionType
    val values = findValues
  }

  val compressionFileTypeParameters = Parameter.String(
    name = "compressionFileType",
    options = List(
      "bzip2" -> ParameterValue.String("bzip2"),
      "gzip" -> ParameterValue.String("gzip"),
      "jar" -> ParameterValue.String("jar"),
      "rar" -> ParameterValue.String("rar"),
      "tar" -> ParameterValue.String("tar"),
      "tarGzip" -> ParameterValue.String("tarGzip"),
      "xz" -> ParameterValue.String("xz"),
      "zip" -> ParameterValue.String("zip")
    )
  )

  sealed trait ActionFileSource extends EnumEntry
  case object ActionFileSource extends Enum[ActionFileSource] with CirceEnum[ActionFileSource] {
    case object AwsS3 extends ActionFileSource
    case object AzureBs extends ActionFileSource
    case object Ftp extends ActionFileSource
    case object Ftps extends ActionFileSource
    case object Gcs extends ActionFileSource
    case object Harana extends ActionFileSource
    case object Http extends ActionFileSource
    case object Https extends ActionFileSource
    case object Sftp extends ActionFileSource
    case object Smb extends ActionFileSource
    case object WebDav extends ActionFileSource
    val values = findValues
  }

  val fileTypeParameters = Parameter.String(
    name = "fileType",
    options = List(
      "awsS3" -> ParameterValue.String("awsS3"),
      "azureBs" -> ParameterValue.String("azureBs"),
      "ftp" -> ParameterValue.String("ftp"),
      "ftps" -> ParameterValue.String("ftps"),
      "gcs" -> ParameterValue.String("gcs"),
      "harana" -> ParameterValue.String("harana"),
      "http" -> ParameterValue.String("http"),
      "https" -> ParameterValue.String("https"),
      "sftp" -> ParameterValue.String("sftp"),
      "smb" -> ParameterValue.String("smb"),
      "webDav" -> ParameterValue.String("webDav")
    )
  )

  sealed trait ActionMode extends EnumEntry
  case object ActionMode extends Enum[ActionMode] with CirceEnum[ActionMode] {
    case object Parallel extends ActionMode
    case object Sequential extends ActionMode
    val values = findValues
  }

  sealed trait EventMode extends EnumEntry
  case object EventMode extends Enum[EventMode] with CirceEnum[EventMode] {
    case object All extends EventMode
    case object Any extends EventMode
    val values = findValues
  }

  sealed trait NotifierType extends EnumEntry
  case object NotifierType extends Enum[NotifierType] with CirceEnum[NotifierType] {
    case object Email extends NotifierType
    case object OpsGenie extends NotifierType
    case object PagerDuty extends NotifierType
    case object PushOver extends NotifierType
    case object Slack extends NotifierType
    case object SplunkOnCall extends NotifierType
    case object WebHook extends NotifierType
    case object WeChat extends NotifierType
    val values = findValues
  }
}
