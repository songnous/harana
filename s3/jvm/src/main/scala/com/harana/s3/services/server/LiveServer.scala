package com.harana.s3.services.server

import com.google.common.base.Strings
import com.google.common.hash.{HashCode, Hashing}
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.mongo.Mongo
import com.harana.modules.vertx.models.Response
import com.harana.modules.vertx.{Vertx, corsRules}
import com.harana.s3.models.{AccessPolicy, Credentials, Destination, PathMatch, Route}
import com.harana.s3.services.router.Router
import com.harana.s3.services.server.models._
import com.harana.s3.services.server.s3_server._
import com.harana.s3.utils.AwsXml
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpMethod._
import io.vertx.ext.web.RoutingContext
import software.amazon.awssdk.services.s3.model.{BucketCannedACL, CompleteMultipartUploadRequest, ObjectCannedACL}
import zio.{IO, Task, UIO, ZLayer}
import io.circe.parser._
import io.circe.syntax._
import zio.clock.Clock
import zio._
import zio.duration.durationInt

import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Base64
import java.util.concurrent.atomic.AtomicReference
import scala.collection.mutable

object LiveServer {
  val layer = ZLayer.fromServices { (clock: Clock.Service,
                                     config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service,
                                     router: Router.Service,
                                     vertx: Vertx.Service) => new Server.Service {

      def s3Request(rc: RoutingContext): Task[Response] = {
        val r = rc.request()
        val path = r.uri().split("/", 3)
        val bucket = path(1)
        val key = if (path.length > 2) Some(path(2)) else None
        val uploadId = r.getParam("uploadId")
        val MD5 = Hashing.md5()

        val response = r.method() match {

            // 💚
            case DELETE if key.isEmpty =>
              router.deleteBucket(bucket).as(Response.Empty())

            // 💚
            case DELETE if uploadId != null =>
              router.abortMultipartUpload(bucket, key.get, uploadId).as(Response.Empty(cors = true))

            // 💚
            case DELETE =>
              router.deleteObject(bucket, key.get).as(Response.Empty())

            // 💚
            case GET if r.uri().equals("/") =>
              xmlResponse(writer => router.listBuckets().map(buckets =>
                AwsXml.writeListAllMyBucketsResult(writer, buckets)
              ))

            // 💚
            case GET if key.isEmpty && r.getParam("acl") != null =>
              xmlResponse(writer => router.getBucketAcl(bucket).map(acl =>
                // FIXME
                AwsXml.writeAccessControlPolicy(writer, false))
              )

            // 💚
            case GET if key.isEmpty && r.getParam("location") != null =>
              xmlResponse(writer =>
                UIO(AwsXml.writeLocationConstraint(writer))
              )

            // 💚
            case GET if key.isEmpty && r.getParam("policy") != null =>
              UIO(Response.Empty())

            // 💚
            case GET if key.isEmpty && r.getParam("uploads") != null =>
              if (r.getParam("delimiter") != null || r.getParam("max-uploads") != null ||
                r.getParam("key-marker") != null || r.getParam("upload-id-marker") != null)
                throw S3Exception(S3ErrorCode.NOT_IMPLEMENTED)

              val encodingType = Option(r.getParam("encoding-type")).getOrElse("url")
              val prefix = Option(r.getParam("prefix"))

              xmlResponse(writer => router.listMultipartUploads(bucket).map(uploads =>
                AwsXml.writeListMultipartUploadsResult(writer, bucket, prefix, encodingType, uploads))
              )

            // ❤️
            case GET if key.isEmpty =>
              val encodingType = Option(r.getParam("encoding-type")).getOrElse("url")
              val prefix = Option(r.getParam("prefix"))
              val delimiter = Option(r.getParam("delimiter"))
              val listType = Option(r.getParam("list-type"))
              val continuationToken = Option(r.getParam("continuation-token"))
              val startAfter = Option(r.getParam("start-after"))
              val isListV2 = listType.isDefined && listType.get.equals("2")
              val fetchOwner = !isListV2 && r.getParam("fetch  -owner").equals("true")

              val marker = {
                if (listType.isEmpty) Option(r.getParam("marker"))
                else if (isListV2) {
                  if (continuationToken.isDefined && startAfter.isDefined) throw S3Exception(S3ErrorCode.INVALID_ARGUMENT)
                  if (continuationToken.isDefined) continuationToken else startAfter
                } else throw S3Exception(S3ErrorCode.NOT_IMPLEMENTED)
              }

              val maxKeys = {
                try {
                  val max = Option(r.getParam("max-keys")).map(_.toInt)
                  if (max.isEmpty || max.get > 1000) 1000 else max.get
                } catch {
                  case _: Exception => throw S3Exception(S3ErrorCode.INVALID_ARGUMENT)
                }
              }

              xmlResponse(writer => router.listObjects(bucket, prefix).map(objects =>
                AwsXml.writeListBucketResult(writer, bucket, prefix, objects, encodingType, isListV2, maxKeys, fetchOwner, marker, continuationToken, startAfter, delimiter))
              )

            // 💚
            case GET if r.getParam("acl") != null =>
              xmlResponse(writer => router.getObjectAcl(bucket, key.get).map(acl =>
                // FIXME
                AwsXml.writeAccessControlPolicy(writer, false)
              ))

            // 💚
            case GET if uploadId != null =>
              val partNumberMarker = r.getParam("part-number-marker")
              if (partNumberMarker != null && !partNumberMarker.equals("0"))
                throw S3Exception(S3ErrorCode.NOT_IMPLEMENTED)

              val encodingType = Option(r.getParam("encoding-type")).getOrElse("url")
              xmlResponse(xml =>
                router.listParts(bucket, key.get, uploadId).map { parts =>
                  AwsXml.writeListPartsResult(xml, bucket, key.get, uploadId, encodingType, parts)
                }
              )

            // ❤️
            case GET =>
              for {
                ifMatch             <- UIO(Option(r.getHeader(HttpHeaders.IF_MATCH)))
                ifNoneMatch         =  Option(r.getHeader(HttpHeaders.IF_NONE_MATCH))
                ifModifiedSince     =  Option(r.getHeader(HttpHeaders.IF_MODIFIED_SINCE)).map(_.toLong)
                ifUnmodifiedSince   =  Option(r.getHeader("If-Unmodified-Since")).map(_.toLong)
                range               =  Option(r.getHeader("range"))
                response            <- router.getObject(bucket, key.get, ifMatch, ifNoneMatch, ifModifiedSince.map(Instant.ofEpochMilli), ifUnmodifiedSince.map(Instant.ofEpochMilli), range).map(Response.ReadStream(_))
              } yield response

            // 💚
            case HEAD if key.isEmpty =>
              for {
                exists    <- router.bucketExists(bucket)
                response  = if (exists) Response.Empty() else Response.Empty(statusCode = Some(404))
              } yield response

            // 💚
            case HEAD =>
              for {
                attributes          <- router.getObjectAttributes(bucket, key.get)
                ifMatch             = Option(r.getHeader(HttpHeaders.IF_MATCH))
                ifNoneMatch         = Option(r.getHeader(HttpHeaders.IF_NONE_MATCH))
                ifModifiedSince     = Option(r.getHeader(HttpHeaders.IF_MODIFIED_SINCE)).map(_.toLong)
                ifUnmodifiedSince   = Option(r.getHeader("If-Unmodified-Since")).map(_.toLong)
                eTag                = Option(attributes.eTag()).map(maybeQuoteETag)
                lastModified        = Option(attributes.lastModified())
                size                = Option(attributes.objectSize())
                storageClass        = Option(attributes.storageClass().name())

                response = (eTag, lastModified) match {
                  case (Some(eTag), _) if ifMatch.isDefined && !ifMatch.get.equals(eTag) =>
                    throw S3Exception(S3ErrorCode.PRECONDITION_FAILED)

                  case (Some(eTag), _) if ifNoneMatch.isDefined && ifNoneMatch.get.equals(eTag) =>
                    Response.Empty(statusCode = Some(304))

                  case (_, Some(lastModified)) if ifModifiedSince.isDefined && lastModified.compareTo(Instant.ofEpochMilli(ifModifiedSince.get)) <= 0 =>
                    throw S3Exception(S3ErrorCode.PRECONDITION_FAILED)

                  case (_, Some(lastModified)) if ifUnmodifiedSince.isDefined && lastModified.compareTo(Instant.ofEpochMilli(ifUnmodifiedSince.get)) >= 0 =>
                    Response.Empty(statusCode = Some(304))

                  case _ => Response.Empty(headers = Map(
                    HttpHeaders.ETAG -> eTag.map(e => List(e)).getOrElse(List.empty),
                    HttpHeaders.LAST_MODIFIED -> lastModified.map(m => List(m.getEpochSecond.toString)).getOrElse(List.empty),
                    HttpHeaders.CONTENT_LENGTH -> size.map(s => List(s.toString)).getOrElse(List.empty),
                    AwsHttpHeaders.STORAGE_CLASS.value -> storageClass.map(s => List(s)).getOrElse(List.empty)
                  ))
                }
              } yield response

            // 💚
            case POST if r.getParam("delete") != null =>
              xmlResponse(writer => xmlRequest(rc, classOf[DeleteMultipleObjectsRequest])(request => {
                val keys = request.objects.map(_.key)
                router.deleteObjects(bucket, keys).as(AwsXml.writeDeleteResult(writer, false, keys))
              }))

            // 💚
            case POST if r.getParam("uploads") != null =>
              val acl = ObjectCannedACL.valueOf(r.getHeader(AwsHttpHeaders.ACL.value))

              xmlResponse(writer => router.createMultipartUpload(bucket, key.get, acl).map(uploadId =>
                AwsXml.writeInitiateMultipartUploadResult(writer, bucket, key.get, uploadId)
              ))

            // 💚
            case POST if uploadId != null && r.getParam("partNumber") == null =>
              xmlResponse(writer => xmlRequest(rc, classOf[CompleteMultipartUploadRequest])(request =>
                router.completeMultipartUpload(bucket, key.get, uploadId).map(etag =>
                  AwsXml.writeCompleteMultipartUploadResult(writer, request.bucket(), request.key(), request.uploadId(), Some(etag))
                )
              ))

            // 💚
            case PUT if key.isEmpty && r.getParam("acl") != null =>
              IO.whenCaseM(hasBody(rc)) {
                case true =>
                  for {
                    acl <- xmlRequest(rc, classOf[AccessControlPolicy])(request => UIO(mapXmlAclsToCannedPolicy(request) match {
                      case "private" => BucketCannedACL.PRIVATE
                      case "public-read" => BucketCannedACL.PUBLIC_READ
                      case _ => throw S3Exception(S3ErrorCode.NOT_IMPLEMENTED)
                    }))
                    _ <- router.putBucketAcl(bucket, acl)
                  } yield ()

                case false =>
                  router.putBucketAcl(bucket,
                    Option(r.getHeader(AwsHttpHeaders.ACL.value)) match {
                      case Some("private") => BucketCannedACL.PRIVATE
                      case Some("public-read") => BucketCannedACL.PUBLIC_READ
                      case Some(acl) if cannedAcls.contains(acl) => throw S3Exception(S3ErrorCode.NOT_IMPLEMENTED)
                      case _ => throw S3Exception(S3ErrorCode.INVALID_REQUEST)
                    }
                  )
              }.as(Response.Empty())

            // 💚
            case PUT if key.isEmpty =>
              xmlRequest(rc, classOf[CreateBucketRequest])(_ => {
                router.createBucket(bucket).as(Response.Empty(headers = Map(HttpHeaders.LOCATION -> List(s"/$bucket"))))
              })

            // ❤️
            case PUT if uploadId != null =>
              Option(r.getHeader(AwsHttpHeaders.COPY_SOURCE.value)) match {
                case Some(copySource) =>
                  val sourceHeader = URLDecoder.decode(copySource, StandardCharsets.UTF_8)
                  val sourcePath = (if (sourceHeader.startsWith("/")) sourceHeader.substring(1) else sourceHeader).split("/", 2)
                  val (sourceBucket, sourceKey) = (sourcePath(0), sourcePath(1))
                  if (sourcePath.length != 2) throw S3Exception(S3ErrorCode.INVALID_REQUEST)

                  val partNumberString = Option(r.getParam("partNumber"))
                  if (partNumberString.isEmpty) throw S3Exception(S3ErrorCode.INVALID_ARGUMENT)

                  // FIXME
                  UIO(Response.Empty())

                case None =>
                  for {
                    partNumber  <- UIO(r.getParam("partNumber").toInt).onError(_ => throw S3Exception(S3ErrorCode.INVALID_ARGUMENT))
                    eTag        <- vertx.withBodyAsStream(rc)(body => router.uploadPart(bucket, key.get, uploadId, partNumber, body))
                                    .mapError(e => S3Exception(S3ErrorCode.UNKNOWN_ERROR, e.getMessage, e.fillInStackTrace()))
                    response    = Response.Empty(headers = Map(HttpHeaders.ETAG -> List(eTag)), cors = true)
                  } yield response
              }

            // 💚
            case PUT if r.getHeader(AwsHttpHeaders.COPY_SOURCE.value) != null =>
              val sourceHeader = URLDecoder.decode(r.getHeader(AwsHttpHeaders.COPY_SOURCE.value), StandardCharsets.UTF_8)
              val sourcePath = (if (sourceHeader.startsWith("/")) sourceHeader.substring(1) else sourceHeader).split("/", 2)
              val (sourceBucket, sourceKey) = (sourcePath(0), sourcePath(1))
              if (sourcePath.length != 2) throw S3Exception(S3ErrorCode.INVALID_REQUEST)

              val replaceMetadata = r.getHeader(AwsHttpHeaders.METADATA_DIRECTIVE.value).equalsIgnoreCase("REPLACE")
              if (sourceBucket.equals(bucket) && sourceKey.equals(key.get) && !replaceMetadata) throw S3Exception(S3ErrorCode.INVALID_REQUEST)

              xmlResponse(xml => router.copyObject(sourceBucket, sourceKey, bucket, key.get).map(result =>
                AwsXml.writeCopyObjectResult(xml, result.lastModified(), result.eTag())
              ))

            // 💚
            case PUT if r.getParam("acl") != null =>
              for {
                acl       <- acl(rc)
                _         <- router.putObjectAcl(bucket, key.get, acl)
                response  = Response.Empty()
              } yield response

            // 💚
            case PUT =>
              val contentLength = Option(r.getHeader(HttpHeaders.CONTENT_LENGTH))
              val decodedContentLength = Option(r.getHeader(AwsHttpHeaders.DECODED_CONTENT_LENGTH.value))
              val finalContentLength = if (decodedContentLength.isDefined) Some(decodedContentLength.get) else contentLength
              if (finalContentLength.isEmpty) throw S3Exception(S3ErrorCode.MISSING_CONTENT_LENGTH)

              val contentMd5 = Option(r.getHeader(HttpHeaders.CONTENT_MD5))
              if (contentMd5.isDefined)
                try {
                  val md5 = HashCode.fromBytes(Base64.getDecoder.decode(contentMd5.get))
                  if (md5.bits() != MD5.bits()) throw S3Exception(S3ErrorCode.INVALID_DIGEST)
                } catch {
                  case _: IllegalArgumentException =>
                    throw S3Exception(S3ErrorCode.INVALID_DIGEST)
                }

              for {
                acl       <- acl(rc)
                eTag      <- vertx.withBodyAsStream(rc)(body => router.putObject(bucket, key.get, body, acl))
                              .mapError(e => S3Exception(S3ErrorCode.UNKNOWN_ERROR, e.getMessage, e.fillInStackTrace()))
                response  = Response.Empty(headers = Map(HttpHeaders.ETAG -> List(eTag)), cors = true)
              } yield response

            // 💚
            case OPTIONS =>
              val headers = mutable.Map.empty[CharSequence, List[_ <: CharSequence]]

              val corsOrigin = r.getHeader(HttpHeaders.ORIGIN)
              if (Strings.isNullOrEmpty(corsOrigin)) throw S3Exception(S3ErrorCode.INVALID_CORS_ORIGIN)
              if (!corsRules.isOriginAllowed(corsOrigin)) throw S3Exception(S3ErrorCode.INVALID_CORS_ORIGIN)

              val corsMethod = r.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD)
              if (Strings.isNullOrEmpty(corsMethod)) throw S3Exception(S3ErrorCode.INVALID_CORS_METHOD)
              if (!corsRules.isMethodAllowed(corsMethod)) throw S3Exception(S3ErrorCode.INVALID_CORS_METHOD)

              val corsHeaders = r.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS)
              if (Strings.isNullOrEmpty(corsHeaders))
                if (corsRules.isEveryHeaderAllowed(corsHeaders))
                  headers.put(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, List(corsHeaders))
                else
                  throw S3Exception(S3ErrorCode.ACCESS_DENIED)

              headers.put(HttpHeaders.VARY, List(HttpHeaders.ORIGIN))
              headers.put(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, List(corsRules.getAllowedOrigin(corsOrigin)))
              headers.put(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, List(corsRules.getAllowedMethods))

              for {
                exists    <- router.bucketExists(bucket)
                _         = if (!exists) throw S3Exception(S3ErrorCode.ACCESS_DENIED) else UIO.unit
                response  = Response.Empty(headers = headers.toMap)
              } yield response
          }

          response.catchAll(e =>
            logger.error(e.cause)(e.message) *> UIO(Response.Empty(statusCode = Some(e.error.statusCode)))
          )
      }

