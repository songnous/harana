package com.harana.s3.services.s3_server.models

import com.google.common.base.CaseFormat
import enumeratum._

sealed trait AuthenticationType extends EnumEntry

case object AuthenticationType extends Enum[AuthenticationType] with CirceEnum[AuthenticationType] {
  case object AWS_V2 extends AuthenticationType
  case object AWS_V4 extends AuthenticationType
  case object AWS_V2_OR_V4 extends AuthenticationType
  case object NONE extends AuthenticationType
  val values = findValues

  def fromString(string: String) =
    AuthenticationType.withName(CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, string))
}