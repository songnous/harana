package com.harana.designer.backend.services

import com.harana.id.jwt.shared.models.DesignerClaims
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.id.jwt.modules.jwt.JWT
import com.harana.modules.core.config.Config
import com.harana.modules.mongo.Mongo
import com.harana.modules.vertx.models.Response
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common.{Entity, Visibility}
import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import io.vertx.ext.web.RoutingContext
import org.mongodb.scala.bson.Document
import zio.{Task, UIO}

import java.time.Instant
import scala.collection.JavaConverters._
import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

object Crud {

  def userId(rc: RoutingContext, config: Config.Service, jwt: JWT.Service): Task[String] =
    for {
      authToken <- UIO(Option(rc.request().headers().get("auth-token")))
      userId    <- if (authToken.isDefined)
                      for {
                        secretToken  <- config.secret("harana-token")
                        _            <- Task.fail(new Exception("Auth token != Secret")).unless(authToken.get == secretToken)
                        headerUserId <- Task.getOrFail(Option(rc.request().headers().get("user-id")))
                      } yield headerUserId
                    else
                      jwt.claims[DesignerClaims](rc).map(_.userId)
    } yield userId


  def owners[E <: Entity](collection: String,
                          rc: RoutingContext,
                          config: Config.Service,
                          jwt: JWT.Service,
                          logger: Logger.Service,
                          micrometer: Micrometer.Service,
                          mongo: Mongo.Service)(implicit ct: ClassTag[E], tt: TypeTag[E], d: Decoder[E], e: Encoder[E]): Task[Map[String, Int]] =
    for {
      userId            <- userId(rc, config, jwt)

      filterStage       =  Document("$match" -> Document("$or" -> creatorOrPublic(userId)))
      projectStage      =  Document("$project" -> Document("tags" -> 1))
      unwindStage       =  Document("$unwind" -> Document("path" -> "$tags"))
      groupStage        =  Document("$group" -> Document("_id" -> "$tags", "count" -> Document("$sum" -> 1)))

      entities          <- mongo.aggregate(collection, List(filterStage, projectStage, unwindStage, groupStage))
      tags              =  entities.map(e => e.get("_id").asString().getValue -> e.get("count").asNumber().intValue).toMap
    } yield tags


  def ownersResponse[E <: Entity](collection: String,
                                  rc: RoutingContext,
                                  config: Config.Service,
                                  jwt: JWT.Service,
                                  logger: Logger.Service, micrometer: Micrometer.Service, mongo: Mongo.Service)(implicit ct: ClassTag[E], tt: TypeTag[E], d: Decoder[E], e: Encoder[E]): Task[Response] =
    owners(collection, rc, config, jwt, logger, micrometer, mongo).map(r => Response.JSON(r.asJson))


  def tags[E <: Entity](collection: String,
                        rc: RoutingContext,
                        config: Config.Service,
                        jwt: JWT.Service,
                        logger: Logger.Service,
                        micrometer: Micrometer.Service,
                        mongo: Mongo.Service)(implicit ct: ClassTag[E], tt: TypeTag[E], d: Decoder[E], e: Encoder[E]): Task[Map[String, Int]] =
    for {
      userId            <- userId(rc, config, jwt)

      filterStage       =  Document("$match" -> Document("$or" -> creatorOrPublic(userId)))
      projectStage      =  Document("$project" -> Document("tags" -> 1))
      unwindStage       =  Document("$unwind" -> Document("path" -> "$tags"))
      groupStage        =  Document("$group" -> Document("_id" -> "$tags", "count" -> Document("$sum" -> 1)))

      entities          <- mongo.aggregate(collection, List(filterStage, projectStage, unwindStage, groupStage))
      tags              =  entities.map(e => e.get("_id").asString().getValue -> e.get("count").asNumber().intValue).toMap
    } yield tags


  def tagsResponse[E <: Entity](collection: String,
                                rc: RoutingContext,
                                config: Config.Service,
                                jwt: JWT.Service,
                                logger: Logger.Service,
                                micrometer: Micrometer.Service,
                                mongo: Mongo.Service)(implicit ct: ClassTag[E], tt: TypeTag[E], d: Decoder[E], e: Encoder[E]): Task[Response] =
    tags(collection, rc, config, jwt, logger, micrometer, mongo).map(r => Response.JSON(r.asJson))


