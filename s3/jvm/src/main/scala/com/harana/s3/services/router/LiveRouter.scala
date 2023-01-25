package com.harana.s3.services.router

import com.harana.modules.aws_s3.AwsS3
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.file.File
import com.harana.modules.ohc.OHC
import com.harana.s3.models.Destination._
import com.harana.s3.models.S3Credentials.toAWSCredentialsProvider
import com.harana.s3.models.{AccessPolicy, Route, S3Credentials}
import com.harana.s3.services.server.models.{S3ErrorCode, S3Exception}
import io.vertx.core.buffer.Buffer
import io.vertx.core.streams.Pump
import io.vertx.ext.reactivestreams.{ReactiveReadStream, ReactiveWriteStream}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model._
import zio.CanFail.canFailAmbiguous1
import zio.clock.Clock
import zio.{IO, Task, UIO, ZIO, ZLayer}

import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{Files, Path}
import java.time.Instant
import java.util.UUID
import java.util.concurrent.atomic.AtomicReference
import scala.jdk.CollectionConverters._

object LiveRouter {

  private val routesRef = new AtomicReference[List[Route]](List.empty)
  private val s3ClientsRef = new AtomicReference[Map[Int, S3AsyncClient]](Map.empty)

  val layer = ZLayer.fromServices { (clock: Clock.Service,
                                     config: Config.Service,
                                     file: File.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     ohc: OHC.Service,
                                     s3: AwsS3.Service) => new Router.Service {

    // 💚
    def createBucket(bucket: String) =
      for {
        _           <- logger.info(s"[CreateBucket] $bucket")
        rootDir     <- localRootDirectory
        _           <- if (validAccess(bucket)(_.createBuckets))
                        route(bucket).map(_.destination) match {
                          case None =>
                            IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                          case Some(Local) =>
                            val path = rootDir.resolve(bucket)
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
        _             <- logger.info(s"[DeleteBucket] $bucket")
        rootDir       <- localRootDirectory
        result        <- if (validAccess(bucket)(_.deleteBuckets))
                          route(bucket).map(_.destination) match {
                            case None =>
                              IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                            case Some(Local) =>
                              val path = rootDir.resolve(bucket)
                              if (Files.notExists(path)) IO.fail(S3Exception(S3ErrorCode.NO_SUCH_BUCKET))
                              else if (Files.list(path).count() > 0) IO.fail(S3Exception(S3ErrorCode.BUCKET_NOT_EMPTY))
                              else toS3Exception(IO(Files.delete(path)))

                            case Some(S3(credentials, region, endpoint)) =>
                              s3Operation(credentials, region, endpoint)(s3.deleteBucket(_, bucket))
                          }
                        else IO.fail(S3Exception(S3ErrorCode.ACCESS_DENIED))
      } yield result


    def listBuckets() =
      logger.info(s"[ListBuckets]") *> UIO(List.empty[Bucket])


    def bucketExists(bucket: String) =
      for {
        _           <- logger.info(s"[BucketExists] $bucket")
        result      <- route(bucket).map(_.destination) match {
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
        _           <- logger.info(s"[GetBucketPolicy] $bucket")
        result      <- route(bucket).map(_.destination) match {
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
        _           <- logger.info(s"[GetBucketAcl] $bucket")
        result      <- route(bucket).map(_.destination) match {
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
        _           <- logger.info(s"[PutBucketAcl] $bucket")
        rootDir     <- localRootDirectory
        result      <- route(bucket).map(_.destination) match {
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
        _           <- logger.info(s"[ListObjects] $bucket")
        rootDir     <- localRootDirectory
        result      <- route(bucket).map(_.destination) match {
                        case None =>
                          IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                        case Some(Local) =>
                          val bucketPath = rootDir.resolve(bucket)
                          if (Files.notExists(bucketPath))
                            IO.fail(S3Exception(S3ErrorCode.NO_SUCH_BUCKET))
                          else
                            toS3Exception(IO(
                              Files.walk(bucketPath).toList.asScala.filterNot(_.equals(bucketPath)).map { path =>
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
        _           <- logger.info(s"[DeleteObject] $bucket / $key")
        result      <- route(bucket, Some(key)).map(_.destination) match {
                        case None =>
                          IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                        case Some(Local) =>
                          fileOperation(bucket, key)(f => IO(Files.delete(f)))

                        case Some(S3(credentials, region, endpoint)) =>
                          s3Operation(credentials, region, endpoint)(s3.deleteObject(_, bucket, key))
         }
      } yield result

    // FIXME need to iterate on keys
    def deleteObjects(bucket: String, keys: List[String]) =
      for {
        _           <- logger.info(s"[DeleteObjects] $bucket / ${keys.mkString(",")}")
        _           <- route(bucket, Some(keys.head)).map(_.destination) match {
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
        _           <- logger.info(s"[GetObject] $bucket / $key")
        result      <- route(bucket, Some(key)).map(_.destination) match {
                        case None =>
                          IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                        case Some(Local) =>
                          fileOperation(bucket, key)(path => file.readStream(path.toString))

                        case Some(S3(credentials, region, endpoint)) =>
                          s3Operation(credentials, region, endpoint)(s3.getObject(_, bucket, key, ifMatch, ifNoneMatch, ifModifiedSince, ifUnmodifiedSince, range))
                      }
      } yield result

    // 💚
    def putObject(bucket: String,
                  key: String,
                  stream: ReactiveWriteStream[Buffer],
                  streamPump: Pump,
                  contentLength: Long,
                  acl: ObjectCannedACL,
                  contentMD5: Option[String] = None,
                  storageClass: Option[String] = None,
                  tags: Map[String, String] = Map()) =
      for {
        _           <- logger.info(s"[PutObject] $bucket / $key")
        result      <- route(bucket, Some(key)).map(_.destination) match {
                        case None =>
                          IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                        case Some(Local) =>
                          for {
                            rootDir     <- localRootDirectory
                            result      <- file.writeStream(rootDir.resolve(bucket).resolve(key).toString, stream, contentLength, Some(() => streamPump.start()), Some(() => streamPump.stop())).as("")
                          } yield result

                        case Some(S3(credentials, region, endpoint)) =>
                          s3Operation(credentials, region, endpoint)(s3.putObject(_, bucket, key, stream, acl, Some(contentLength), contentMD5, storageClass, tags))
                       }
      } yield result



    // ❤️
    def copyObject(sourceBucket: String, sourceKey: String, destinationBucket: String, destinationKey: String) =
      for {
        _           <- logger.info(s"[CopyObject]")
        result      <- route(sourceBucket, Some(sourceKey)).map(_.destination) match {
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
        _           <- logger.info(s"[GetObjectAttributes] $bucket / $key")
        result      <- route(bucket, Some(key)).map(_.destination) match {
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
        _           <- logger.info(s"[GetObjectACL] $bucket / $key")
        result      <- route(bucket, Some(key)).map(_.destination) match {
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
        _           <- logger.info(s"[PutObjectACL] $bucket / $key")
        result      <- route(bucket, Some(key)).map(_.destination) match {
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
        _           <- logger.info(s"[UploadPartCopy] $sourceBucket / $sourceKey - $destinationBucket / $destinationKey")
        result      <- route(sourceBucket, Some(sourceKey)).map(_.destination) match {
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
        _           <- logger.info(s"[UploadPart] $bucket / $key")
        result      <- route(bucket, Some(key)).map(_.destination) match {
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
        _           <- logger.info(s"[ListParts] $bucket / $key")
        result      <- route(bucket, Some(key)).map(_.destination) match {
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
        _           <- logger.info(s"[ListMultipartUploads] $bucket")
        result      <- route(bucket).map(_.destination) match {
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
        _           <- logger.info(s"[CreateMultipartUpload] $bucket / $key")
        result      <- route(bucket, Some(key)).map(_.destination) match {
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
        _           <- logger.info(s"[AbortMultipartUpload] $bucket / $key")
        result      <- route(bucket, Some(key)).map(_.destination) match {
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
        _           <- logger.info(s"[CompleteMultipartUpload] $bucket / $key")
        result      <- route(bucket, Some(key)).map(_.destination) match {
                          case None =>
                            IO.fail(S3Exception(S3ErrorCode.ROUTE_NOT_FOUND))

                          case Some(Local) =>
                            toS3Exception("")

                          case Some(S3(credentials, region, endpoint)) =>
                            s3Operation(credentials, region, endpoint)(s3.completeMultipartUpload(_, bucket, key, uploadId))
                        }
      } yield result


    def updateRoutes(routes: List[Route]) =
      UIO(routesRef.set(routes))


    private def bucketOperation[T](bucket: String)(operation: Path => Task[T]): IO[S3Exception, T] =
      for {
        rootDir     <- localRootDirectory
        path        =  rootDir.resolve(bucket)
        result      <- if (Files.notExists(rootDir.resolve(bucket)))
                        IO.fail(S3Exception(S3ErrorCode.NO_SUCH_BUCKET))
                       else
                        toS3Exception(operation(path))
    } yield result


    private def fileOperation[T](bucket: String, key: String)(operation: Path => Task[T]): IO[S3Exception, T] =
      for {
        rootDir     <- localRootDirectory
        path        =  rootDir.resolve(bucket).resolve(key)
        result      <- if (Files.notExists(rootDir.resolve(bucket)))
                        IO.fail(S3Exception(S3ErrorCode.NO_SUCH_BUCKET))
                       else if (Files.notExists(path)) {

          println(s"Path = ${path} does not exist")
          IO.fail(S3Exception(S3ErrorCode.NO_SUCH_KEY))
        } else
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
      task.mapError(e => S3Exception(S3ErrorCode.UNKNOWN_ERROR, e.getMessage, e.fillInStackTrace()))


    private def localRootDirectory =
      for {
        localRoot   <- config.string("local.root").orElseFail(S3Exception(S3ErrorCode.INVALID_LOCAL_ROOT))
        path        =  Path.of(localRoot)
        _           <- toS3Exception(ZIO.when(Files.notExists(path))(Task(Files.createDirectory(path))))
      } yield path


    private def validAccess(bucket: String, key: Option[String] = None)(check: AccessPolicy => Boolean) = {
      val term = if (key.nonEmpty) s"$bucket/$key" else bucket
      routesRef.get().filter(_.pathMatch.matches(term)).flatMap(_.accessPolicies).exists(check)
    }


    private def route(bucket: String, key: Option[String] = None) = {
      val term = if (key.nonEmpty) s"$bucket/$key" else bucket
      val routes = routesRef.get().filter(_.pathMatch.matches(term))
      if (routes.isEmpty) None else Some(routes.maxBy(_.priority))
    }
  }}
}
