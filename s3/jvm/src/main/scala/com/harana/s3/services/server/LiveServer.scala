package com.harana.s3.services.server

import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.vertx.Vertx
import com.harana.modules.vertx.models.Response
import com.harana.s3.services.router.Router
import com.harana.s3.services.server.models.AwsHttpHeaders
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpMethod._
import io.vertx.core.streams.Pump
import io.vertx.ext.reactivestreams.ReactiveWriteStream
import io.vertx.ext.web.RoutingContext
import zio.clock.Clock
import zio.{Task, ZLayer}

object LiveServer {
  val layer = ZLayer.fromServices { (clock: Clock.Service,
                                     config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     router: Router.Service,
                                     vertx: Vertx.Service) => new Server.Service {

      def handle(rc: RoutingContext, method: HttpMethod): Task[Response] = {
        val r = rc.request()

        val path = r.uri().split("/", 3)
        val uploadId = r.getParam("uploadId")

        method match {

          case DELETE if path.length <= 2 && path(2).isEmpty =>
            router.deleteBucket(path(1))

          case DELETE if uploadId != null =>
            router.abortMultipartUpload(path(1), path(2), uploadId)

          case DELETE =>
            router.deleteObject(path(1), path(2))

          case GET if r.uri().equals("/") =>
            router.listBuckets()

          case GET if path.length <= 2 && path(2).isEmpty =>
            if (r.getParam("acl") != null)
              router.getBucketAcl(path(1))

            else if (r.getParam("location") != null) {
              // handleContainerLocation(response)
            }

            else if (r.getParam("policy") != null)
              router.getBucketPolicy(path(1))

            else if (r.getParam("uploads") != null)
              router.listMultipartUploads(path(1))

            router.listObjects(path(1))

          case GET =>
            if (r.getParam("acl") != null)
              router.getObjectAcl(path(1), path(2))
            else if (uploadId != null)
              router.listParts(path(1), path(2), uploadId)

            router.getObject(path(1), path(2)).map(Response.ReadStream(_))

          case HEAD if path.length <= 2 && path(2).isEmpty =>
            router.bucketExists(path(1))

          case HEAD =>
            router.getObjectMetadata(path(1), path(2))

          case POST if r.getParam("delete") != null =>
            router.deleteObjects(path(1))

          case POST if r.getParam("uploads") != null =>
            router.createMultipartUpload(path(1), path(2))

          case POST if uploadId != null && r.getParam("partNumber") == null =>
            router.completeMultipartUpload(path(1), path(2), uploadId, is)

          case PUT if path.length <= 2 && path(2).isEmpty =>
            if (r.getParam("acl") != null)
              router.putBucketAcl(path[1], is)
            else
              router.createBucket(path(1))

          case PUT if uploadId != null =>
            if (r.getHeader(AwsHttpHeaders.COPY_SOURCE.value) != null)
              router.uploadPartCopy(path(1), path(2), uploadId)
            else
              vertx.withUploadStream(rc, router.uploadPart(path(1), path(2), uploadId, _)).as(Response.Empty())

          case PUT if r.getHeader(AwsHttpHeaders.COPY_SOURCE.value) != null =>
            router.copyObject(path(1), path(2))

          case PUT =>
            if (r.getParam("acl") != null)
              router.putObjectAcl(path(1), path(2), is)

            router.putObject(path(1), path(2), is)

          case OPTIONS =>
            router.getItemOptions(path(1))
        }
      }
    }
  }
}