    def createRoute(rc: RoutingContext): Task[Response] =
      for {
        route       <- Task.fromEither(decode[Route](rc.body().asString))
        _           <- mongo.insert[Route]("Routes", route)
        response    =  Response.Empty()
      } yield response


    def deleteRoute(rc: RoutingContext): Task[Response] =
      for {
        id          <- Task(rc.pathParam("id"))
        _           <- mongo.deleteEquals[Route]("Routes", Map("id" -> id))
        response    =  Response.Empty()
      } yield response


    def updateRoute(rc: RoutingContext): Task[Response] =
      for {
        id          <- Task(rc.pathParam("id"))
        route       <- Task.fromEither(decode[Route](rc.body().asString))
        _           <- mongo.insert[Route]("Routes", route)
        _           <- mongo.update[Route]("Routes", id, route)
        response    =  Response.Empty()
      } yield response


    def listRoutes(rc: RoutingContext): Task[Response] =
      for {
        routes      <- mongo.all[Route]("Routes")
        response    =  Response.JSON(routes.asJson)
      } yield response


    def syncRoutes =
      (mongo.all[Route]("Routes") >>= router.updateRoutes)
        .repeat(Schedule.spaced(10.second).forever)
        .provide(Has(clock)).fork.unit


    def sampleData(rc: RoutingContext): Task[Response] =
      for {
        _           <- logger.info("Inserting sample data for S3 server ..")
        policy      <- UIO(AccessPolicy(credentials = Credentials.Allow, createBuckets = true, deleteBuckets = true, listObjects = true, getObject = true, putObject = true, copyObject = true, deleteObject = true))
        route       <- UIO(Route(PathMatch.Any, Destination.Local, Set(policy), 0, false, false))
        _           <- mongo.insert[Route]("Routes", route)
        response    =  Response.Empty()
      } yield response

  }}
}