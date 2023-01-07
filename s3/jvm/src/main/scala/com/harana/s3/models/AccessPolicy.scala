package com.harana.s3.models

import io.circe.generic.JsonCodec

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
  case class Basic(accessKeyId: String, secretAccessKey: String) extends Credentials
  case class URL(url: String) extends Credentials
}