package com.harana.s3.utils

import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util
import java.util.Base64
import java.util.Collections
import java.util.regex.Pattern
import javax.annotation.Nullable
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import com.google.common.base.Joiner
import com.google.common.base.Splitter
import com.google.common.base.Strings
import com.google.common.collect.ImmutableSet
import com.google.common.collect.SortedSetMultimap
import com.google.common.collect.TreeMultimap
import com.google.common.io.BaseEncoding
import com.google.common.net.HttpHeaders
import com.google.common.net.PercentEscaper
import com.harana.s3.services.server.models.{AwsHttpHeaders, S3Exception}
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._


object AwsSignature {

  private val logger = LoggerFactory.getLogger(classOf[AwsSignature])
  private val AWS_URL_PARAMETER_ESCAPER = new PercentEscaper("-_.~", false)
  private val SIGNED_SUBRESOURCES = ImmutableSet.of("acl", "delete", "lifecycle", "location", "logging", "notification", "partNumber", "policy", "requestPayment", "response-cache-control", "response-content-disposition", "response-content-encoding", "response-content-language", "response-content-type", "response-expires", "torrent", "uploadId", "uploads", "versionId", "versioning", "versions", "website")
  private val REPEATING_WHITESPACE = Pattern.compile("\\s+")

  /**
   * Create Amazon V2 signature.  Reference:
   * http://docs.aws.amazon.com/general/latest/gr/signature-version-2.html
   */
  def createAuthorizationSignature(request: Nothing, uri: String, credential: String, queryAuth: Boolean, bothDateHeader: Boolean) = {
    // sort Amazon headers
    val canonicalizedHeaders = TreeMultimap.create
    for (headerName <- Collections.list(request.getHeaderNames)) {
      val headerValues = Collections.list(request.getHeaders(headerName))
      headerName = headerName.toLowerCase
      if (!headerName.startsWith("x-amz-") || (bothDateHeader && headerName.equalsIgnoreCase(AwsHttpHeaders.DATE))) continue //todo: continue is not supported
      if (headerValues.isEmpty) canonicalizedHeaders.put(headerName, "")
      import scala.collection.JavaConversions._
      for (headerValue <- headerValues) {
        canonicalizedHeaders.put(headerName, Strings.nullToEmpty(headerValue))
      }
    }
    // Build string to sign
    val builder = new lang.StringBuilder().append(request.getMethod).append('\n').append(Strings.nullToEmpty(request.getHeader(HttpHeaders.CONTENT_MD5))).append('\n').append(Strings.nullToEmpty(request.getHeader(HttpHeaders.CONTENT_TYPE))).append('\n')
    val expires = request.getParameter("Expires")
    if (queryAuth) {
      // If expires is not nil, then it is query string sign
      // If expires is nil, maybe also query string sign
      // So should check other accessid param, presign to judge.
      // not the expires
      builder.append(Strings.nullToEmpty(expires))
    }
    else if (!bothDateHeader) if (canonicalizedHeaders.containsKey(AwsHttpHeaders.DATE)) builder.append("")
    else builder.append(request.getHeader(HttpHeaders.DATE))
    else if (!canonicalizedHeaders.containsKey(AwsHttpHeaders.DATE)) builder.append(request.getHeader(AwsHttpHeaders.DATE))
    else {
    }
    builder.append('\n')
    import scala.collection.JavaConversions._
    for (entry <- canonicalizedHeaders.entries) {
      builder.append(entry.getKey).append(':').append(entry.getValue).append('\n')
    }
    builder.append(uri)
    var separator = '?'
    val subresources = Collections.list(request.getParameterNames)
    Collections.sort(subresources)
    import scala.collection.JavaConversions._
    for (subresource <- subresources) {
      if (SIGNED_SUBRESOURCES.contains(subresource)) {
        builder.append(separator).append(subresource)
        val value = request.getParameter(subresource)
        if (!("" == value)) builder.append('=').append(value)
        separator = '&'
      }
    }
    val stringToSign = builder.toString
    logger.trace("stringToSign: {}", stringToSign)
    // Sign string
    var mac: Mac = null
    try {
      mac = Mac.getInstance("HmacSHA1")
      mac.init(new SecretKeySpec(credential.getBytes(StandardCharsets.UTF_8), "HmacSHA1"))
    } catch {
      case e@(_: InvalidKeyException | _: NoSuchAlgorithmException) =>
        throw new RuntimeException(e)
    }
    Base64.getEncoder.encodeToString(mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8)))
  }

  @throws[InvalidKeyException]
  @throws[NoSuchAlgorithmException]
  private def signMessage(data: Array[Byte], key: Array[Byte], algorithm: String) = {
    val mac = Mac.getInstance(algorithm)
    mac.init(new SecretKeySpec(key, algorithm))
    mac.doFinal(data)
  }

  @throws[NoSuchAlgorithmException]
  private def getMessageDigest(payload: Array[Byte], algorithm: String) = {
    val md = MessageDigest.getInstance(algorithm)
    val hash = md.digest(payload)
    BaseEncoding.base16.lowerCase.encode(hash)
  }

  @Nullable private def extractSignedHeaders(authorization: String): util.List[String] = {
    val index = authorization.indexOf("SignedHeaders=")
    if (index < 0) return null
    val endSigned = authorization.indexOf(',', index)
    if (endSigned < 0) return null
    val startHeaders = authorization.indexOf('=', index)
    Splitter.on(';').splitToList(authorization.substring(startHeaders + 1, endSigned))
  }

  private def buildCanonicalHeaders(request: Nothing, signedHeaders: util.List[String]) = {
    val headers = new util.ArrayList[String](signedHeaders.size)
    for (header <- signedHeaders) {
      headers.add(header.toLowerCase)
    }
    Collections.sort(headers)
    val headersWithValues = new StringBuilder
    var firstHeader = true
    for (header <- headers) {
      if (firstHeader) firstHeader = false
      else headersWithValues.append('\n')
      headersWithValues.append(header)
      headersWithValues.append(':')
      var firstValue = true
      import scala.collection.JavaConversions._
      for (value <- Collections.list(request.getHeaders(header))) {
        if (firstValue) firstValue = false
        else headersWithValues.append(',')
        value = value.trim
        if (!value.startsWith("\"")) value = REPEATING_WHITESPACE.matcher(value).replaceAll(" ")
        headersWithValues.append(value)
      }
    }
    headersWithValues.toString
  }

  @throws[UnsupportedEncodingException]
  private def buildCanonicalQueryString(request: Nothing) = {
    // The parameters are required to be sorted
    val parameters = Collections.list(request.getParameterNames)
    Collections.sort(parameters)
    val queryParameters = new util.ArrayList[String]
    import scala.collection.JavaConversions._
    for (key <- parameters) {
      if (key == "X-Amz-Signature") continue //todo: continue is not supported
      // re-encode keys and values in AWS normalized form
      val value = request.getParameter(key)
      queryParameters.add(AWS_URL_PARAMETER_ESCAPER.escape(key) + "=" + AWS_URL_PARAMETER_ESCAPER.escape(value))
    }
    Joiner.on("&").join(queryParameters)
  }

  @throws[IOException]
  @throws[NoSuchAlgorithmException]
  private def createCanonicalRequest(request: HttpServletRequest, uri: String, payload: Array[Byte], hashAlgorithm: String) = {
    val authorizationHeader = request.getHeader("Authorization")
    var xAmzContentSha256 = request.getHeader(AwsHttpHeaders.CONTENT_SHA256)
    if (xAmzContentSha256 == null) xAmzContentSha256 = request.getParameter("X-Amz-SignedHeaders")
    var digest: String = null
    if (authorizationHeader == null) digest = "UNSIGNED-PAYLOAD"
    else if ("STREAMING-AWS4-HMAC-SHA256-PAYLOAD" == xAmzContentSha256) digest = "STREAMING-AWS4-HMAC-SHA256-PAYLOAD"
    else if ("UNSIGNED-PAYLOAD" == xAmzContentSha256) digest = "UNSIGNED-PAYLOAD"
    else digest = getMessageDigest(payload, hashAlgorithm)
    var signedHeaders: util.List[String] = null
    if (authorizationHeader != null) signedHeaders = extractSignedHeaders(authorizationHeader)
    else signedHeaders = Splitter.on(';').splitToList(request.getParameter("X-Amz-SignedHeaders"))

    var method = request.getMethod
    if ("OPTIONS" == method) {
      val corsMethod = request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD)
      if (corsMethod != null) method = corsMethod
    }

    val canonicalRequest = Joiner.on("\n").join(method, uri, buildCanonicalQueryString(request), buildCanonicalHeaders(request, signedHeaders) + "\n", Joiner.on(';').join(signedHeaders), digest)
    getMessageDigest(canonicalRequest.getBytes(StandardCharsets.UTF_8), hashAlgorithm)
  }

  /**
   * Create v4 signature. Reference:
   * http://docs.aws.amazon.com/general/latest/gr/signature-version-4.html
   */
  @throws[InvalidKeyException]
  @throws[IOException]
  @throws[NoSuchAlgorithmException]
  @throws[S3Exception]
  private[s3proxy] def createAuthorizationSignatureV4(request: Nothing, authHeader: Nothing, payload: Array[Byte], uri: String, credential: String) = {
    val canonicalRequest = createCanonicalRequest(request, uri, payload, authHeader.getHashAlgorithm)
    val algorithm = authHeader.getHmacAlgorithm
    val dateKey = signMessage(authHeader.getDate.getBytes(StandardCharsets.UTF_8), ("AWS4" + credential).getBytes(StandardCharsets.UTF_8), algorithm)
    val dateRegionKey = signMessage(authHeader.getRegion.getBytes(StandardCharsets.UTF_8), dateKey, algorithm)
    val dateRegionServiceKey = signMessage(authHeader.getService.getBytes(StandardCharsets.UTF_8), dateRegionKey, algorithm)
    val signingKey = signMessage("aws4_request".getBytes(StandardCharsets.UTF_8), dateRegionServiceKey, algorithm)
    var date = request.getHeader(AwsHttpHeaders.DATE)
    if (date == null) date = request.getParameter("X-Amz-Date")
    val signatureString = "AWS4-HMAC-SHA256\n" + date + "\n" + authHeader.getDate + "/" + authHeader.getRegion + "/s3/aws4_request\n" + canonicalRequest
    val signature = signMessage(signatureString.getBytes(StandardCharsets.UTF_8), signingKey, algorithm)
    BaseEncoding.base16.lowerCase.encode(signature)
  }
}