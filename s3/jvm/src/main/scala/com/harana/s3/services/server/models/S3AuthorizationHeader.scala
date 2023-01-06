package com.harana.s3.services.s3_server.models

import com.google.common.base.Splitter
import com.google.common.collect.ImmutableMap

case class S3AuthorizationHeader(authenticationType: AuthenticationType,
                                 hmacAlgorithm: String = null,
                                 hashAlgorithm: String = null,
                                 region: String = null,
                                 date: String = null,
                                 service: String = null,
                                 identity: String,
                                 signature: String)

object S3AuthorizationHeader {

  val DIGEST_MAP = ImmutableMap.builder[String, String].put("SHA256", "SHA-256").put("SHA1", "SHA-1").put("MD5", "MD5").build
  val SIGNATURE_FIELD = "Signature="
  val CREDENTIAL_FIELD = "Credential="

  def apply(header: String) = {

    if (header.startsWith("AWS ")) {
      val fields = Splitter.on(' ').splitToList(header)
      if (fields.size ne 2) throw new IllegalArgumentException("Invalid header")
      val identityTuple = Splitter.on(':').splitToList(fields.get(1))
      if (identityTuple.size ne 2) throw new IllegalArgumentException("Invalid header")

      new S3AuthorizationHeader(
        authenticationType = AuthenticationType.AWS_V2,
        identity = identityTuple.get(0),
        signature = identityTuple.get(1)
      )
    }
    else if (header.startsWith("AWS4-HMAC")) {
      val credentialIndex = header.indexOf(CREDENTIAL_FIELD)
      if (credentialIndex < 0) throw new IllegalArgumentException("Invalid header")
      val credentialEnd = header.indexOf(',', credentialIndex)
      if (credentialEnd < 0) throw new IllegalArgumentException("Invalid header")
      val credential = header.substring(credentialIndex + CREDENTIAL_FIELD.length, credentialEnd)
      val fields = Splitter.on('/').splitToList(credential)
      if (fields.size ne 5) throw new IllegalArgumentException("Invalid Credential: " + credential)
      val awsSignatureVersion = header.substring(0, header.indexOf(' '))

      new S3AuthorizationHeader(
        authenticationType = AuthenticationType.AWS_V4,
        hmacAlgorithm = "Hmac" + Splitter.on('-').splitToList(awsSignatureVersion).get(2),
        hashAlgorithm = DIGEST_MAP.get(Splitter.on('-').splitToList(awsSignatureVersion).get(2)),
        region = fields.get(2),
        date = fields.get(1),
        service = fields.get(3),
        identity = fields.get(0),
        signature = extractSignature(header)
      )
    }
    else throw new IllegalArgumentException("Invalid header")
  }

  private def extractSignature(header: String) = {
    var signatureIndex = header.indexOf(SIGNATURE_FIELD)
    if (signatureIndex < 0) throw new IllegalArgumentException("Invalid signature")
    signatureIndex += SIGNATURE_FIELD.length
    val signatureEnd = header.indexOf(',', signatureIndex)
    if (signatureEnd < 0) header.substring(signatureIndex)
    else header.substring(signatureIndex, signatureEnd)
  }
}