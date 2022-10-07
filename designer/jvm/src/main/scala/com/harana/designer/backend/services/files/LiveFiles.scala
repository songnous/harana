package com.harana.designer.backend.services.files

import com.harana.designer.backend.services.Crud
import com.harana.designer.backend.services.files.Files.Service
import com.harana.id.jwt.modules.jwt.JWT
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.kubernetes.Kubernetes
import com.harana.modules.vertx.Vertx
import com.harana.modules.vertx.models.Response
import com.harana.modules.vfs.Vfs
import com.harana.sdk.shared.models.jwt.DesignerClaims
import com.harana.shared.models.HaranaFile
import io.circe.parser.decode
import io.circe.syntax._
import io.vertx.ext.web.RoutingContext
import zio.{Task, ZIO, ZLayer}

import java.io.ByteArrayInputStream
import scala.jdk.CollectionConverters._

object LiveFiles {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                     jwt: JWT.Service,
                                     kubernetes: Kubernetes.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     vertx: Vertx.Service,
                                     vfs: Vfs.Service) => new Service {

    def list(rc: RoutingContext): Task[Response] =
      for {
        userId          <- Crud.userId(rc, config, jwt)
        basePath        <- basePath(userId)
        uri             <- uri(userId, rc.queryParam("path").asScala.head)
        files           <- vfs.list(uri)
        updatedFiles    =  files.map(f => f.copy(path = f.path.replace(basePath, "")))
        response        =  Response.JSON(updatedFiles.asJson)
      } yield response


    def tags(rc: RoutingContext): Task[Response] = {
      null
    }

    def search(rc: RoutingContext): Task[Response] =
      for {
        userId          <- Crud.userId(rc, config, jwt)
        basePath        <- basePath(userId)
        uri             <- uri(userId, rc.queryParam("path").asScala.head)
        query           <- Task(rc.pathParam("query"))
        files           <- vfs.search(uri, query)
        updatedFiles    =  files.map(f => f.copy(path = f.path.replace(basePath, "")))
        response        =  Response.JSON(updatedFiles.asJson)
      } yield response


    def info(rc: RoutingContext): Task[Response] =
      for {
        userId          <- Crud.userId(rc, config, jwt)
        basePath        <- basePath(userId)
        uri             <- uri(userId, rc.queryParam("path").asScala.head)
        file            <- vfs.info(uri)
        updatedFile     =  file.copy(path = file.path.replace(basePath, ""))
        response        =  Response.JSON(updatedFile.asJson)
      } yield response


    def updateInfo(rc: RoutingContext): Task[Response] =
      for {
        userId          <- Crud.userId(rc, config, jwt)
        file            <- Task.fromEither(decode[HaranaFile](rc.getBodyAsString))
        uri             <- uri(userId, rc.queryParam("path").asScala.head)
        _               <- vfs.rename(uri, file.name)
        response        =  Response.Empty()
      } yield response


    def copy(rc: RoutingContext): Task[Response] =
      for {
        userId          <- Crud.userId(rc, config, jwt)
        fromUri         <- uri(userId, rc.queryParam("fromPath").asScala.head)
        toUri           <- uri(userId, rc.queryParam("toPath").asScala.head)
        _               <- vfs.copy(fromUri, toUri)
        response        =  Response.Empty()
      } yield response


    def move(rc: RoutingContext): Task[Response] =
      for {
        userId          <- Crud.userId(rc, config, jwt)
        fromUri         <- uri(userId, rc.queryParam("fromPath").asScala.head)
        toUri           <- uri(userId, rc.queryParam("toPath").asScala.head)
        _               <- vfs.move(fromUri, toUri)
        response        =  Response.Empty()
      } yield response


    def duplicate(rc: RoutingContext): Task[Response] =
      for {
        userId          <- Crud.userId(rc, config, jwt)
        uri             <- uri(userId, rc.queryParam("path").asScala.head)
        _               <- vfs.duplicate(uri)
        response        =  Response.Empty()
      } yield response


    def delete(rc: RoutingContext): Task[Response] =
      for {
        userId          <- Crud.userId(rc, config, jwt)
        uri             <- uri(userId, rc.queryParam("path").asScala.head)
        _               <- vfs.delete(uri)
        response        =  Response.Empty()
      } yield response


    def createDirectory(rc: RoutingContext): Task[Response] =
      for {
        userId          <- jwt.claims[DesignerClaims](rc).map(_.userId)
        uri             <- uri(userId, rc.queryParam("path").asScala.head)
        _               <- vfs.mkdir(uri)
        response        =  Response.Empty()
      } yield response


    def download(rc: RoutingContext): Task[Response] =
      for {
        userId          <- jwt.claims[DesignerClaims](rc).map(_.userId)
        uri             <- uri(userId, rc.queryParam("path").asScala.head)
        file            <- vfs.info(uri)
        inputStream     <- vfs.read(uri)
        response        =  Response.File(file.name, inputStream, false, Some(file.size))
      } yield response


    def preview(rc: RoutingContext): Task[Response] =
      for {
        userId          <- jwt.claims[DesignerClaims](rc).map(_.userId)
        uri             <- uri(userId, rc.queryParam("path").asScala.head)
        fileInfo        <- vfs.info(uri)
        file            <- vfs.underlyingFile(uri)
        maximumRows     <- config.int("preview.maximumRows", 50)
        response        <- fileInfo.`extension` match {
                            case Some("avro") => previewAvro(file, maximumRows).map(pd => Response.JSON(pd.asJson))
                            case Some("csv") => previewCsv(file, maximumRows).map(pd => Response.JSON(pd.asJson))
                            case Some("parquet") => previewParquet(file, maximumRows).map(pd => Response.JSON(pd.asJson))
                            case _ => vfs.read(uri).map(is => Response.InputStream(is, false, Some(fileInfo.size)))
                        }
      } yield response


    def upload(rc: RoutingContext): Task[Response] =
      for {
        userId          <- jwt.claims[DesignerClaims](rc).map(_.userId)
        path            =  rc.queryParam("path").asScala.head
        uploads         =  rc.fileUploads().asScala
        _               <- ZIO.foreach_(uploads)(upload =>
                              for {
                                buffer   <- vertx.getUploadedFile(upload.uploadedFileName())
                                path     <- uri(userId, s"$path${upload.fileName()}")
                                _        <- vfs.write(path, new ByteArrayInputStream(buffer.getBytes()))
                              } yield ()
                            )
        response        =  Response.Empty()
      } yield response


    def compress(rc: RoutingContext): Task[Response] =
      for {
        userId          <- jwt.claims[DesignerClaims](rc).map(_.userId)
        uri             <- uri(userId, rc.queryParam("path").asScala.head)
        _               <- vfs.compress(uri)
        response        =  Response.Empty()
      } yield response


    def decompress(rc: RoutingContext): Task[Response] =
      for {
        userId          <- jwt.claims[DesignerClaims](rc).map(_.userId)
        uri             <- uri(userId, rc.queryParam("path").asScala.head)
        _               <- vfs.decompress(uri)
        response        =  Response.Empty()
      } yield response


    def startFileSharing(rc: RoutingContext): Task[Response] =
      null

    def stopFileSharing(rc: RoutingContext): Task[Response] =
      null

    def isFileSharingEnabled(rc: RoutingContext): Task[Response] =
      null

    def startRemoteLogin(rc: RoutingContext): Task[Response] =
      null

    def stopRemoteLogin(rc: RoutingContext): Task[Response] =
      null

    def isRemoteLoginEnabled(rc: RoutingContext): Task[Response] =
      null

    private def uri(userId: String, path: String) =
      basePath(userId).map(p => s"local://$p$path")

    private def basePath(userId: String) =
      config.string("data.path").map(prefix => s"$prefix/$userId")

  }}
}