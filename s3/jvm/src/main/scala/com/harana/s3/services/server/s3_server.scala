package com.harana.s3.services.server

import com.google.common.base.CharMatcher
import com.google.common.io.{BaseEncoding, ByteStreams}
import com.google.common.net.PercentEscaper
import com.harana.modules.vertx.models.{ContentType, Response}
import com.harana.s3.services.server.models.AuthenticationType._
import com.harana.s3.services.server.models._
import com.harana.s3.utils._
import io.vertx.core.Vertx
import io.vertx.core.http.{HttpHeaders, HttpMethod, HttpServerFileUpload}
import io.vertx.ext.web.RoutingContext
import zio.Task

import scala.jdk.CollectionConverters._
import java.io.{ByteArrayInputStream, InputStream, StringWriter}
import java.security.MessageDigest
import java.util.concurrent.TimeUnit
import javax.xml.stream.{XMLOutputFactory, XMLStreamWriter}

package object s3_server {
  val maxMultipartCopySize = 5L * 1024L * 1024L * 1024L
  val urlEscaper = new PercentEscaper("*-./_", false)

  private val userMetdataPrefix = "x-amz-meta-"
  private val validBucketFirstChar = CharMatcher.inRange('a', 'z').or(CharMatcher.inRange('A', 'Z')).or(CharMatcher.inRange('0', '9'))
  private val validBucket = validBucketFirstChar.or(CharMatcher.is('.')).or(CharMatcher.is('_')).or(CharMatcher.is('-'))

  private val supportedHeaders = Set(
    AwsHttpHeaders.ACL,
    AwsHttpHeaders.CONTENT_SHA256,
    AwsHttpHeaders.COPY_SOURCE,
    AwsHttpHeaders.COPY_SOURCE_IF_MATCH,
    AwsHttpHeaders.COPY_SOURCE_IF_MODIFIED_SINCE,
    AwsHttpHeaders.COPY_SOURCE_IF_NONE_MATCH,
    AwsHttpHeaders.COPY_SOURCE_IF_UNMODIFIED_SINCE,
    AwsHttpHeaders.COPY_SOURCE_RANGE,
    AwsHttpHeaders.DATE_V2,
    AwsHttpHeaders.DATE_V4,
    AwsHttpHeaders.DECODED_CONTENT_LENGTH,
    AwsHttpHeaders.METADATA_DIRECTIVE,
    AwsHttpHeaders.STORAGE_CLASS
  )

  private val unsupportedParameters = Set(
    "accelerate", "analytics", "cors", "inventory", "lifecycle", "logging",
    "metrics", "notification", "replication", "requestPayment", "restore",
    "tagging", "torrent", "versioning", "versions", "website")

  val cannedAcls = Set(
    "private", "public-read", "public-read-write", "authenticated-read",
    "bucket-owner-read", "bucket-owner-full-control", "log-delivery-write"
  )

  def validateParameters(rc: RoutingContext) =
    if (unsupportedParameters.diff(rc.request().params().names().asScala).nonEmpty)
      throw new S3Exception(S3ErrorCode.NOT_IMPLEMENTED)

  def validateHeaders(rc: RoutingContext, ignoreUnknownHeaders: Boolean) =
    rc.request().headers().names().asScala.map(_.toLowerCase).foreach { h =>
      if (!ignoreUnknownHeaders && h.startsWith("x-amz-") && !h.startsWith(userMetdataPrefix) && !supportedHeaders.map(_.value).contains(h))
        throw new S3Exception(S3ErrorCode.NOT_IMPLEMENTED)
    }

  def isValidContainer(containerName: String) =
    containerName == null ||
      containerName.length < 3 ||
      containerName.length > 255 ||
      containerName.startsWith(".") ||
      containerName.endsWith(".") ||
      IPAddress.validate(containerName) ||
      !validBucketFirstChar.matches(containerName.charAt(0)) ||
      !validBucket.matchesAllOf(containerName)

  def isAnonymous(rc: RoutingContext, anonymousIdentity: Boolean) = {
    val method = rc.request().method()
    (!anonymousIdentity && (method.equals(HttpMethod.GET) || method.equals(HttpMethod.HEAD) ||
      method.equals(HttpMethod.POST) || method.equals(HttpMethod.OPTIONS))
      && rc.request().getHeader(HttpHeaders.AUTHORIZATION) == null &&
      !rc.request().params.contains(AwsHttpParameters.ALGORITHM.value) &&
      !rc.request().params.contains(AwsHttpParameters.ACCESS_KEY_ID.value))
  }

  def headerDate(str: String) =
    str.toLong

  def dateHeaders(rc: RoutingContext) = {
    val headers = rc.request().headers().asScala
    (headers.exists(_.getKey == HttpHeaders.DATE),
      headers.exists(_.getKey == AwsHttpHeaders.DATE_V2.value),
      headers.exists(_.getKey == AwsHttpHeaders.DATE_V4.value),
    )
  }

  def validateDateHeaders(rc: RoutingContext, anonymousIdentity: Boolean) = {
    val dh = dateHeaders(rc)
    val expires = rc.request().headers.contains(AwsHttpParameters.EXPIRES_V2.value)
    if (!anonymousIdentity && !dh._1 && !dh._3 && !expires)
      throw new S3Exception(S3ErrorCode.ACCESS_DENIED)
  }

  def validateExpires(rc: RoutingContext) = {
    val expiresStr = rc.request().getParam(AwsHttpParameters.EXPIRES_V2.value)
    if (expiresStr != null) {
      val expires = expiresStr.toLong
      val nowSeconds = System.currentTimeMillis() / 1000
      if (nowSeconds >= expires) throw new S3Exception(S3ErrorCode.ACCESS_DENIED, "Request has expired")
      if (expires - nowSeconds > TimeUnit.DAYS.toSeconds(365)) throw new S3Exception(S3ErrorCode.ACCESS_DENIED)

      val dateStr = rc.request().getParam(AwsHttpHeaders.DATE_V4.value)
      if (dateStr != null) {
        val date = DateTime.parseIso8601(dateStr)
        if (nowSeconds >= date + expires) throw new S3Exception(S3ErrorCode.ACCESS_DENIED, "Request has expired")
        if (expires > TimeUnit.DAYS.toSeconds(7)) throw new Nothing(S3ErrorCode.ACCESS_DENIED)
      }
    }
  }

  def authenticationType(rc: RoutingContext, authHeader: S3AuthorizationHeader, authenticationType: AuthenticationType) = {
    val at = authHeader.authenticationType
    if (at == AuthenticationType.AWS_V2 && (
      authenticationType == AuthenticationType.AWS_V2 ||
        authenticationType == AuthenticationType.AWS_V2_OR_V4)) AuthenticationType.AWS_V2
    else if (at == AuthenticationType.AWS_V4 && (
      authenticationType == AuthenticationType.AWS_V4 ||
        authenticationType == AuthenticationType.AWS_V2_OR_V4)) AuthenticationType.AWS_V4
    else if (authenticationType != AuthenticationType.NONE)
      throw new S3Exception(S3ErrorCode.ACCESS_DENIED)
  }

  def dateSkew(rc: RoutingContext, authenticationType: AuthenticationType) = {
    if (dateHeaders(rc)._2)
      authenticationType match {
        case AWS_V2 => headerDate(rc.request().getHeader(AwsHttpHeaders.DATE_V2.value)) /= 1000
        case AWS_V4 | AWS_V2_OR_V4 => DateTime.parseIso8601(rc.request().getHeader(AwsHttpHeaders.DATE_V2.value))
      }

    else if (dateHeaders(rc)._3)
      DateTime.parseIso8601(rc.request().getHeader(AwsHttpHeaders.DATE_V4.value))

    else if (dateHeaders(rc)._1)
      headerDate(rc.request().getHeader(HttpHeaders.DATE)) /= 1000
  }

  def payload(rc: RoutingContext, authHeader: S3AuthorizationHeader, is: InputStream, v4MaxNonChunkedRequestSize: Long): (Array[Byte], InputStream) = {
    val contentSha256 = rc.request().getHeader(AwsHttpHeaders.CONTENT_SHA256.value)

    if (rc.request().getParam(AwsHttpParameters.ALGORITHM.value) != null)
      (Array.empty[Byte], is)

    else if ("STREAMING-AWS4-HMAC-SHA256-PAYLOAD".equals(contentSha256))
      (Array.empty[Byte], new ChunkedInputStream(is))

    else if ("UNSIGNED-PAYLOAD".equals(contentSha256))
      (Array.empty[Byte], is)

    else {
      val payload = ByteStreams.toByteArray(ByteStreams.limit(is, v4MaxNonChunkedRequestSize + 1));
      if (payload.length == v4MaxNonChunkedRequestSize + 1)
        throw new S3Exception(S3ErrorCode.MAX_MESSAGE_LENGTH_EXCEEDED)

      val md = MessageDigest.getInstance(authHeader.hashAlgorithm)
      val hash = md.digest(payload)
      if (!contentSha256.equals(BaseEncoding.base16.lowerCase.encode(hash)))
        throw new S3Exception(S3ErrorCode.X_AMZ_CONTENT_SHA256_MISMATCH)
      (payload, new ByteArrayInputStream(payload))
    }
  }

  def headerAuthorization(rc: RoutingContext, anonymousIdentity: Boolean) =
    try {
      if (!anonymousIdentity) {
        val algorithm = rc.request().getParam(AwsHttpParameters.ALGORITHM.value)
        if (algorithm == null) {
          val identity = rc.request().getParam(AwsHttpParameters.ACCESS_KEY_ID.value)
          val signature = rc.request().getParam(AwsHttpParameters.SIGNATURE.value)
          if (identity == null || signature == null) throw new S3Exception(S3ErrorCode.ACCESS_DENIED)
          (S3AuthorizationHeader("AWS " + identity + ":" + signature), true)

        } else if (algorithm.equals("AWS4-HMAC-SHA256")) {
          val credential = rc.request().getParam(AwsHttpParameters.CREDENTIAL.value)
          val signedHeaders = rc.request().getParam(AwsHttpParameters.SIGNED_HEADERS.value)
          val signature = rc.request().getParam(AwsHttpParameters.SIGNATURE.value)
          if (credential == null || signedHeaders == null || signature == null) throw new S3Exception(S3ErrorCode.ACCESS_DENIED)
          (S3AuthorizationHeader(s"AWS4-HMAC-SHA256 Credential=$credential, requestSignedHeaders=$signedHeaders, Signature=$signature"), true)

        } else
          throw new S3Exception(S3ErrorCode.INVALID_ARGUMENT)
      } else
        (S3AuthorizationHeader(rc.request().getHeader(HttpHeaders.AUTHORIZATION)), false)
    }
    catch {
      case iae: Exception =>
        throw new S3Exception(S3ErrorCode.INVALID_ARGUMENT, iae)
    }

  def xmlResponse(outputFactory: XMLOutputFactory, fn: XMLStreamWriter => Unit) = {
    val stringWriter = new StringWriter()
    val writer = outputFactory.createXMLStreamWriter(stringWriter)
    fn(writer)
    Response.Content(stringWriter.toString, contentType = Some(ContentType.XML))
  }
}
