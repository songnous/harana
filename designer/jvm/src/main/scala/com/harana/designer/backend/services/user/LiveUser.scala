package com.harana.designer.backend.services.user

import com.harana.designer.backend.services.Crud
import com.harana.designer.backend.services.user.User.Service
import com.harana.modules.aws.AWS
import com.harana.id.jwt.modules.jwt.JWT
import com.harana.modules.mongo.{Mongo, convertToBson}
import com.harana.modules.vertx.models.Response
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.kubernetes.Kubernetes
import com.harana.sdk.shared.models.common.{User, UserSettings}
import com.harana.sdk.shared.models.flow.{Flow, FlowExecution}
import io.circe.syntax._
import com.harana.sdk.shared.utils.CirceCodecs._
import io.vertx.ext.web.RoutingContext
import org.mongodb.scala.bson.BsonBoolean
import zio.{Task, UIO, ZLayer}
import io.circe.yaml.parser._
import io.circe.parser.decode

object LiveUser {
  val layer = ZLayer.fromServices { (aws: AWS.Service,
                                     config: Config.Service,
                                     jwt: JWT.Service,
                                     kubernetes: Kubernetes.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service) => new Service {

    def onboard(rc: RoutingContext): Task[Response] =
      for {
        userId              <- Crud.userId(rc, config, jwt)

        sampleFlow          =  createSampleFlow(userId)
        _                   <- mongo.insert[Flow]("Flows", sampleFlow._1)
        _                   <- mongo.insert[FlowExecution]("FlowExecutions", sampleFlow._2)

        dataBucket          <- config.string("data.s3.bucket")
        sampleDataBucket    <- config.string("data.s3.sampledata.bucket")
        _                   <- aws.iamCreateS3User(s"harana-user-s3-$userId", dataBucket, userId)

        _                   <- aws.s3CopyFolder(sampleDataBucket, "data", Some(dataBucket), userId)

        _                   <- mongo.updateFields("Users", userId, Map("onboarded" -> BsonBoolean(true)))
        response            =  Response.Empty()

      } yield response


    def logout(rc: RoutingContext): Task[Response] =
      for {
        _                   <- Task(rc.response.removeCookie("jwt"))
        loginUrl            <- config.string("web.auth.loginUrl")
        response            =  Response.Redirect(loginUrl)
      } yield response


    def renewToken(rc: RoutingContext): Task[Response] =
      for {
        //        existingToken       <- ZIO.fromOption(rc.cookieMap.asScala.get("jwt"))
        response            <-  UIO(Response.Empty(cookies = List()))
      } yield response


    def preferences(rc: RoutingContext): Task[Response] =
      for {
        userId              <- Crud.userId(rc, config, jwt)
        preferences         <- mongo.findEquals[User]("Users", Map("id" -> userId)).map(_.head.preferences)
        response            =  Response.JSON(preferences.asJson)
    } yield response


    def savePreferences(rc: RoutingContext): Task[Response] =
      for {
        userId              <- Crud.userId(rc, config, jwt)
        preferences         <- Task.fromEither(decode[Map[String, String]](rc.body().asString))
        _                   <- mongo.updateFields("Users", userId, Map("preferences" -> preferences))
        response            =  Response.Empty()
      } yield response


    def settings(rc: RoutingContext): Task[Response] =
      for {
        userId              <- Crud.userId(rc, config, jwt)
        settings            <- mongo.findEquals[User]("Users", Map("id" -> userId)).map(_.head.settings)
        response            =  Response.JSON(settings.asJson)
      } yield response


    def saveSettings(rc: RoutingContext): Task[Response] =
      for {
        userId              <- Crud.userId(rc, config, jwt)
        settings            <- Task.fromEither(decode[UserSettings](rc.body().asString()))
        bson                <- convertToBson(settings)
        _                   <- mongo.updateFields("Users", userId, Map("settings" -> bson))
        response            =  Response.Empty()
      } yield response


    def startSession(rc: RoutingContext): Task[Response] =
      for {
        appsNamespace             <- config.string("apps.namespace")
        filesharingNamespace      <- config.string("filesharing.namespace")
        terminalNamespace         <- config.string("terminal.namespace")
      } yield null


    def extendSession(rc: RoutingContext): Task[Response] =
      null


    def testLogs(rc: RoutingContext): Task[Response] =
      Task(
        Response.Content(
          "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n"
        )
      )
  }}
}