  def list[E <: Entity](collection: String,
                        rc: RoutingContext,
                        config: Config.Service,
                        jwt: JWT.Service,
                        logger: Logger.Service,
                        micrometer: Micrometer.Service,
                        mongo: Mongo.Service)(implicit ct: ClassTag[E], tt: TypeTag[E], d: Decoder[E], e: Encoder[E]): Task[List[E]] =
    for {
      tag               <- Task(rc.queryParam("tag").asScala.headOption)
      userId            <- userId(rc, config, jwt)

      filter            <- UIO(if (tag.isDefined) Map("$or" -> creatorOrPublic(userId), "tags" -> tag.get) else Map("$or" -> creatorOrPublic(userId)))
      entities          <- mongo.findEquals[E](collection, filter).onError(e => logger.error(e.prettyPrint))

    } yield entities


  def listResponse[E <: Entity](collection: String,
                                rc: RoutingContext,
                                config: Config.Service,
                                jwt: JWT.Service,
                                logger: Logger.Service,
                                micrometer: Micrometer.Service,
                                mongo: Mongo.Service)(implicit ct: ClassTag[E], tt: TypeTag[E], d: Decoder[E], e: Encoder[E]): Task[Response] =
    list(collection, rc, config, jwt, logger, micrometer, mongo).map(r => Response.JSON(r.asJson))


  def search[E <: Entity](collection: String,
                          rc: RoutingContext,
                          config: Config.Service,
                          jwt: JWT.Service,
                          logger: Logger.Service,
                          micrometer: Micrometer.Service,
                          mongo: Mongo.Service)(implicit ct: ClassTag[E], tt: TypeTag[E], d: Decoder[E], e: Encoder[E]): Task[List[E]] =
    for {
      query             <- Task(rc.pathParam("query"))
      tag               <- Task(rc.queryParam("tag").asScala.headOption)

      userId            <- userId(rc, config, jwt)
      entityName        =  ct.runtimeClass.getSimpleName

      filter            <- UIO(if (tag.isDefined) Map("$or" -> creatorOrPublic(userId), "tags" -> tag.get) else Map("$or" -> creatorOrPublic(userId)))
      entities          <- mongo.textSearchFindEquals[E](collection, query, filter).onError(e => logger.error(e.prettyPrint))

      _                 <- logger.debug(s"Search $entityName. Found: ${entities.size}")
    } yield entities


  def searchResponse[E <: Entity](collection: String,
                                  rc: RoutingContext,
                                  config: Config.Service,
                                  jwt: JWT.Service,
                                  logger: Logger.Service,
                                  micrometer: Micrometer.Service,
                                  mongo: Mongo.Service)(implicit ct: ClassTag[E], tt: TypeTag[E], d: Decoder[E], e: Encoder[E]): Task[Response] =
    search(collection, rc, config, jwt, logger, micrometer, mongo).map(r => Response.JSON(r.asJson))


  def get[E <: Entity](collection: String,
                       rc: RoutingContext,
                       config: Config.Service,
                       jwt: JWT.Service,
                       logger: Logger.Service,
                       micrometer: Micrometer.Service,
                       mongo: Mongo.Service)(implicit ct: ClassTag[E], tt: TypeTag[E], d: Decoder[E], e: Encoder[E]): Task[Option[E]] =
    for {
      id                <- Task(rc.pathParam("id"))
      userId            <- userId(rc, config, jwt)
      entityName        =  ct.runtimeClass.getSimpleName

      entity            <- mongo.findEquals[E](collection, Map("id" -> id, "$or" -> creatorOrPublic(userId))).map(_.headOption)
      _                 <- logger.debug(s"Get $entityName with id: $id")
    } yield entity


  def getResponse[E <: Entity](collection: String,
                               rc: RoutingContext,
                               config: Config.Service,
                               jwt: JWT.Service,
                               logger: Logger.Service,
                               micrometer: Micrometer.Service,
                               mongo: Mongo.Service)(implicit ct: ClassTag[E], tt: TypeTag[E], d: Decoder[E], e: Encoder[E]): Task[Response] =
    get(collection, rc, config, jwt, logger, micrometer, mongo).map(r => Response.JSON(r.asJson))


