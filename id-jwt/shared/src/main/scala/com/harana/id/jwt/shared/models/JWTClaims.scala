package com.harana.id.jwt.shared.models

import java.time.Instant

trait JWTClaims {
  val subject: String
  val audiences: List[String]
  val expires: Instant
  val issued: Instant
  val notBefore: Instant

  type JWTClaimsType <: JWTClaims
  def renew(expires: Instant): JWTClaimsType
}
