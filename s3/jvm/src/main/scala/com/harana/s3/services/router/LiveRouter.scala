package com.harana.s3.services.router

import com.harana.modules.aws_s3.AwsS3
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.file.File
import com.harana.modules.ohc.OHC
import com.harana.s3.models.Destination._
import com.harana.s3.models.S3Credentials.toAWSCredentialsProvider
import com.harana.s3.models.{AccessPolicy, PathMatch, Route, S3Credentials}
import com.harana.s3.services.server.models.{S3ErrorCode, S3Exception}
import io.vertx.core.buffer.Buffer
import io.vertx.ext.reactivestreams.{ReactiveReadStream, ReactiveWriteStream}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model._
import zio.CanFail.canFailAmbiguous1
import zio.clock.Clock
import zio.{IO, Task, UIO, ZLayer}

import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{Files, Path, Paths}
import java.time.Instant
import java.util.UUID
import java.util.concurrent.atomic.AtomicReference
import scala.jdk.CollectionConverters._

object LiveRouter {
  val layer = ZLayer.fromServices { (clock: Clock.Service,
                                     config: Config.Service,
                                     jasyncfio: File.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     ohc: OHC.Service,
                                     s3: AwsS3.Service) => new Router.Service {

    private val routesRef = new AtomicReference[List[Route]](List.empty)
    private val s3ClientsRef = new AtomicReference[Map[Int, S3AsyncClient]](Map.empty)


    // 💚
    def createBucket(bucket: String) =
      for {
        _           <- logger.debug(s"[CreateBucket] $bucket")
        rootDir     <- localRootDirectory
        _           <- if (validAccess(bucket)(_.createBuckets))
                        route(bucket).map(_.destination) match {
                          case None =>
                            IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                          case Some(Local) =>
                            val path = Paths.get(rootDir, bucket)
                            if (Files.exists(path))
                              IO.fail(S3Exception(S3ErrorCode.BUCKET_ALREADY_EXISTS))
                            else
                              toS3Exception(Files.createDirectory(path))

                          case Some(S3(credentials, region, endpoint)) =>
                            s3Operation(credentials, region, endpoint)(s3.createBucket(_, bucket))
                        }
                      else IO.fail(S3Exception(S3ErrorCode.ACCESS_DENIED))
      } yield ()


    // 💚
    def deleteBucket(bucket: String) =
      for {
        _             <- logger.debug(s"[DeleteBucket] $bucket")
        rootDir       <- config.string("local.root")
        result        <- if (validAccess(bucket)(_.deleteBuckets))
                          route(bucket).map(_.destination) match {
                            case None =>
                              IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                            case Some(Local) =>
                              val path = Paths.get(rootDir, bucket)
                              if (Files.notExists(path)) IO.fail(S3Exception(S3ErrorCode.NO_SUCH_BUCKET))
                              else if (Files.list(path).count() > 0) IO.fail(S3Exception(S3ErrorCode.BUCKET_NOT_EMPTY))
                              else toS3Exception(IO(Files.delete(path)))

                            case Some(S3(credentials, region, endpoint)) =>
                              s3Operation(credentials, region, endpoint)(s3.deleteBucket(_, bucket))
                          }
                        else IO.fail(S3Exception(S3ErrorCode.ACCESS_DENIED))
      } yield result


    // 💚
    def listBuckets() =
      for {
        _           <- logger.debug(s"[ListBuckets]")
        rootDir     <- localRootDirectory
        result      <- anyRoute.map(_.destination) match {
                        case None =>
                          IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                        case Some(Local) =>
                          toS3Exception(
                            Files.list(Paths.get(rootDir)).toList.asScala.toList.map { path =>
                              val at = Files.readAttributes(path, classOf[BasicFileAttributes])
                              Bucket.builder().name(path.getFileName.toString).creationDate(at.creationTime().toInstant).build()
                            }
                          )

                        case Some(S3(credentials, region, endpoint)) =>
                          s3Operation(credentials, region, endpoint)(s3.listBuckets)
                      }
      } yield result


    // 💚
    def bucketExists(bucket: String) =
      for {
        _           <- logger.debug(s"[BucketExists] $bucket")
        result      <- anyRoute.map(_.destination) match {
                        case None =>
                          IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                        case Some(Local) =>
                          bucketOperation(bucket)(path => IO(Files.exists(path)))

                        case Some(S3(credentials, region, endpoint)) =>
                          s3Operation(credentials, region, endpoint)(s3.bucketExists(_, bucket))
                      }
      } yield result


    // ❤️
    def getBucketPolicy(bucket: String) =
      for {
        _           <- logger.debug(s"[GetBucketPolicy] $bucket")
        result      <- anyRoute.map(_.destination) match {
                        case None =>
                          IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                        case Some(Local) =>
                          bucketOperation(bucket)(_ => toS3Exception(""))

                        case Some(S3(credentials, region, endpoint)) =>
                          s3Operation(credentials, region, endpoint)(s3.getBucketPolicy(_, bucket))
                      }
      } yield result


    // ❤️
    def getBucketAcl(bucket: String) =
      for {
        _           <- logger.debug(s"[GetBucketAcl] $bucket")
        result      <- anyRoute.map(_.destination) match {
                        case None =>
                          IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                        case Some(Local) =>
                          bucketOperation(bucket)(path => IO(
                              GetBucketAclResponse.builder()
                              .owner(Owner.builder().id("").displayName("").build())
                              .build()
                          ))

                        case Some(S3(credentials, region, endpoint)) =>
                          s3Operation(credentials, region, endpoint)(s3.getBucketAcl(_, bucket))
                      }
      } yield result


    // ❤️
    def putBucketAcl(bucket: String, acl: BucketCannedACL) =
      for {
        _           <- logger.debug(s"[PutBucketAcl] $bucket")
        rootDir     <- localRootDirectory
        result      <- anyRoute.map(_.destination) match {
                        case None =>
                          IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                        case Some(Local) =>
                          IO.unit

                        case Some(S3(credentials, region, endpoint)) =>
                          s3Operation(credentials, region, endpoint)(s3.putBucketAcl(_, bucket, acl))
                      }
      } yield result


    // 💚
    def listObjects(bucket: String, prefix: Option[String] = None) =
      for {
        _           <- logger.debug(s"[ListObjects] $bucket")
        rootDir     <- localRootDirectory
        result      <- anyRoute.map(_.destination) match {
                        case None =>
                          IO.fail((S3Exception(S3ErrorCode.ROUTE_NOT_FOUND)))

                        case Some(Local) =>
                          val bucketPath = Paths.get(rootDir, bucket)
                          if (Files.notExists(bucketPath))
                            IO.fail(S3Exception(S3ErrorCode.NO_SUCH_BUCKET))
                          else
                            toS3Exception(IO(
                              Files.walk(bucketPath).toList.asScala.map { path =>
                                val at = Files.readAttributes(path, classOf[BasicFileAttributes])
                                S3Object.builder()
                                  .key(path.toUri.getPath)
                                  .size(at.size())
                                  .lastModified(at.lastModifiedTime().toInstant)
                                  .eTag(UUID.randomUUID().toString)
                                  .build()
                              }.toList
                            ))

                        case Some(S3(credentials, region, endpoint)) =>
                          s3Operation(credentials, region, endpoint)(s3.listObjects(_, bucket, prefix))
                      }
      } yield result

    // 💚
    def deleteObject(bucket: String, key: String) =
      for {
        _           <- logger.debug(s"[DeleteObject] $bucket / $key")
        result      <- anyRoute.map(_.destination) match {
                        case None =>
                          IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                        case Some(Local) =>
                          fileOperation(bucket, key)(f => IO(Files.delete(f)))

                        case Some(S3(credentials, region, endpoint)) =>
                          s3Operation(credentials, region, endpoint)(s3.deleteObject(_, bucket, key))
         }
      } yield result

    // 💚
    def deleteObjects(bucket: String, keys: List[String]) =
      for {
        _           <- logger.debug(s"[DeleteObjects] $bucket / ${keys.mkString(",")}")
        _           <- anyRoute.map(_.destination) match {
                        case None =>
                          IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                        case Some(Local) =>
                          IO.foreach(keys)(key => fileOperation(bucket, key)(f => IO(Files.delete(f))))

                        case Some(S3(credentials, region, endpoint)) =>
                          s3Operation(credentials, region, endpoint)(s3.deleteObjects(_, bucket, keys.map(ObjectIdentifier.builder().key(_).build())))
        }
      } yield ()

    // 💚
    def getObject(bucket: String,
                  key: String,
                  ifMatch: Option[String] = None,
                  ifNoneMatch: Option[String] = None,
                  ifModifiedSince: Option[Instant] = None,
                  ifUnmodifiedSince: Option[Instant] = None,
                  range: Option[String] = None) =
      for {
        _           <- logger.debug(s"[GetObject] $bucket / $key")
        stream      =  ReactiveReadStream.readStream[Buffer]()
        result      <- anyRoute.map(_.destination) match {
                        case None =>
                          IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                        case Some(Local) =>
                          fileOperation(bucket, key)(path => jasyncfio.readStream(path.toString, stream).as(stream))

                        case Some(S3(credentials, region, endpoint)) =>
                          s3Operation(credentials, region, endpoint)(s3.getObject(_, bucket, key, ifMatch, ifNoneMatch, ifModifiedSince, ifUnmodifiedSince, range))
                      }
      } yield result

    // 💚
    def putObject(bucket: String,
                  key: String,
                  writeStream: ReactiveWriteStream[Buffer],
                  acl: ObjectCannedACL,
                  contentLength: Option[Long] = None,
                  contentMD5: Option[String] = None,
                  storageClass: Option[String] = None,
                  tags: Map[String, String] = Map()) =
      for {
        _           <- logger.debug(s"[PutObject] $bucket / $key")
        result      <- anyRoute.map(_.destination) match {
                        case None =>
                          IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                        case Some(Local) =>
                          fileOperation(bucket, key)(path => jasyncfio.writeStream(path.toString, writeStream).as(""))

                        case Some(S3(credentials, region, endpoint)) =>
                          s3Operation(credentials, region, endpoint)(s3.putObject(_, bucket, key, writeStream, acl, contentLength, contentMD5, storageClass, tags))
                       }
      } yield result



    // ❤️
    def copyObject(sourceBucket: String, sourceKey: String, destinationBucket: String, destinationKey: String) =
      for {
        _           <- logger.debug(s"[CopyObject]")
        result      <- anyRoute.map(_.destination) match {
                          case None =>
                            IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                          case Some(Local) =>
                            toS3Exception(CopyObjectResult.builder().build())

                          case Some(S3(credentials, region, endpoint)) =>
                            s3Operation(credentials, region, endpoint)(s3.copyObject(_, sourceBucket, sourceKey, destinationBucket, destinationKey))
                        }
      } yield result

    // 💚
    def getObjectAttributes(bucket: String, key: String) =
      for {
        _           <- logger.debug(s"[GetObjectAttributes] $bucket / $key")
        result      <- anyRoute.map(_.destination) match {
                          case None =>
                            IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                          case Some(Local) =>
                            fileOperation(bucket, key)(path => toS3Exception({
                              val attributes = Files.readAttributes(path, classOf[BasicFileAttributes])

                              GetObjectAttributesResponse.builder()
                                .eTag(attributes.fileKey().toString)
                                .lastModified(attributes.lastModifiedTime().toInstant)
                                .objectSize(attributes.size())
                                .build()
                            }))

                          case Some(S3(credentials, region, endpoint)) =>
                            s3Operation(credentials, region, endpoint)(s3.getObjectAttributes(_, bucket, key))
                        }
      } yield result


    // ❤️
    def getObjectAcl(bucket: String, key: String) =
      for {
        _           <- logger.debug(s"[GetObjectACL] $bucket / $key")
        result      <- anyRoute.map(_.destination) match {
                          case None =>
                            IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                          case Some(Local) =>
                            toS3Exception(
                              GetObjectAclResponse.builder().build()
                            )

                          case Some(S3(credentials, region, endpoint)) =>
                            s3Operation(credentials, region, endpoint)(s3.getObjectAcl(_, bucket, key))
                        }
      } yield result

    // ❤️
    def putObjectAcl(bucket: String, key: String, acl: ObjectCannedACL) =
      for {
        _           <- logger.debug(s"[PutObjectACL] $bucket / $key")
        result      <- anyRoute.map(_.destination) match {
                          case None =>
                            IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                          case Some(Local) =>
                            IO.unit

                          case Some(S3(credentials, region, endpoint)) =>
                            s3Operation(credentials, region, endpoint)(s3.putObjectAcl(_, bucket, key, acl))
                        }
      } yield result


    // ❤️
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
                       copySourceRange: Option[String]) =
      for {
        _           <- logger.debug(s"[UploadPartCopy] $sourceBucket / $sourceKey - $destinationBucket / $destinationKey")
        result      <- anyRoute.map(_.destination) match {
                          case None =>
                            IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                          case Some(Local) =>
                            toS3Exception(CopyPartResult.builder().build())

                          case Some(S3(credentials, region, endpoint)) =>
                            s3Operation(credentials, region, endpoint)(s3.uploadPartCopy(_,
                              sourceBucket, sourceKey, destinationBucket, destinationKey,
                              uploadId, partNumber, copySourceIfMatch, copySourceIfNoneMatch,
                              copySourceIfModifiedSince, copySourceIfUnmodifiedSince, copySourceRange))
                        }
      } yield result


    // ❤️
    def uploadPart(bucket: String, key: String, uploadId: String, partNumber: Int, writeStream: ReactiveWriteStream[Buffer]) =
      for {
        _           <- logger.debug(s"[UploadPart] $bucket / $key")
        result      <- anyRoute.map(_.destination) match {
                          case None =>
                            IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                          case Some(Local) =>
                            toS3Exception("")

                          case Some(S3(credentials, region, endpoint)) =>
                            s3Operation(credentials, region, endpoint)(s3.uploadPart(_, bucket, key, uploadId, partNumber, writeStream))
                        }
      } yield result


    // ❤️
    def listParts(bucket: String, key: String, uploadId: String) =
      for {
        _           <- logger.debug(s"[ListParts] $bucket / $key")
        result      <- anyRoute.map(_.destination) match {
                          case None =>
                            IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                          case Some(Local) =>
                            toS3Exception(List())

                          case Some(S3(credentials, region, endpoint)) =>
                            s3Operation(credentials, region, endpoint)(s3.listParts(_, bucket, key, uploadId))
                        }
      } yield result


    // ❤️
    def listMultipartUploads(bucket: String) =
      for {
        _           <- logger.debug(s"[ListMultipartUploads] $bucket")
        result      <- anyRoute.map(_.destination) match {
                          case None =>
                            IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                          case Some(Local) =>
                            toS3Exception(List())

                          case Some(S3(credentials, region, endpoint)) =>
                            s3Operation(credentials, region, endpoint)(s3.listMultipartUploads(_, bucket))
                        }
      } yield result


    // ❤️
    def createMultipartUpload(bucket: String, key: String, cannedACL: ObjectCannedACL) =
      for {
        _           <- logger.debug(s"[CreateMultipartUpload] $bucket / $key")
        result      <- anyRoute.map(_.destination) match {
                          case None =>
                            IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                          case Some(Local) =>
                            toS3Exception("")

                          case Some(S3(credentials, region, endpoint)) =>
                            s3Operation(credentials, region, endpoint)(s3.createMultipartUpload(_, bucket, key, cannedACL))
                        }
      } yield result


    // ❤️
    def abortMultipartUpload(bucket: String, key: String, uploadId: String) =
      for {
        _           <- logger.debug(s"[AbortMultipartUpload] $bucket / $key")
        result      <- anyRoute.map(_.destination) match {
                          case None =>
                            IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                          case Some(Local) =>
                            IO.unit

                          case Some(S3(credentials, region, endpoint)) =>
                            s3Operation(credentials, region, endpoint)(s3.abortMultipartUpload(_, bucket, key, uploadId))
                        }
      } yield result

    // ❤️
    def completeMultipartUpload(bucket: String, key: String, uploadId: String) =
      for {
        _           <- logger.debug(s"[CompleteMultipartUpload] $bucket / $key")
        result      <- anyRoute.map(_.destination) match {
                          case None =>
                            IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                          case Some(Local) =>
                            toS3Exception("")

                          case Some(S3(credentials, region, endpoint)) =>
                            s3Operation(credentials, region, endpoint)(s3.completeMultipartUpload(_, bucket, key, uploadId))
                        }
      } yield result


    private def bucketOperation[T](bucket: String)(operation: Path => Task[T]): IO[S3Exception, T] =
      for {
        rootDir     <- localRootDirectory
        path        =  Paths.get(rootDir, bucket)
        result      <- if (Files.notExists(Paths.get(rootDir, bucket)))
                        IO.fail(S3Exception(S3ErrorCode.NO_SUCH_BUCKET))
                       else
                        toS3Exception(operation(path))
    } yield result


    private def fileOperation[T](bucket: String, key: String)(operation: Path => Task[T]): IO[S3Exception, T] =
      for {
        rootDir     <- localRootDirectory
        path        =  Paths.get(rootDir, bucket, key)
        result      <- if (Files.notExists(Paths.get(rootDir, bucket)))
                        IO.fail(S3Exception(S3ErrorCode.NO_SUCH_BUCKET))
                       else if (Files.notExists(path))
                        IO.fail(S3Exception(S3ErrorCode.NO_SUCH_KEY))
                       else
                        toS3Exception(operation(path))
    } yield result

    
    private def s3Operation[T](credentials: S3Credentials, region: Option[String], endpoint: Option[String])(operation: S3AsyncClient => Task[T]): IO[S3Exception, T] = {
      val hc = credentials.hashCode() + region.hashCode()
      val map = s3ClientsRef.get()
      
      for {
        client <- if (map.contains(hc)) 
                    UIO(map(hc))
                  else {
                    for {
                      client  <- toS3Exception(s3.newClient(toAWSCredentialsProvider(credentials), region.map(Region.of), endpoint))
                      _       =  s3ClientsRef.set(map + (hc -> client))
                    } yield client
                  }
         result <- toS3Exception(operation(client))
      } yield result
    }


    private def toS3Exception[T](task: T): IO[S3Exception, T] =
      toS3Exception(IO(task))

    private def toS3Exception[T](task: Task[T]): IO[S3Exception, T] =
      task.mapError(e => S3Exception(S3ErrorCode.UNKNOWN_ERROR, e.getMessage, e.getCause))

    private def localRootDirectory =
      config.string("local.root").orElseFail(S3Exception(S3ErrorCode.INVALID_LOCAL_ROOT))


    private def validAccess(bucket: String, key: Option[String] = None)(check: AccessPolicy => Boolean) = {
      val term = if (key.isDefined) s"$bucket/$key" else bucket
      routesRef.get().filter(_.pathMatch.matches(term)).flatMap(_.accessPolicies).exists(check)
    }


    private def route(bucket: String, key: Option[String] = None) = {
      val term = if (key.isDefined) s"$bucket/$key" else bucket
      if (routesRef.get().isEmpty) None
      else Some(routesRef.get().filter(_.pathMatch.matches(term)).maxBy(_.priority))
    }


    private def anyRoute =
      if (routesRef.get().isEmpty) None 
      else Some(routesRef.get().filter(_.pathMatch == PathMatch.Any).maxBy(_.priority))
  }}
}
