package com.harana.id.services.ssh

import com.harana.id.services.ssh.SSH.Service
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.mongo.Mongo
import com.harana.modules.vertx.models.Response
import com.harana.sdk.shared.models.common.User
import io.circe.parser.decode
import io.circe.syntax._
import io.vertx.ext.web.RoutingContext
import zio.{Task, ZLayer}

import java.nio.charset.StandardCharsets
import java.util.Base64

object LiveSSH {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service) => new Service {

    private val decoder = Base64.getDecoder
    private val success = Map("success" -> "true").asJson

    def authenticatePassword(rc: RoutingContext): Task[Response] =
      for {
        entity                  <- Task.fromEither(decode[PasswordRequest](rc.body().asString))
        password                <- Task(new String(decoder.decode(entity.passwordBase64), StandardCharsets.UTF_8))
        user                    <- mongo.findOne[User]("Users", Map("emailAddress" -> entity.username, "password" -> Some(password)))
        response                =  if (user.isDefined) Response.JSON(success) else Response.Empty(statusCode = Some(401))
      } yield response


    def authenticatePublicKey(rc: RoutingContext): Task[Response] =
      for {
        entity                  <- Task.fromEither(decode[PublicKeyRequest](rc.body().asString))
        user                    <- mongo.findOne[User]("Users", Map("emailAddress" -> entity.username, "publicKey" -> Some(entity.publicKey)))
        response                =  if (user.isDefined) Response.JSON(success) else Response.Empty(statusCode = Some(401))
      } yield response


    def configuration(rc: RoutingContext): Task[Response] =
      for {
        entity                  <- Task.fromEither(decode[ConfigurationRequest](rc.body().asString))
        user                    <- mongo.findOne[User]("Users", Map("emailAddress" -> entity.username))
        response                = if (user.isDefined) Response.JSON(success) else Response.Empty(statusCode = Some(401))
      } yield response

  }}
}