  def delete[E <: Entity](collection: String,
                          rc: RoutingContext,
                          config: Config.Service,
                          jwt: JWT.Service,
                          logger: Logger.Service,
                          micrometer: Micrometer.Service,
                          mongo: Mongo.Service)(implicit ct: ClassTag[E], tt: TypeTag[E], d: Decoder[E]): Task[Unit] =
    for {
      id                <- Task(rc.pathParam("id"))
      userId            <- userId(rc, config, jwt)
      entityName        =  ct.runtimeClass.getSimpleName

      _                 <- mongo.deleteEquals[E](collection, Map("id" -> id, "createdBy" -> userId))
      _                 <- logger.debug(s"Delete $entityName with id: $id")
    } yield ()


  def deleteResponse[E <: Entity](collection: String,
                                  rc: RoutingContext,
                                  config: Config.Service,
                                  jwt: JWT.Service,
                                  logger: Logger.Service,
                                  micrometer: Micrometer.Service,
                                  mongo: Mongo.Service)(implicit ct: ClassTag[E], tt: TypeTag[E], d: Decoder[E]): Task[Response] =
    delete(collection, rc, config, jwt, logger, micrometer, mongo).as(Response.Empty())


  def create[E <: Entity](collection: String,
                          rc: RoutingContext,
                          config: Config.Service,
                          jwt: JWT.Service,
                          logger: Logger.Service,
                          micrometer: Micrometer.Service,
                          mongo: Mongo.Service)(implicit ct: ClassTag[E], tt: TypeTag[E], d: Decoder[E], e: Encoder[E]): Task[E] =
    for {
      userId            <- userId(rc, config, jwt)
      entityName        =  ct.runtimeClass.getSimpleName
      entityJson        <- Task(rc.getBodyAsJson)
      isValid           <- Task(entityJson.getValue("createdBy").equals(userId))

      entity            <- Task.fromEither(decode[E](rc.getBodyAsString))
      _                 <- mongo.insert[E](collection, entity).when(isValid)
      _                 <- logger.debug(s"Insert $entityName")
    } yield entity


  def createResponse[E <: Entity](collection: String,
                                  rc: RoutingContext,
                                  config: Config.Service,
                                  jwt: JWT.Service,
                                  logger: Logger.Service,
                                  micrometer: Micrometer.Service,
                                  mongo: Mongo.Service)(implicit ct: ClassTag[E], tt: TypeTag[E], d: Decoder[E], e: Encoder[E]): Task[Response] =
    create(collection, rc, config, jwt, logger, micrometer, mongo).as(Response.Empty())


  def update[E <: Entity](collection: String,
                          rc: RoutingContext,
                          config: Config.Service,
                          jwt: JWT.Service,
                          logger: Logger.Service,
                          micrometer: Micrometer.Service,
                          mongo: Mongo.Service)(implicit ct: ClassTag[E], tt: TypeTag[E], d: Decoder[E], e: Encoder[E]): Task[E] =
    for {
      userId            <- userId(rc, config, jwt)
      entityName        =  ct.runtimeClass.getSimpleName
      entityJson        <- Task(rc.getBodyAsJson)
      isValid           <- Task(entityJson.getValue("createdBy").equals(userId))
      _                 =  entityJson.put("updated", Instant.now)
      entity            <- Task.fromEither(decode[E](entityJson.toString))
      _                 <- logger.debug(entityJson.toString)
      _                 <- mongo.update[E](collection, entity).when(isValid)
      _                 <- logger.debug(s"Update $entityName")
    } yield entity


  def updateResponse[E <: Entity](collection: String,
                                  rc: RoutingContext,
                                  config: Config.Service,
                                  jwt: JWT.Service,
                                  logger: Logger.Service,
                                  micrometer: Micrometer.Service,
                                  mongo: Mongo.Service)(implicit ct: ClassTag[E], tt: TypeTag[E], d: Decoder[E], e: Encoder[E]): Task[Response] =
    update(collection, rc, config, jwt, logger, micrometer, mongo).as(Response.Empty())


  private def creatorOrPublic(userId: UserId) = {
    val isPublic = Document("visibility" -> Visibility.Public.toString)
    val isCreator = Document("createdBy" -> userId)
    List(isPublic, isCreator)
  }
}