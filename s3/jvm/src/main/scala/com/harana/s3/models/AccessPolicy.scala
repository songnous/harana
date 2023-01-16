package com.harana.s3.models

import io.circe.generic.JsonCodec
import io.circe.generic.semiauto._
import io.circe._

@JsonCodec
case class AccessPolicy(credentials: Credentials,
                        createBuckets: Boolean,
                        deleteBuckets: Boolean,
                        listObjects: Boolean,
                        getObject: Boolean,
                        putObject: Boolean,
                        copyObject: Boolean,
                        deleteObject: Boolean)

sealed trait Credentials
object Credentials {

  implicit val decoder: Decoder[Credentials] = deriveDecoder
  implicit val encoder: Encoder[Credentials] = deriveEncoder

  case class Basic(accessKeyId: String, secretAccessKey: String) extends Credentials
  case class URL(url: String) extends Credentials
}