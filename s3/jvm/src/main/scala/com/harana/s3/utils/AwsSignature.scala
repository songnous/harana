package com.harana.s3.utils

import com.google.common.base.{Joiner, Splitter, Strings}
import com.google.common.io.BaseEncoding
import com.google.common.net.{HttpHeaders, PercentEscaper}
import com.harana.s3.services.server.models.{AwsHttpHeaders, AwsHttpParameters, S3AuthorizationHeader}
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.regex.Pattern
import java.util.{Base64, Collections}
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import scala.jdk.CollectionConverters._

object AwsSignature {

  val AWS_URL_PARAMETER_ESCAPER = new PercentEscaper("-_.~", false)
  val SIGNED_SUBRESOURCES = Set(
    "acl", "delete", "lifecycle", "location", "logging", "notification", "partNumber", "policy", "requestPayment", "response-cache-control", "response-content-disposition", "response-content-encoding",
    "response-content-language", "response-content-type", "response-expires", "torrent", "uploadId", "uploads", "versionId", "versioning", "versions", "website")
  val REPEATING_WHITESPACE = Pattern.compile("\\s+")

  def createAuthorizationSignature(rc: RoutingContext, credential: String, queryAuth: Boolean, bothDateHeader: Boolean) = {
    val headers = rc.request().headers().names.asScala.toList

    val canonicalizedHeaders = headers.filterNot(h => h.startsWith("x-amz-") || bothDateHeader && h.equalsIgnoreCase(AwsHttpHeaders.DATE_V4.value)).map { h =>
      val values = rc.request().headers().getAll(h).asScala.toList
      if (values.isEmpty) h -> List("") else h -> values
    }.toMap

    val str = new StringBuilder()
    str.append(rc.request().method().name()).append("\n")
    str.append(Strings.nullToEmpty(rc.request().getHeader(HttpHeaders.CONTENT_MD5))).append("\n")
    str.append(Strings.nullToEmpty(rc.request().getHeader(HttpHeaders.CONTENT_TYPE))).append("\n")

    if (queryAuth) str.append(Strings.nullToEmpty(rc.request().getParam(AwsHttpParameters.EXPIRES_V2.value)))
    else if (!bothDateHeader) if (canonicalizedHeaders.contains(AwsHttpHeaders.DATE_V2.value)) str.append("")
    else str.append(rc.request().getHeader(HttpHeaders.DATE))
    else if (!canonicalizedHeaders.contains(AwsHttpHeaders.DATE_V2.value)) str.append(rc.request().getHeader(AwsHttpHeaders.DATE_V2.value))
    else {}

    str.append("\n")
    for (key <- canonicalizedHeaders.keys) {
      str.append(key).append(':').append(key).append('\n')
    }
    str.append(rc.request().uri())
    var separator = '?'
    rc.request().params().names().asScala.toList.sorted.foreach { subResource =>
      if (SIGNED_SUBRESOURCES.contains(subResource)) {
        str.append(separator).append(subResource)
        val value = rc.request().getParam(subResource)
        if (!("" == value)) str.append('=').append(value)
        separator = '&'
      }
    }

    val mac = Mac.getInstance("HmacSHA1")
    mac.init(new SecretKeySpec(credential.getBytes(StandardCharsets.UTF_8), "HmacSHA1"))
    Base64.getEncoder.encodeToString(mac.doFinal(str.toString.getBytes(StandardCharsets.UTF_8)))
  }

  def signMessage(data: Array[Byte], key: Array[Byte], algorithm: String) = {
    val mac = Mac.getInstance(algorithm)
    mac.init(new SecretKeySpec(key, algorithm))
    mac.doFinal(data)
  }

  def getMessageDigest(payload: Array[Byte], algorithm: String) = {
    val md = MessageDigest.getInstance(algorithm)
    val hash = md.digest(payload)
    BaseEncoding.base16.lowerCase.encode(hash)
  }

  def extractSignedHeaders(authorization: String): List[String] = {
    val index = authorization.indexOf("SignedHeaders=")
    if (index < 0) return List.empty
    val endSigned = authorization.indexOf(',', index)
    if (endSigned < 0) return List.empty
    val startHeaders = authorization.indexOf('=', index)
    Splitter.on(';').splitToList(authorization.substring(startHeaders + 1, endSigned)).asScala.toList
  }

