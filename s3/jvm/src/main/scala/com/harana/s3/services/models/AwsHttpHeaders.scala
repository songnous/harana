package com.harana.s3.services.models

import enumeratum.values.{StringEnum, StringEnumEntry}

sealed abstract class AwsHttpHeaders(val value: String) extends StringEnumEntry

case object AwsHttpHeaders extends StringEnum[AwsHttpHeaders] {
  case object ACL extends AwsHttpHeaders("x-amz-acl")
  case object CONTENT_SHA256 extends AwsHttpHeaders("x-amz-content-sha256")
  case object COPY_SOURCE extends AwsHttpHeaders("x-amz-copy-source")
  case object COPY_SOURCE_IF_MATCH extends AwsHttpHeaders("x-amz-copy-source-if-match")
  case object COPY_SOURCE_IF_MODIFIED_SINCE extends AwsHttpHeaders("x-amz-copy-source-if-modified-since")
  case object COPY_SOURCE_IF_NONE_MATCH extends AwsHttpHeaders("x-amz-copy-source-if-none-match")
  case object COPY_SOURCE_IF_UNMODIFIED_SINCE extends AwsHttpHeaders("x-amz-copy-source-if-unmodified-since")
  case object COPY_SOURCE_RANGE extends AwsHttpHeaders("x-amz-copy-source-range")
  case object DATE_V2 extends AwsHttpHeaders("x-amz-date")
  case object DATE_V4 extends AwsHttpHeaders("X-Amz-Date")
  case object DECODED_CONTENT_LENGTH extends AwsHttpHeaders("x-amz-decoded-content-length")
  case object METADATA_DIRECTIVE extends AwsHttpHeaders("x-amz-metadata-directive")
  case object REQUEST_ID extends AwsHttpHeaders("x-amz-request-id")
  case object STORAGE_CLASS extends AwsHttpHeaders("x-amz-storage-class")
  val values = findValues
}