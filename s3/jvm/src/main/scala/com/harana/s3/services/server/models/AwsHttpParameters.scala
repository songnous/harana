package com.harana.s3.services.server.models

import enumeratum.values.{StringEnum, StringEnumEntry}

sealed abstract class AwsHttpParameters(val value: String) extends StringEnumEntry

case object AwsHttpParameters extends StringEnum[AwsHttpParameters] {
  case object ACCESS_KEY_ID extends AwsHttpParameters("AWSAccessKeyId")
  case object ACL extends AwsHttpParameters("acl")
  case object ALGORITHM extends AwsHttpParameters("X-Amz-Algorithm")
  case object CONTINUATION_TOKEN extends AwsHttpParameters("continuation-token")
  case object CREDENTIAL extends AwsHttpParameters("X-Amz-Credential")
  case object DATE extends AwsHttpParameters("X-Amz-Date")
  case object DELETE extends AwsHttpParameters("delete")
  case object DELIMITER extends AwsHttpParameters("delimiter")
  case object ENCODING_TYPE extends AwsHttpParameters("encoding-type")
  case object EXPIRES_V2 extends AwsHttpParameters("Expires")
  case object EXPIRES_V4 extends AwsHttpParameters("X-Amz-Expires")
  case object FETCH_OWNER extends AwsHttpParameters("fetch-owner")
  case object KEY_MARKER extends AwsHttpParameters("key-marker")
  case object LIST_TYPE extends AwsHttpParameters("list-type")
  case object LOCATION extends AwsHttpParameters("location")
  case object MARKER extends AwsHttpParameters("marker")
  case object MAX_KEYS extends AwsHttpParameters("max-keys")
  case object MAX_UPLOADS extends AwsHttpParameters("max-uploads")
  case object PART_NUMBER extends AwsHttpParameters("partNumber")
  case object PART_NUMBER_MARKER extends AwsHttpParameters("part-number-marker")
  case object POLICY extends AwsHttpParameters("policy")
  case object PREFIX extends AwsHttpParameters("prefix")
  case object RESPONSE_CONTENT_TYPE extends AwsHttpParameters("response-content-type")
  case object RESPONSE_EXPIRES extends AwsHttpParameters("response-expires")
  case object SIGNATURE extends AwsHttpParameters("X-Amz-Signature")
  case object SIGNED_HEADERS extends AwsHttpParameters("X-Amz-SignedHeaders")
  case object START_AFTER extends AwsHttpParameters("start-after")
  case object UPLOAD_ID extends AwsHttpParameters("uploadId")
  case object UPLOAD_ID_MARKER extends AwsHttpParameters("upload-id-marker")
  case object UPLOADS extends AwsHttpParameters("uploads")
  val values = findValues
}