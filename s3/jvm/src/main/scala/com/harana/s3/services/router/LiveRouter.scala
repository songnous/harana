package com.harana.s3.services.router

import com.harana.modules.aws_s3.AwsS3
import com.harana.modules.core.cache.Cache
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.jasyncfio.Jasyncfio
import com.harana.modules.ohc.OHC
import com.harana.modules.vertx.Vertx
import com.harana.s3.models.Destination._
import com.harana.s3.models.S3Credentials.toAWSCredentialsProvider
import com.harana.s3.models.{AccessPolicy, PathMatch, Route, S3Credentials}
import com.harana.s3.services.server.models.{S3ErrorCode, S3Exception}
import io.vertx.core.buffer.Buffer
import io.vertx.ext.reactivestreams.{ReactiveReadStream, ReactiveWriteStream}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.{Bucket, BucketCannedACL, CopyObjectResult, CopyPartResult, GetBucketAclResponse, GetObjectAclResponse, GetObjectAttributesResponse, Grant, ObjectCannedACL, ObjectIdentifier, Owner, S3Object}
import zio.clock.Clock
import zio.{Task, ZLayer}

import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{Files, Path, Paths}
import java.time.Instant
import java.util.UUID
import java.util.concurrent.atomic.AtomicReference
import scala.jdk.CollectionConverters._

