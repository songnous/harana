package com.harana.modules.aws_s3

import awscala.Region
import io.vertx.core.buffer.Buffer
import io.vertx.ext.reactivestreams.{ReactiveReadStream, ReactiveWriteStream}
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model._
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object AwsS3 {
  type AwsS3 = Has[AwsS3.Service]

  trait Service {

    def newClient(region: Region, credentials: AwsCredentialsProvider): Task[S3AsyncClient]

    def createBucket(client: S3AsyncClient, bucket: String): Task[Unit]

    def deleteBucket(client: S3AsyncClient, bucket: String): Task[Unit]

    def listBuckets(client: S3AsyncClient): Task[List[Bucket]]

    def bucketExists(client: S3AsyncClient, bucket: String): Task[Boolean]

    def getBucketPolicy(client: S3AsyncClient, bucket: String): Task[String]

    def getBucketAcl(client: S3AsyncClient, bucket: String): Task[GetBucketAclResponse]

    def putBucketAcl(client: S3AsyncClient, bucket: String, acl: String): Task[Unit]

    def listObjects(client: S3AsyncClient, bucket: String, prefix: Option[String] = None): Task[List[S3Object]]

    def deleteObject(client: S3AsyncClient, bucket: String, key: String): Task[Unit]

    def deleteObjects(client: S3AsyncClient, bucket: String, identifiers: List[ObjectIdentifier]): Task[Unit]

    def getObject(client: S3AsyncClient, bucket: String, key: String): Task[ReactiveReadStream[Buffer]]

    def putObject(client: S3AsyncClient, bucket: String, key: String, writeStream: ReactiveWriteStream[Buffer]): Task[Unit]

    def copyObject(client: S3AsyncClient, sourceBucket: String, sourceKey: String, destinationBucket: String, destinationKey: String): Task[Unit]

    def getObjectAcl(client: S3AsyncClient, bucket: String, key: String): Task[GetObjectAclResponse]

    def putObjectAcl(client: S3AsyncClient, bucket: String, key: String, acl: String): Task[Unit]

    def uploadPartCopy(client: S3AsyncClient, sourceBucket: String, sourceKey: String, destinationBucket: String, destinationKey: String, uploadId: String): Task[Unit]

    def uploadPart(client: S3AsyncClient, bucket: String, key: String, uploadId: String, writeStream: ReactiveWriteStream[Buffer]): Task[Unit]

    def listParts(client: S3AsyncClient, bucket: String, key: String, uploadId: String): Task[List[Part]]

    def listMultipartUploads(client: S3AsyncClient, bucket: String): Task[List[MultipartUpload]]

    def createMultipartUpload(client: S3AsyncClient, bucket: String, key: String): Task[String]

    def abortMultipartUpload(client: S3AsyncClient, bucket: String, key: String, uploadId: String): Task[Unit]

    def completeMultipartUpload(client: S3AsyncClient, bucket: String, key: String, uploadId: String): Task[String]

  }
}