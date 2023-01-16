package com.harana.s3.services.router

import io.vertx.core.buffer.Buffer
import io.vertx.ext.reactivestreams.{ReactiveReadStream, ReactiveWriteStream}
import software.amazon.awssdk.services.s3.model._
import zio.macros.accessible
import zio.{Has, Task}

import java.time.Instant

@accessible
object Router {
  type Router = Has[Router.Service]

  trait Service {

    def createBucket(bucket: String): Task[Unit]

    def deleteBucket(bucket: String): Task[Unit]

    def listBuckets(): Task[List[Bucket]]

    def bucketExists(bucket: String): Task[Boolean]

    def getBucketPolicy(bucket: String): Task[String]

    def getBucketAcl(bucket: String): Task[GetBucketAclResponse]

    def putBucketAcl(bucket: String, acl: BucketCannedACL): Task[Unit]

    def listObjects(bucket: String, prefix: Option[String] = None): Task[List[S3Object]]

    def deleteObject(bucket: String, key: String): Task[Unit]

    def deleteObjects(bucket: String, keys: List[String]): Task[Unit]

    def getObject(bucket: String,
                  key: String,
                  ifMatch: Option[String] = None,
                  ifNoneMatch: Option[String] = None,
                  ifModifiedSince: Option[Instant] = None,
                  ifUnmodifiedSince: Option[Instant] = None,
                  range: Option[String] = None): Task[ReactiveReadStream[Buffer]]

    def getObjectAttributes(bucket: String, key: String): Task[GetObjectAttributesResponse]

    def putObject(bucket: String,
                  key: String,
                  writeStream: ReactiveWriteStream[Buffer],
                  acl: ObjectCannedACL,
                  contentLength: Option[Long] = None,
                  contentMD5: Option[String] = None,
                  storageClass: Option[String] = None,
                  tags: Map[String, String] = Map()): Task[String]

    def copyObject(sourceBucket: String, sourceKey: String, destinationBucket: String, destinationKey: String): Task[CopyObjectResult]

    def getObjectAcl(bucket: String, key: String): Task[GetObjectAclResponse]

    def putObjectAcl(bucket: String, key: String, acl: ObjectCannedACL): Task[Unit]

    def uploadPartCopy(sourceBucket: String,
                       sourceKey: String,
                       destinationBucket: String,
                       destinationKey: String,
                       uploadId: String,
                       partNumber: Int,
                       copySourceIfMatch: Option[String],
                       copySourceIfNoneMatch: Option[String],
                       copySourceIfModifiedSince: Option[Instant],
                       copySourceIfUnmodifiedSince: Option[Instant],
                       copySourceRange: Option[String]): Task[CopyPartResult]

    def uploadPart(bucket: String, key: String, uploadId: String, partNumber: Int, writeStream: ReactiveWriteStream[Buffer]): Task[String]

    def listParts(bucket: String, key: String, uploadId: String): Task[List[Part]]

    def listMultipartUploads(bucket: String): Task[List[MultipartUpload]]

    def createMultipartUpload(bucket: String, key: String, cannedACL: ObjectCannedACL): Task[String]

    def abortMultipartUpload(bucket: String, key: String, uploadId: String): Task[Unit]

    def completeMultipartUpload(bucket: String, key: String, uploadId: String): Task[String]

  }
}
