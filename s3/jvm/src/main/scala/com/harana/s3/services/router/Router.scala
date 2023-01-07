package com.harana.s3.services.router

import io.vertx.core.buffer.Buffer
import io.vertx.ext.reactivestreams.{ReactiveReadStream, ReactiveWriteStream}
import software.amazon.awssdk.services.s3.model._
import zio.macros.accessible
import zio.{Has, Task}

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

    def putBucketAcl(bucket: String, acl: String): Task[Unit]

    def listObjects(bucket: String, prefix: Option[String] = None): Task[List[S3Object]]

    def deleteObject(bucket: String, key: String): Task[Unit]

    def deleteObjects(bucket: String, keys: List[String]): Task[Unit]

    def getObject(bucket: String, key: String): Task[ReactiveReadStream[Buffer]]

    def putObject(bucket: String, key: String, writeStream: ReactiveWriteStream[Buffer]): Task[Unit]

    def copyObject(sourceBucket: String, sourceKey: String, destinationBucket: String, destinationKey: String): Task[Unit]

    def getObjectAcl(bucket: String, key: String): Task[GetObjectAclResponse]

    def putObjectAcl(bucket: String, key: String, acl: String): Task[Unit]

    def uploadPartCopy(sourceBucket: String, sourceKey: String, destinationBucket: String, destinationKey: String, uploadId: String): Task[Unit]

    def uploadPart(bucket: String, key: String, uploadId: String, writeStream: ReactiveWriteStream[Buffer]): Task[Unit]

    def listParts(bucket: String, key: String, uploadId: String): Task[List[Part]]

    def listMultipartUploads(bucket: String): Task[List[MultipartUpload]]

    def createMultipartUpload(bucket: String, key: String): Task[String]

    def abortMultipartUpload(bucket: String, key: String, uploadId: String): Task[Unit]

    def completeMultipartUpload(bucket: String, key: String, uploadId: String): Task[String]

  }
}
