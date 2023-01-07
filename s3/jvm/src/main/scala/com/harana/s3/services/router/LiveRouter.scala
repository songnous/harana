package com.harana.s3.services.router

import com.harana.modules.aws_s3.AwsS3
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.jasyncfio.Jasyncfio
import com.harana.s3.models.Destination._
import com.harana.s3.models.{AccessPolicy, PathMatch, Route}
import com.harana.s3.services.server.models.{S3ErrorCode, S3Exception}
import io.vertx.core.buffer.Buffer
import io.vertx.ext.reactivestreams.{ReactiveReadStream, ReactiveWriteStream}
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.{Bucket, ObjectIdentifier, S3Object}
import zio.clock.Clock
import zio.{Task, ZLayer}

import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{Files, Path, Paths}
import java.util.UUID
import java.util.concurrent.atomic.AtomicReference
import scala.jdk.CollectionConverters._

object LiveRouter {
  val layer = ZLayer.fromServices { (clock: Clock.Service,
                                     config: Config.Service,
                                     jasyncfio: Jasyncfio.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     s3: AwsS3.Service) => new Router.Service {

    private val routesRef = new AtomicReference[List[Route]](List.empty)
    private val s3ClientsRef = new AtomicReference[Map[Route, S3AsyncClient]](Map.empty)

    def createBucket(bucket: String) =
      for {
        _           <- logger.debug(s"[CreateBucket] $bucket")
        rootDir     <- config.string("local.root")
        result      <- if (validAccess(bucket)(_.createBuckets))
                        route(bucket).destination match {
                          case Local =>
                            val path = Paths.get(rootDir, bucket)
                            if (Files.exists(path))
                              Task.fail(new S3Exception(S3ErrorCode.BUCKET_ALREADY_EXISTS))
                            else
                              Task(Files.createDirectory(path))

                          case S3(r,c) => s3.createBucket(client, bucket)
                          case S3Compatible(e,c) => s3.createBucket(client, bucket)
                        }
                      else Task.fail(new S3Exception(S3ErrorCode.ACCESS_DENIED))
      } yield result


    def deleteBucket(bucket: String) =
      for {
        _           <- logger.debug(s"[DeleteBucket] $bucket")
        rootDir     <- config.string("local.root")
        result      <- if (validAccess(bucket)(_.deleteBuckets))
                        route(bucket).destination match {
                          case Local =>
                            val path = Paths.get(rootDir, bucket)
                            if (Files.notExists(path))
                              Task.fail(new S3Exception(S3ErrorCode.NO_SUCH_BUCKET))
                            else if (Files.list(path).count() > 0)
                              Task.fail(new S3Exception(S3ErrorCode.BUCKET_NOT_EMPTY))
                            else
                              Task(Files.delete(path))

                          case S3(r,c) => s3.deleteBucket(client, bucket)
                          case S3Compatible(e,c) => s3.deleteBucket(client, bucket)
                        }
                      else Task.fail(new S3Exception(S3ErrorCode.ACCESS_DENIED))
      } yield result


    def listBuckets() =
      for {
        _           <- logger.debug(s"[ListBuckets]")
        rootDir     <- config.string("local.root")
        result      <- anyRoute.destination match {
                        case Local =>
                          Task(
                            Files.list(Paths.get(rootDir)).toList.asScala.toList.map { path =>
                              val at = Files.readAttributes(path, classOf[BasicFileAttributes])
                              Bucket.builder().name(path.getFileName.toString).creationDate(at.creationTime().toInstant).build()
                            }
                          )

                        case S3(r,c) => s3.listBuckets(client)
                        case S3Compatible(e,c) => s3.listBuckets(client)
                      }
      } yield result


    def bucketExists(bucket: String) =
      for {
        _           <- logger.debug(s"[BucketExists] $bucket")
        rootDir     <- config.string("local.root")
        result      <- anyRoute.destination match {
                        case Local => Task(Files.exists(Paths.get(rootDir, bucket)))
                        case S3(r,c) => s3.bucketExists(client, bucket)
                        case S3Compatible(e,c) => s3.listBuckets(client, bucket)
                      }
      } yield result


    def getBucketPolicy(bucket: String) =
      s3.getBucketPolicy(client, bucket)


    def getBucketAcl(bucket: String) =
      s3.getBucketAcl(client, bucket)


    def putBucketAcl(bucket: String, acl: String) =
      s3.putBucketAcl(client, bucket, acl)


    def listObjects(bucket: String, prefix: Option[String] = None) =
      for {
        _           <- logger.debug(s"[ListObjects] $bucket")
        rootDir     <- config.string("local.root")
        result      <- anyRoute.destination match {
                        case Local =>
                          val bucketPath = Paths.get(rootDir, bucket)
                          if (Files.notExists(bucketPath))
                            Task.fail(new S3Exception(S3ErrorCode.NO_SUCH_BUCKET))
                          else
                            Task(
                              Files.walk(bucketPath).toList.asScala.map { path =>
                                val at = Files.readAttributes(path, classOf[BasicFileAttributes])
                                S3Object.builder()
                                  .key(path.toUri.getPath)
                                  .size(at.size())
                                  .lastModified(at.lastModifiedTime().toInstant)
                                  .eTag(UUID.randomUUID().toString)
                                  .build()
                              }
                            )

                        case S3(r,c) => s3.listObjects(client, bucket, prefix)
                        case S3Compatible(e,c) => s3.listObjects(client, bucket, prefix)
                      }
      } yield result


    def deleteObject(bucket: String, key: String) =
      for {
        _           <- logger.debug(s"[DeleteObject] $bucket / $key")
        result      <- anyRoute.destination match {
                        case Local => fileOperation(bucket, key, f => Task(Files.delete(f)))
                        case S3(r,c) => s3.deleteObject(client, bucket, key)
                        case S3Compatible(e,c) => s3.deleteObject(client, bucket, key)
                      }
      } yield result


    def deleteObjects(bucket: String, keys: List[String]) =
      for {
        _           <- logger.debug(s"[DeleteObjects] $bucket / ${keys.mkString(",")}")
        result      <- anyRoute.destination match {
                        case Local => Task.foreach(keys)(key => fileOperation(bucket, key, f => Task(Files.delete(f))))
                        case S3(r,c) => s3.deleteObjects(client, bucket, keys.map(ObjectIdentifier.builder().key(_).build()))
                        case S3Compatible(e,c) => s3.deleteObjects(client, bucket, keys.map(ObjectIdentifier.builder().key(_).build()))
                      }
      } yield result


    def getObject(bucket: String, key: String) =
      for {
        _           <- logger.debug(s"[GetObject] $bucket / $key")
        rootDir     <- config.string("local.root")
        result      <- anyRoute.destination match {
                        case Local => fileOperation(bucket, key, f =>
                          for {
                            file    <- jasyncfio.open(f.toAbsolutePath.toString)
//                                    <- file.
                            result       <- jasyncfio.close(file)
                          } yield result

                        case S3(r,c) => s3.getObject(client, bucket, key)
                        case S3Compatible(e,c) => s3.getObject(client, bucket, key)
                      }
      } yield result


    def putObject(bucket: String, key: String, writeStream: ReactiveWriteStream[Buffer]) =
      s3.putObject(client, bucket, key, writeStream)


    def copyObject(sourceBucket: String, sourceKey: String, destinationBucket: String, destinationKey: String) =
      s3.copyObject(client, sourceBucket, sourceKey, destinationBucket, destinationKey)


    def getObjectAcl(bucket: String, key: String) =
      s3.getObjectAcl(client, bucket, key)


    def putObjectAcl(bucket: String, key: String, acl: String) =
      s3.putObjectAcl(client, bucket, key, acl)


    def uploadPartCopy(sourceBucket: String, sourceKey: String, destinationBucket: String, destinationKey: String, uploadId: String) =
      s3.uploadPartCopy(client, sourceBucket, sourceKey, destinationBucket, destinationKey, uploadId)


    def uploadPart(bucket: String, key: String, uploadId: String, writeStream: ReactiveWriteStream[Buffer]) =
      s3.uploadPart(client, bucket, key, uploadId, writeStream)


    def listParts(bucket: String, key: String, uploadId: String) =
      s3.listParts(client, bucket, key, uploadId)


    def listMultipartUploads(bucket: String) =
      s3.listMultipartUploads(client, bucket)


    def createMultipartUpload(bucket: String, key: String) =
      s3.createMultipartUpload(client, bucket, key)


    def abortMultipartUpload(bucket: String, key: String, uploadId: String) =
      s3.abortMultipartUpload(client, bucket, key, uploadId)


    def completeMultipartUpload(bucket: String, key: String, uploadId: String) =
      s3.completeMultipartUpload(client, bucket, key, uploadId)


    private def fileOperation[T](bucket: String, key: String, operation: Path => Task[T]) =
      for {
        rootDir     <- config.string("local.root")
        path        =  Paths.get(rootDir, bucket, key)
        result      <- if (Files.notExists(Paths.get(rootDir, bucket)))
                        Task.fail(new S3Exception(S3ErrorCode.NO_SUCH_BUCKET))
                       else if (Files.notExists(path))
                        Task.fail(new S3Exception(S3ErrorCode.NO_SUCH_KEY))
                       else
                        operation(path)
    } yield result


    private def validAccess(bucket: String, key: Option[String] = None)(check: AccessPolicy => Boolean) = {
      val term = if (key.isDefined) s"$bucket/$key" else bucket
      routesRef.get().filter(_.pathMatch.matches(term)).flatMap(_.accessPolicies).exists(check)
    }

    private def route(bucket: String, key: Option[String] = None) = {
      val term = if (key.isDefined) s"$bucket/$key" else bucket
      routesRef.get().filter(_.pathMatch.matches(term)).maxBy(_.priority)
    }

    private def anyRoute =
      routesRef.get().filter(_.pathMatch == PathMatch.Any).maxBy(_.priority)

    private def clients =
      routesRef.get().foreach { route =>

      }
  }}
}
