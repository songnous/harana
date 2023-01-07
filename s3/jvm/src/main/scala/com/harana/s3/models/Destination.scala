package com.harana.s3.models

import software.amazon.awssdk.regions.Region

sealed trait Destination
object Destination {
  case object Local extends Destination

  case class S3(region: Region,
                credentials: S3Credentials) extends Destination

  case class S3Compatible(endpoint: String,
                          credentials: S3Credentials) extends Destination
}

sealed trait S3Credentials
object S3Credentials {
  case object DefaultChain extends S3Credentials
  case class Basic(accessKeyId: String, secretAccessKey: String) extends S3Credentials
  case class CredentialsProvider(className: String) extends S3Credentials
  case class Profile(name: String) extends S3Credentials
  case class URL(url: String) extends S3Credentials
}