  def buildCanonicalHeaders(rc: RoutingContext, signedHeaders: List[String]) =
    signedHeaders.map(_.toLowerCase).sorted.map { h =>
      val values = rc.request().headers().getAll(h).asScala.map(_.trim).map(v =>
        if (v.startsWith("\"")) REPEATING_WHITESPACE.matcher(v).replaceAll(" ") else v
      ).mkString(",")
      s"$h:$values"
    }.mkString("\n")


  def buildCanonicalQueryString(rc: RoutingContext) = {
    val params = rc.request().params().names().asScala.toList
    Joiner.on("&").join(
      params.filterNot(_.equals(AwsHttpParameters.SIGNATURE.value)).sorted.map { p =>
        AWS_URL_PARAMETER_ESCAPER.escape(p) + "=" + AWS_URL_PARAMETER_ESCAPER.escape(rc.request().getParam(p))
      }.asJava
    )
  }
  
  def createCanonicalRequest(rc: RoutingContext, uri: String, payload: Array[Byte], hashAlgorithm: String) = {
    val authorizationHeader = rc.request().getHeader("Authorization")
    val hashHeader = rc.request().getHeader(AwsHttpHeaders.CONTENT_SHA256.value)

    val xAmzContentSha256 =
      if (hashHeader == null) rc.request().getParam(AwsHttpParameters.SIGNED_HEADERS.value)
      else hashHeader

    val digest =
      if (authorizationHeader == null) "UNSIGNED-PAYLOAD"
      else if ("STREAMING-AWS4-HMAC-SHA256-PAYLOAD" == xAmzContentSha256) "STREAMING-AWS4-HMAC-SHA256-PAYLOAD"
      else if ("UNSIGNED-PAYLOAD" == xAmzContentSha256) "UNSIGNED-PAYLOAD"
      else getMessageDigest(payload, hashAlgorithm)

    val signedHeaders =
      if (authorizationHeader != null) extractSignedHeaders(authorizationHeader)
      else Splitter.on(';').splitToList(rc.request().getParam(AwsHttpParameters.SIGNED_HEADERS.value)).asScala.toList

    val method =
      if (rc.request().method() == HttpMethod.OPTIONS) {
        val corsMethod = rc.request().getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD)
        if (corsMethod != null) HttpMethod.valueOf(corsMethod)
      } else rc.request().method()

    val canonicalRequest = Joiner.on("\n").join(method, uri, buildCanonicalQueryString(rc), buildCanonicalHeaders(rc, signedHeaders) + "\n", Joiner.on(';').join(signedHeaders.asJava), digest)
    getMessageDigest(canonicalRequest.getBytes(StandardCharsets.UTF_8), hashAlgorithm)
  }

  def createAuthorizationSignatureV4(rc: RoutingContext, authHeader: S3AuthorizationHeader, payload: Array[Byte], uri: String, credential: String) = {
    val canonicalRequest = createCanonicalRequest(rc, uri, payload, authHeader.hashAlgorithm)
    val algorithm = authHeader.hashAlgorithm
    val dateKey = signMessage(authHeader.date.getBytes(StandardCharsets.UTF_8), ("AWS4" + credential).getBytes(StandardCharsets.UTF_8), algorithm)
    val dateRegionKey = signMessage(authHeader.region.getBytes(StandardCharsets.UTF_8), dateKey, algorithm)
    val dateRegionServiceKey = signMessage(authHeader.service.getBytes(StandardCharsets.UTF_8), dateRegionKey, algorithm)
    val signingKey = signMessage("aws4_request".getBytes(StandardCharsets.UTF_8), dateRegionServiceKey, algorithm)
    var date = rc.request().getHeader(AwsHttpHeaders.DATE_V4.value)
    if (date == null) date = rc.request().getParam(AwsHttpParameters.DATE.value)
    val signatureString = "AWS4-HMAC-SHA256\n" + date + "\n" + authHeader.date + "/" + authHeader.region + "/s3/aws4_request\n" + canonicalRequest
    val signature = signMessage(signatureString.getBytes(StandardCharsets.UTF_8), signingKey, algorithm)
    BaseEncoding.base16.lowerCase.encode(signature)
  }
}