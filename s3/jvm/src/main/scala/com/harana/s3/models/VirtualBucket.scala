package com.harana.s3.models

import enumeratum.{CirceEnum, Enum, EnumEntry}
import io.circe.generic.JsonCodec
import software.amazon.awssdk.regions.Region

@JsonCodec
case class Route(path: String,
                 pathMatch: PathMatch,
                 destination: Destination,
                 immutable: Boolean,
                 durable: Boolean)

sealed trait PathMatch extends EnumEntry
object PathMatch extends Enum[PathMatch] with CirceEnum[PathMatch] {
  case object Exact extends PathMatch
  case object StartsWith extends PathMatch
  case object EndsWith extends PathMatch
  case object Contains extends PathMatch
  case object Regex extends PathMatch
  val values = findValues
}

sealed trait Destination
object Destination {
  case object Local extends Destination

  case class S3(region: Region,
                credentials: S3Credentials) extends Destination

  case class S3Compatible(endpoint: String,
                          credentials: S3Credentials) extends Destination

  case class Log(prefix: String) extends Destination
}

sealed trait S3Credentials
object S3Credentials {
  case object DefaultChain extends S3Credentials
  case class Basic(accessKeyId: String, secretAccessKey: String) extends S3Credentials
  case class CredentialsProvider(className: String) extends S3Credentials
  case class Profile(name: String) extends S3Credentials
  case class URL(url: String) extends S3Credentials
}