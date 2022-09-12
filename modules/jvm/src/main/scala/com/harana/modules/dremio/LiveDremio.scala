package com.harana.modules.dremio

import com.harana.modules.dremio.Dremio.Service
import com.harana.modules.dremio.models.{Entity, _}
import com.harana.modules.core.okhttp.OkHttp
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import io.circe.{Decoder, Json}
import io.circe.syntax._
import zio.{Task, ZLayer}

object LiveDremio {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     okHttp: OkHttp.Service) => new Service {

    def jobStatus(id: JobId): Task[JobStatus] =
      for {
        _           <- logger.debug(s"Getting job status: $id")
        response    <- httpGet(s"/api/v3/job/$id")
        jobStatus   <- Task.fromTry(response.as[JobStatus].toTry)
      } yield jobStatus


    def jobResults(id: JobId, offset: Option[Int], limit: Option[Int]): Task[JobResults] =
      for {
        _           <- logger.debug(s"Getting job results: $id")
        response    <- httpGet(s"/api/v3/job/$id/results?offset=${offset.getOrElse("")}&limit=${limit.getOrElse("")}")
        jobResults  <- Task.fromTry(response.as[JobResults].toTry)
      } yield jobResults


    def cancelJob(id: JobId): Task[Unit] =
      for {
        _           <- logger.debug(s"Cancelling job: $id")
        _           <- httpPost(s"/api/v3/job/$id/cancel", None)
      } yield ()


    def getCatalog: Task[List[EntitySummary]] =
      for {
        _           <- logger.debug(s"Getting catalog")
        response    <- httpGet(s"/api/v3/catalog")
        entities    <- Task.fromTry(response.hcursor.downField("data").as[List[EntitySummary]].toTry)
      } yield entities


    def getCatalogEntity[E <: Entity](idOrPath: Either[EntityId, String])(implicit d: Decoder[E], m: Manifest[E]): Task[E] =
      for {
        _           <- logger.debug(s"Getting catalog entity: ${idOrPath.toString}")
        url         =  idOrPath match {
                          case Left(u) => s"/api/v3/catalog"
                          case Right(u) => s"/api/v3/catalog/by-path"
                        }
        json        <- httpGet(url)
        entity      <- Task.fromTry(json.as[E].toTry)
      } yield entity


    def getCatalogEntityTags(id: EntityId): Task[List[String]] =
      for {
        _           <- logger.debug(s"Getting catalog entity tags: $id≥d")
        response    <- httpGet(s"/api/v3/catalog/$id/collaboration/tag")
        tags        <- Task.fromTry(response.hcursor.downField("tags").as[List[String]].toTry)
      } yield tags


    def getCatalogEntityWiki(id: EntityId): Task[String] =
      for {
        _           <- logger.debug(s"Getting catalog entity tags: $id≥d")
        response    <- httpGet(s"/api/v3/catalog/$id/collaboration/wiki")
        wiki        <- Task.fromTry(response.hcursor.downField("text").as[String].toTry)
      } yield wiki


    def updateCatalogEntity[E <: Entity](id: EntityId, entity: E): Task[Unit] =
      for {
        _           <- logger.debug(s"Updating catalog entity: $id")

      } yield ()


    def updateCatalogEntityTags(id: EntityId, tags: List[String]): Task[Unit] =
      for {
        _           <- logger.debug(s"Updating catalog entity tags: $id")
        body        <- Task(Map("tags" -> tags).asJson.noSpaces)
        _           <- httpPost(s"/api/v3/catalog/$id/collaboration/tag", Some(body))
      } yield ()


    def updateCatalogEntityWiki(id: EntityId, text: String): Task[Unit] =
      for {
        _           <- logger.debug(s"Updating catalog entity wiki: $id")
        body        <- Task(Map("text" -> text).asJson.noSpaces)
        _           <- httpPost(s"/api/v3/catalog/$id/collaboration/wiki", Some(body))
      } yield ()


    def deleteCatalogEntity(id: EntityId): Task[Unit] =
      for {
        _           <- logger.debug(s"Deleting catalog entity: $id")
        _           <- httpDelete(s"/api/v3/catalog/$id")
      } yield ()


    def refreshCatalogEntity(id: EntityId): Task[Unit] =
      for {
        _           <- logger.debug(s"Refreshing catalog entity: $id")
        _           <- httpPost(s"/api/v3/catalog/$id/refresh", None)
      } yield ()


    def sql(sql: String): Task[JobId] =
      for {
        _           <- logger.debug(s"SQL query: $sql")
        response    <- httpPost("/api/v3/sql", Some(Map("sql" -> sql).asJson.noSpaces))
        jobId       <- Task.fromTry(response.hcursor.downField("id").as[String].toTry)
      } yield jobId


    private def getToken: Task[String] =
      for {
        username    <- config.secret("dremio-username")
        password    <- config.secret("dremio-password")
        body        <- Task(Map("username" -> username, "password" -> password).asJson.noSpaces)
        response    <- httpPost("/api/v2/login", Some(body))
        token       <- Task.fromTry(response.hcursor.downField("token").as[String].toTry)
      } yield s"_dremio{$token}"


    private def httpGet(suffix: String): Task[Json] =
      for {
        token       <- getToken
        host        <- config.secret("dremio-host")
        response    <- okHttp.getAsJson(s"http://$host$suffix", credentials = Some((token, ""))).mapError(e => new Exception(e.toString))
      } yield response


    private def httpDelete(suffix: String): Task[Json] =
      for {
        token       <- getToken
        host        <- config.secret("dremio-host")
        response    <- okHttp.deleteAsJson(s"http://$host$suffix", credentials = Some((token, ""))).mapError(e => new Exception(e.toString))
      } yield response


    private def httpPost(suffix: String, body: Option[String]): Task[Json] =
      for {
        token       <- getToken
        host        <- config.secret("dremio-host")
        response    <- okHttp.postAsJson(s"http://$host$suffix", mimeType = Some("application/json"), body = body, credentials = Some((token, ""))).mapError(e => new Exception(e.toString))
      } yield response
  }}
}