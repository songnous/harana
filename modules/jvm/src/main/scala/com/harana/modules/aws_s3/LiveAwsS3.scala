package com.harana.modules.aws_s3

import com.harana.modules.aws_s3.AwsS3.Service
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import io.vertx.core.buffer.Buffer
import io.vertx.ext.reactivestreams.{ReactiveReadStream, ReactiveWriteStream}
import org.reactivestreams.{Subscriber, Subscription}
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.core.async.{AsyncRequestBody, AsyncResponseTransformer, SdkPublisher}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model._
import zio.blocking.Blocking
import zio.{Task, ZLayer}

import java.nio.ByteBuffer
import java.util.Optional
import scala.jdk.CollectionConverters._

object LiveAwsS3 {

  val layer = ZLayer.fromServices { (blocking: Blocking.Service,
                                     config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    def newClient(region: Region, credentials: AwsCredentialsProvider, targetThroughput: Option[Long] = None) =
      Task(
        S3AsyncClient.crtBuilder()
          .credentialsProvider(credentials)
          .region(region)
          .targetThroughputInGbps(targetThroughput.getOrElse(40.0))
          .minimumPartSizeInBytes(8 * 1024 * 1024)
          .build())

    def createBucket(client: S3AsyncClient, bucket: String) =
      Task.fromCompletableFuture(client.createBucket(CreateBucketRequest.builder().bucket(bucket).build())).unit

    def deleteBucket(client: S3AsyncClient, bucket: String) =
      Task.fromCompletableFuture(client.deleteBucket(DeleteBucketRequest.builder().bucket(bucket).build())).unit

    def listBuckets(client: S3AsyncClient) =
      Task.fromCompletableFuture(client.listBuckets(ListBucketsRequest.builder().build())).map(_.buckets().asScala.toList)

    def bucketExists(client: S3AsyncClient, bucket: String) =
      listBuckets(client).map(b => b.map(_.name()).contains(bucket))

    def getBucketPolicy(client: S3AsyncClient, bucket: String) =
      Task.fromCompletableFuture(client.getBucketPolicy(GetBucketPolicyRequest.builder().bucket(bucket).build())).map(_.policy())

    def getBucketAcl(client: S3AsyncClient, bucket: String): Task[GetBucketAclResponse] =
      Task.fromCompletableFuture(client.getBucketAcl(GetBucketAclRequest.builder().bucket(bucket).build()))

    def putBucketAcl(client: S3AsyncClient, bucket: String, acl: String) =
      Task.fromCompletableFuture(client.putBucketAcl(PutBucketAclRequest.builder().bucket(bucket).acl(acl).build()))

    def listObjects(client: S3AsyncClient, bucket: String, prefix: Option[String] = None) = {
      var builder = ListObjectsV2Request.builder().bucket(bucket)
      builder = if (prefix.isDefined) builder.prefix(prefix.get) else builder
      Task.fromCompletableFuture(client.listObjectsV2(builder.build())).map(_.contents().asScala.toList)
    }

    def deleteObject(client: S3AsyncClient, bucket: String, key: String) =
      Task.fromCompletableFuture(client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build())).unit

    def deleteObjects(client: S3AsyncClient, bucket: String, identifiers: List[ObjectIdentifier]) =
      Task.fromCompletableFuture(
        client.deleteObjects(DeleteObjectsRequest.builder().bucket(bucket)
        .delete(Delete.builder().objects(identifiers.asJava).build()).build())
      ).unit

    def getObject(client: S3AsyncClient, bucket: String, key: String) = {
      val readStream = ReactiveReadStream.readStream[Buffer]

      Task.fromCompletableFuture(client.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build(), new AsyncResponseTransformer[GetObjectResponse, _] {
        override def prepare() = _
        override def onResponse(response: GetObjectResponse) = _
        override def onStream(publisher: SdkPublisher[ByteBuffer]) =
          publisher.subscribe(new Subscriber[ByteBuffer] {
            override def onSubscribe(sub: Subscription) = readStream.onSubscribe(sub)
            override def onNext(t: ByteBuffer) = readStream.onNext(Buffer.buffer(t.array()))
            override def onError(t: Throwable) = readStream.onError(t)
            override def onComplete() = readStream.onComplete()
          })

        override def exceptionOccurred(error: Throwable) = _
      })).as(readStream)
    }

