package com.harana.s3.services.router

import com.harana.s3.models.Route
import com.harana.s3.services.server.models.S3Exception
import io.vertx.core.buffer.Buffer
import io.vertx.core.streams.{Pump, ReadStream}
import io.vertx.ext.reactivestreams.{ReactiveReadStream, ReactiveWriteStream}
import software.amazon.awssdk.services.s3.model._
import zio.macros.accessible
import zio.{Has, IO, UIO}

import java.time.Instant

@accessible
object Router {
  type Router = Has[Router.Service]

  trait Service {

    def createBucket(bucket: String): IO[S3Exception, Unit]

    def deleteBucket(bucket: String): IO[S3Exception, Unit]

    def listBuckets(): UIO[List[Bucket]]

    def bucketExists(bucket: String): IO[S3Exception, Boolean]

    def getBucketPolicy(bucket: String): IO[S3Exception, String]

    def getBucketAcl(bucket: String): IO[S3Exception, GetBucketAclResponse]

    def putBucketAcl(bucket: String, acl: BucketCannedACL): IO[S3Exception, Unit]

    def listObjects(bucket: String, prefix: Option[String] = None): IO[S3Exception, List[S3Object]]

    def deleteObject(bucket: String, key: String): IO[S3Exception, Unit]

    def deleteObjects(bucket: String, keys: List[String]): IO[S3Exception, Unit]

    def getObject(bucket: String,
                  key: String,
                  ifMatch: Option[String] = None,
                  ifNoneMatch: Option[String] = None,
                  ifModifiedSince: Option[Instant] = None,
                  ifUnmodifiedSince: Option[Instant] = None,
                  range: Option[String] = None): IO[S3Exception, ReadStream[Buffer]]

    def getObjectAttributes(bucket: String, key: String): IO[S3Exception, GetObjectAttributesResponse]

    def putObject(bucket: String,
                  key: String,
                  stream: ReactiveWriteStream[Buffer],
                  streamPump: Pump,
                  contentLength: Long,
                  acl: ObjectCannedACL,
                  contentMD5: Option[String] = None,
                  storageClass: Option[String] = None,
                  tags: Map[String, String] = Map()): IO[S3Exception, String]

    def copyObject(sourceBucket: String, sourceKey: String, destinationBucket: String, destinationKey: String): IO[S3Exception, CopyObjectResult]

    def getObjectAcl(bucket: String, key: String): IO[S3Exception, GetObjectAclResponse]

    def putObjectAcl(bucket: String, key: String, acl: ObjectCannedACL): IO[S3Exception, Unit]

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
                       copySourceRange: Option[String]): IO[S3Exception, CopyPartResult]

    def uploadPart(bucket: String,
                   key: String,
                   uploadId: String,
                   partNumber: Int,
                   writeStream: ReactiveWriteStream[Buffer]): IO[S3Exception, String]

    def listParts(bucket: String, key: String, uploadId: String): IO[S3Exception, List[Part]]

    def listMultipartUploads(bucket: String): IO[S3Exception, List[MultipartUpload]]

    def createMultipartUpload(bucket: String, key: String, cannedACL: ObjectCannedACL): IO[S3Exception, String]

    def abortMultipartUpload(bucket: String, key: String, uploadId: String): IO[S3Exception, Unit]

    def completeMultipartUpload(bucket: String, key: String, uploadId: String): IO[S3Exception, String]

    def updateRoutes(routes: List[Route]): UIO[Unit]
  }
}