object LiveRouter {
  val layer = ZLayer.fromServices { (clock: Clock.Service,
                                     config: Config.Service,
                                     jasyncfio: Jasyncfio.Service,
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
        rootDir     <- config.string("local.root")
        _           <- if (validAccess(bucket)(_.createBuckets))
                        route(bucket).destination match {
                          case Local =>
                            val path = Paths.get(rootDir, bucket)
                            if (Files.exists(path))
                              Task.fail(new S3Exception(S3ErrorCode.BUCKET_ALREADY_EXISTS))
                            else
                              Task(Files.createDirectory(path))

                          case S3(credentials, region, endpoint) => 
                            s3Operation(credentials, region, endpoint)(s3.createBucket(_, bucket))
                        }
                      else Task.fail(new S3Exception(S3ErrorCode.ACCESS_DENIED))
      } yield ()


    // 💚
    def deleteBucket(bucket: String) =
      for {
        _             <- logger.debug(s"[DeleteBucket] $bucket")
        rootDir       <- config.string("local.root")
        result        <- if (validAccess(bucket)(_.deleteBuckets))
                          route(bucket).destination match {
                            case Local =>
                              val path = Paths.get(rootDir, bucket)
                              if (Files.notExists(path)) Task.fail(new S3Exception(S3ErrorCode.NO_SUCH_BUCKET))
                              else if (Files.list(path).count() > 0) Task.fail(new S3Exception(S3ErrorCode.BUCKET_NOT_EMPTY))
                              else Task(Files.delete(path))

                            case S3(credentials, region, endpoint) => 
                              s3Operation(credentials, region, endpoint)(s3.deleteBucket(_, bucket))
                          }
                        else Task.fail(new S3Exception(S3ErrorCode.ACCESS_DENIED))
      } yield result


    // 💚
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

                        case S3(credentials, region, endpoint) =>
                          s3Operation(credentials, region, endpoint)(s3.listBuckets)
                      }
      } yield result


    // 💚
    def bucketExists(bucket: String) =
      for {
        _           <- logger.debug(s"[BucketExists] $bucket")
        result      <- anyRoute.destination match {
                        case Local =>
                          bucketOperation(bucket)(path => Task(Files.exists(path)))

                        case S3(credentials, region, endpoint) =>
                          s3Operation(credentials, region, endpoint)(s3.bucketExists(_, bucket))
                      }
      } yield result


    // ❤️
    def getBucketPolicy(bucket: String) =
      for {
        _           <- logger.debug(s"[GetBucketPolicy] $bucket")
        result      <- anyRoute.destination match {
                        case Local =>
                          bucketOperation(bucket)(_ => Task(""))

                        case S3(credentials, region, endpoint) =>
                          s3Operation(credentials, region, endpoint)(s3.getBucketPolicy(_, bucket))
                      }
      } yield result


    // ❤️
    def getBucketAcl(bucket: String) =
      for {
        _           <- logger.debug(s"[GetBucketAcl] $bucket")
        result      <- anyRoute.destination match {
                        case Local =>
                          bucketOperation(bucket)(path => Task(
                              GetBucketAclResponse.builder()
                              .owner(Owner.builder().id("").displayName("").build())
                              .build()
                          ))

                        case S3(credentials, region, endpoint) =>
                          s3Operation(credentials, region, endpoint)(s3.getBucketAcl(_, bucket))
                      }
      } yield result


    // ❤️
    def putBucketAcl(bucket: String, acl: BucketCannedACL) =
      for {
        _           <- logger.debug(s"[PutBucketAcl] $bucket")
        rootDir     <- config.string("local.root")
        result      <- anyRoute.destination match {
                        case Local =>
                          Task.unit

                        case S3(credentials, region, endpoint) =>
                          s3Operation(credentials, region, endpoint)(s3.putBucketAcl(_, bucket, acl))
                      }
      } yield result


    // 💚
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
                              }.toList
                            )

                        case S3(credentials, region, endpoint) => 
                          s3Operation(credentials, region, endpoint)(s3.listObjects(_, bucket, prefix))
                      }
      } yield result

    // 💚
    def deleteObject(bucket: String, key: String) =
      for {
        _           <- logger.debug(s"[DeleteObject] $bucket / $key")
        result      <- anyRoute.destination match {
                        case Local =>
                          fileOperation(bucket, key)(f => Task(Files.delete(f)))

                        case S3(credentials, region, endpoint) =>
                          s3Operation(credentials, region, endpoint)(s3.deleteObject(_, bucket, key))
         }
      } yield result

    // 💚
    def deleteObjects(bucket: String, keys: List[String]) =
      for {
        _           <- logger.debug(s"[DeleteObjects] $bucket / ${keys.mkString(",")}")
        _           <- anyRoute.destination match {
                        case Local =>
                          Task.foreach(keys)(key => fileOperation(bucket, key)(f => Task(Files.delete(f))))

                        case S3(credentials, region, endpoint) =>
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
        result      <- anyRoute.destination match {
                        case Local =>
                          fileOperation(bucket, key)(path => jasyncfio.readStream(path.toString, stream).as(stream))

                        case S3(credentials, region, endpoint) =>
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
        result      <- anyRoute.destination match {
                        case Local =>
                          fileOperation(bucket, key)(path => jasyncfio.writeStream(path.toString, writeStream).as(""))

                        case S3(credentials, region, endpoint) =>
                          s3Operation(credentials, region, endpoint)(s3.putObject(_, bucket, key, writeStream, acl, contentLength, contentMD5, storageClass, tags))
                       }
      } yield result



    // ❤️
    def copyObject(sourceBucket: String, sourceKey: String, destinationBucket: String, destinationKey: String) =
      for {
        _           <- logger.debug(s"[CopyObject]")
        result      <- anyRoute.destination match {
                          case Local =>
                            Task(CopyObjectResult.builder().build())

                          case S3(credentials, region, endpoint) =>
                            s3Operation(credentials, region, endpoint)(s3.copyObject(_, sourceBucket, sourceKey, destinationBucket, destinationKey))
                        }
      } yield result

    // 💚
    def getObjectAttributes(bucket: String, key: String) =
      for {
        _           <- logger.debug(s"[GetObjectAttributes] $bucket / $key")
        result      <- anyRoute.destination match {
                          case Local =>
                            fileOperation(bucket, key)(path => Task {
                              val attributes = Files.readAttributes(path, classOf[BasicFileAttributes])

                              GetObjectAttributesResponse.builder()
                                .eTag(attributes.fileKey().toString)
                                .lastModified(attributes.lastModifiedTime().toInstant)
                                .objectSize(attributes.size())
                                .build()
                            })

                          case S3(credentials, region, endpoint) =>
                            s3Operation(credentials, region, endpoint)(s3.getObjectAttributes(_, bucket, key))
                        }
      } yield result


    // ❤️
    def getObjectAcl(bucket: String, key: String) =
      for {
        _           <- logger.debug(s"[GetObjectACL] $bucket / $key")
        result      <- anyRoute.destination match {
                          case Local =>
                            Task(
                              GetObjectAclResponse.builder().build()
                            )

                          case S3(credentials, region, endpoint) =>
                            s3Operation(credentials, region, endpoint)(s3.getObjectAcl(_, bucket, key))
                        }
      } yield result

    // ❤️
    def putObjectAcl(bucket: String, key: String, acl: ObjectCannedACL) =
      for {
        _           <- logger.debug(s"[PutObjectACL] $bucket / $key")
        result      <- anyRoute.destination match {
                          case Local =>
                            Task.unit

                          case S3(credentials, region, endpoint) =>
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
        result      <- anyRoute.destination match {
                          case Local =>
                            Task(CopyPartResult.builder().build())

                          case S3(credentials, region, endpoint) =>
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
        result      <- anyRoute.destination match {
                          case Local =>
                            Task("")

                          case S3(credentials, region, endpoint) =>
                            s3Operation(credentials, region, endpoint)(s3.uploadPart(_, bucket, key, uploadId, partNumber, writeStream))
                        }
      } yield result


    // ❤️
    def listParts(bucket: String, key: String, uploadId: String) =
      for {
        _           <- logger.debug(s"[ListParts] $bucket / $key")
        result      <- anyRoute.destination match {
                          case Local =>
                            Task(List())

                          case S3(credentials, region, endpoint) =>
                            s3Operation(credentials, region, endpoint)(s3.listParts(_, bucket, key, uploadId))
                        }
      } yield result


    // ❤️
    def listMultipartUploads(bucket: String) =
      for {
        _           <- logger.debug(s"[ListMultipartUploads] $bucket")
        result      <- anyRoute.destination match {
                          case Local =>
                            Task(List())

                          case S3(credentials, region, endpoint) =>
                            s3Operation(credentials, region, endpoint)(s3.listMultipartUploads(_, bucket))
                        }
      } yield result


    // ❤️
    def createMultipartUpload(bucket: String, key: String, cannedACL: ObjectCannedACL) =
      for {
        _           <- logger.debug(s"[CreateMultipartUpload] $bucket / $key")
        result      <- anyRoute.destination match {
                          case Local =>
                            Task("")

                          case S3(credentials, region, endpoint) =>
                            s3Operation(credentials, region, endpoint)(s3.createMultipartUpload(_, bucket, key, cannedACL))
                        }
      } yield result


    // ❤️
    def abortMultipartUpload(bucket: String, key: String, uploadId: String) =
      for {
        _           <- logger.debug(s"[AbortMultipartUpload] $bucket / $key")
        result      <- anyRoute.destination match {
                          case Local =>
                            Task.unit

                          case S3(credentials, region, endpoint) =>
                            s3Operation(credentials, region, endpoint)(s3.abortMultipartUpload(_, bucket, key, uploadId))
                        }
      } yield result

    // ❤️
    def completeMultipartUpload(bucket: String, key: String, uploadId: String) =
      for {
        _           <- logger.debug(s"[CompleteMultipartUpload] $bucket / $key")
        result      <- anyRoute.destination match {
                          case Local =>
                            Task("")

                          case S3(credentials, region, endpoint) =>
                            s3Operation(credentials, region, endpoint)(s3.completeMultipartUpload(_, bucket, key, uploadId))
                        }
      } yield result


    private def bucketOperation[T](bucket: String)(operation: Path => Task[T]) =
      for {
        rootDir     <- config.string("local.root")
        path        =  Paths.get(rootDir, bucket)
        result      <- if (Files.notExists(Paths.get(rootDir, bucket)))
                        Task.fail(new S3Exception(S3ErrorCode.NO_SUCH_BUCKET))
                       else
                        operation(path)
    } yield result


    private def fileOperation[T](bucket: String, key: String)(operation: Path => Task[T]) =
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

    
    private def s3Operation[T](credentials: S3Credentials, region: Option[String], endpoint: Option[String])(operation: S3AsyncClient => Task[T]): Task[T] = {
      val hc = credentials.hashCode() + region.hashCode()
      val map = s3ClientsRef.get()
      
      for {
        client <- if (map.contains(hc)) 
                    Task(map(hc)) 
                  else {
                    for {
                      client  <- s3.newClient(toAWSCredentialsProvider(credentials), region.map(Region.of), endpoint)
                      _       =  s3ClientsRef.set(map + (hc -> client))
                    } yield client
                  }
         result <- operation(client)
      } yield result
    }
    
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
  }}
}
