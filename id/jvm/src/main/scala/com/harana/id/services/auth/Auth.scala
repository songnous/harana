package com.harana.id.services.auth

import com.harana.modules.vertx.models.Response
import com.harana.sdk.shared.models.common.User
import io.vertx.ext.web.RoutingContext
import org.pac4j.core.profile.CommonProfile
import org.pac4j.vertx.VertxProfileManager
import zio.macros.accessible
import zio.{Has, Task}
import io.vertx.core.http.Cookie

@accessible
object Auth {
  type Auth = Has[Auth.Service]

  trait Service {
    def logout(rc: RoutingContext): Task[Response]

    def redirectToApp(user: User, profile: Option[CommonProfile]): Task[Response]

    def resetPassword(rc: RoutingContext): Task[Response]

    def renewToken(rc: RoutingContext): Task[Response]

    def signup(rc: RoutingContext): Task[Response]

    def signupOrRedirect(rc: RoutingContext, profile: Option[CommonProfile]): Task[Response]

    def resendConfirmation(rc: RoutingContext): Task[Response]

    def pauseAccount(rc: RoutingContext): Task[Response]

    def cancelAccount(rc: RoutingContext): Task[Response]
  }
}