    def putObject(client: S3AsyncClient, bucket: String, key: String, writeStream: ReactiveWriteStream[Buffer]) =
      Task.fromCompletableFuture(client.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(), publisher(writeStream))).unit

    def copyObject(client: S3AsyncClient, sourceBucket: String, sourceKey: String, destinationBucket: String, destinationKey: String) =
      Task.fromCompletableFuture(client.copyObject(CopyObjectRequest.builder()
        .sourceBucket(sourceBucket).sourceKey(sourceKey)
        .destinationBucket(destinationBucket).destinationKey(destinationKey)
        .build()
      )).unit

    def getObjectAcl(client: S3AsyncClient, bucket: String, key: String) =
      Task.fromCompletableFuture(client.getObjectAcl(GetObjectAclRequest.builder().bucket(bucket).key(key).build()))

    def putObjectAcl(client: S3AsyncClient, bucket: String, key: String, acl: String) =
      Task.fromCompletableFuture(client.putObjectAcl(PutObjectAclRequest.builder().bucket(bucket).acl(acl).build())).unit

    def uploadPartCopy(client: S3AsyncClient, sourceBucket: String, sourceKey: String, destinationBucket: String, destinationKey: String, uploadId: String) =
      Task.fromCompletableFuture(client.uploadPartCopy(UploadPartCopyRequest.builder()
        .sourceBucket(sourceBucket).sourceKey(sourceKey)
        .destinationBucket(destinationBucket).destinationKey(destinationKey)
        .uploadId(uploadId).build()
      )).unit

    def uploadPart(client: S3AsyncClient, bucket: String, key: String, uploadId: String, writeStream: ReactiveWriteStream[Buffer]) =
      Task.fromCompletableFuture(
        client.uploadPart(UploadPartRequest.builder().bucket(bucket).key(key).uploadId(uploadId).build(), AsyncRequestBody.fromPublisher(publisher(writeStream)))
      ).unit

    def listParts(client: S3AsyncClient, bucket: String, key: String, uploadId: String) =
      Task.fromCompletableFuture(client.listParts(ListPartsRequest.builder().bucket(bucket).key(key).uploadId(uploadId).build())).map(_.parts().asScala.toList)

    def listMultipartUploads(client: S3AsyncClient, bucket: String) =
      Task.fromCompletableFuture(client.listMultipartUploads(ListMultipartUploadsRequest.builder().bucket(bucket).build())).map(_.uploads().asScala.toList)

    def createMultipartUpload(client: S3AsyncClient, bucket: String, key: String) =
      Task.fromCompletableFuture(client.createMultipartUpload(CreateMultipartUploadRequest.builder().bucket(bucket).key(key).build())).map(_.uploadId())

    def abortMultipartUpload(client: S3AsyncClient, bucket: String, key: String, uploadId: String) =
      Task.fromCompletableFuture(client.abortMultipartUpload(AbortMultipartUploadRequest.builder().bucket(bucket).key(key).uploadId(uploadId).build())).unit

    def completeMultipartUpload(client: S3AsyncClient, bucket: String, key: String, uploadId: String) =
      Task.fromCompletableFuture(client.completeMultipartUpload(CompleteMultipartUploadRequest.builder().bucket(bucket).key(key).uploadId(uploadId).build())).map(_.eTag())


    private def publisher(writeStream: ReactiveWriteStream[Buffer]) =
      new AsyncRequestBody() {
        override def contentLength: Optional[Long] = Optional.empty
        override def subscribe(s: Subscriber[_ >: ByteBuffer]) =
          writeStream.subscribe(new Subscriber[Buffer] {
            override def onSubscribe(sub: Subscription) = s.onSubscribe(sub)
            override def onNext(t: Buffer) = s.onNext(t.getByteBuf.nioBuffer())
            override def onError(t: Throwable) = s.onError(t)
            override def onComplete() = s.onComplete()
          })
      }
  }}
}