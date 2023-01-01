package com.harana.s3

import com.google.common.base.CharMatcher
import com.google.common.hash.Hashing
import com.google.common.net.PercentEscaper
import com.harana.s3.services.models.AwsHttpHeaders
import com.harana.s3.utils.IPAddress

package object services {


  val userMetdataPrefix = "x-amz-meta-"

  val validBucketFirstChar = CharMatcher.inRange('a', 'z').or(CharMatcher.inRange('A', 'Z')).or(CharMatcher.inRange('0', '9'))
  val validBucket = validBucketFirstChar.or(CharMatcher.is('.')).or(CharMatcher.is('_')).or(CharMatcher.is('-'))

  val maxMultipartCopySize = 5L * 1024L * 1024L * 1024L

  val supportedHeaders = Set(
    AwsHttpHeaders.ACL,
    AwsHttpHeaders.CONTENT_SHA256,
    AwsHttpHeaders.COPY_SOURCE,
    AwsHttpHeaders.COPY_SOURCE_IF_MATCH,
    AwsHttpHeaders.COPY_SOURCE_IF_MODIFIED_SINCE,
    AwsHttpHeaders.COPY_SOURCE_IF_NONE_MATCH,
    AwsHttpHeaders.COPY_SOURCE_IF_UNMODIFIED_SINCE,
    AwsHttpHeaders.COPY_SOURCE_RANGE,
    AwsHttpHeaders.DATE,
    AwsHttpHeaders.DECODED_CONTENT_LENGTH,
    AwsHttpHeaders.METADATA_DIRECTIVE,
    AwsHttpHeaders.STORAGE_CLASS
  )

  val unsupportedParameters = Set(
    "accelerate", "analytics", "cors", "inventory", "lifecycle", "logging",
    "metrics", "notification", "replication", "requestPayment", "restore",
    "tagging", "torrent", "versioning", "versions", "website")

  val cannedAcls = Set(
    "private", "public-read", "public-read-write", "authenticated-read",
    "bucket-owner-read", "bucket-owner-full-control", "log-delivery-write"
  )

  val urlEscaper = new PercentEscaper("*-./_", /*plusForSpace=*/ false)

  def isValidContainer(containerName: String) =
    containerName == null ||
    containerName.length < 3 ||
    containerName.length > 255 ||
    containerName.startsWith(".") ||
    containerName.endsWith(".") ||
    IPAddress.validate(containerName) ||
    !validBucketFirstChar.matches(containerName.charAt(0)) ||
    !validBucket.matchesAllOf(containerName)

}
