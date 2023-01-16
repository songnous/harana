package com.harana.s3.models

import software.amazon.awssdk.auth.credentials._
import software.amazon.awssdk.regions.Region
import io.circe.generic.semiauto._
import io.circe._

sealed trait Destination
object Destination {
  implicit val decoder: Decoder[Destination] = deriveDecoder
  implicit val encoder: Encoder[Destination] = deriveEncoder

  case object Local extends Destination

  case class S3(credentials: S3Credentials,
                region: Option[String],
                endpoint: Option[String]) extends Destination
}

sealed trait S3Credentials
object S3Credentials {
  implicit val decoder: Decoder[S3Credentials] = deriveDecoder
  implicit val encoder: Encoder[S3Credentials] = deriveEncoder

  case object Anonymous extends S3Credentials
  case object Default extends S3Credentials
  case class CredentialsProvider(className: String) extends S3Credentials
  case object EnvironmentVariables extends S3Credentials
  case class Profile(name: String) extends S3Credentials
  case class Static(accessKeyId: String, secretAccessKey: String) extends S3Credentials

  def toAWSCredentialsProvider(credentials: S3Credentials) =
    credentials match {
      case Anonymous => AnonymousCredentialsProvider.create()
      case Default => DefaultCredentialsProvider.builder().build()
      case CredentialsProvider(c) => Class.forName(c).getDeclaredConstructor().newInstance().asInstanceOf[AwsCredentialsProvider]
      case EnvironmentVariables => EnvironmentVariableCredentialsProvider.create()
      case Profile(n) => ProfileCredentialsProvider.create(n)
      case Static(a,s) => StaticCredentialsProvider.create(AwsBasicCredentials.create(a, s))
  }
}
