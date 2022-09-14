package com.harana.designer.backend.services.data

import com.harana.designer.backend.services.Crud
import com.harana.designer.backend.services.data.Data.Service
import com.harana.modules.alluxiofs.AlluxioFs
import com.harana.id.jwt.modules.jwt.JWT
import com.harana.modules.vertx.Vertx
import com.harana.modules.vertx.models.Response
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.id.jwt.shared.models.DesignerClaims
import io.circe.syntax._
import com.harana.sdk.shared.utils.CirceCodecs._
import io.vertx.ext.web.RoutingContext
import zio.{Task, ZIO, ZLayer}

import scala.jdk.CollectionConverters._

object LiveData {
  val layer = ZLayer.fromServices { (alluxio: AlluxioFs.Service,
                                     config: Config.Service,
                                     jwt: JWT.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     vertx: Vertx.Service) => new Service {

    def list(rc: RoutingContext): Task[Response] =
      for {
        userId          <- Crud.userId(rc, config, jwt)
        uri             <- uri(userId, rc.queryParam("path").asScala.head)
        files           <- alluxio.list(uri)
        response        =  Response.JSON(files.asJson)
      } yield response


    def tags(rc: RoutingContext): Task[Response] = {
      null
    }

    def search(rc: RoutingContext): Task[Response] =
      for {
        userId          <- Crud.userId(rc, config, jwt)
        uri             <- uri(userId, rc.queryParam("path").asScala.head)
        query           <- Task(rc.pathParam("query"))
//        files           <- vfs.search(uri, query)
//        response        =  Response.JSON(files.asJson)
        response        =  Response.Empty()
      } yield response


    def info(rc: RoutingContext): Task[Response] =
      for {
        userId          <- Crud.userId(rc, config, jwt)
        uri             <- uri(userId, rc.queryParam("path").asScala.head)
//        file            <- vfs.info(uri)
//        response        =  Response.JSON(file.asJson)
        response        =  Response.Empty()
      } yield response


    def update(rc: RoutingContext): Task[Response] = {
      null
    }


    def copy(rc: RoutingContext): Task[Response] =
      null


    def move(rc: RoutingContext): Task[Response] =
      for {
        userId          <- Crud.userId(rc, config, jwt)
        toUri           <- uri(userId, rc.queryParam("toPath").asScala.head)
        toUri           <- uri(userId, rc.queryParam("toPath").asScala.head)
//        _               <- vfs.move(fromUri, toUri)
        response        =  Response.Empty()
      } yield response


    def delete(rc: RoutingContext): Task[Response] =
      for {
        userId          <- Crud.userId(rc, config, jwt)
        uri             <- uri(userId, rc.queryParam("path").asScala.head)
//        _               <- vfs.delete(uri)
        response        =  Response.Empty()
      } yield response


    def createDirectory(rc: RoutingContext): Task[Response] =
      for {
        userId          <- Crud.userId(rc, config, jwt)
        uri             <- uri(userId, rc.queryParam("path").asScala.head)
  //      _               <- vfs.mkdir(uri)
        response        =  Response.Empty()
      } yield response


    def download(rc: RoutingContext): Task[Response] =
      for {
        userId          <- Crud.userId(rc, config, jwt)
        uri             <- uri(userId, rc.queryParam("path").asScala.head)
//        file            <- vfs.info(uri)
//        inputStream     <- vfs.read(uri)
//        response        =  Response.File(file.name, inputStream, Some(file.size))
        response        =  Response.Empty()
      } yield response


    def upload(rc: RoutingContext): Task[Response] =
      for {
        userId          <- Crud.userId(rc, config, jwt)
        path            =  rc.queryParam("path").asScala.head
        uploads         =  rc.fileUploads().asScala
        _               <- ZIO.foreach_(uploads)(upload =>
                              for {
                                buffer   <- vertx.getUploadedFile(upload.uploadedFileName())
                                path     <- uri(userId, s"$path${upload.fileName()}")
//                                _        <- vfs.write(path, new ByteArrayInputStream(buffer.getBytes()))
                              } yield ()
                            )
        response        =  Response.Empty()
      } yield response


    private def uri(userId: String, path: String) =
      config.string("data.path").map(prefix => s"local://$prefix/$userId$path")
  }}
}