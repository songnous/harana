package com.harana.id

import java.util.Optional
import com.harana.Layers
import com.harana.id.jwt.modules.jwt.JWT
import com.harana.id.jwt.{Layers => JWTLayers}
import com.harana.id.services.auth.{LiveAuth, Auth}
import com.harana.id.services.billing.{Billing, LiveBilling}
import com.harana.id.utils.HashUtils
import com.harana.modules.mongo.Mongo
import com.harana.modules.vertx.Vertx
import com.harana.modules.vertx.models.{AuthType, Response, Route}
import com.harana.sdk.shared.models.common.User
import com.harana.modules.core.app.{App => CoreApp}
import com.harana.modules.core.config.Config
import com.harana.modules.core.{Layers => CoreLayers}
import io.vertx.core.http.HttpMethod
import org.apache.logging.log4j.LogManager
import org.jose4j.jwk.JsonWebKeySet
import org.pac4j.core.context.WebContext
import org.pac4j.core.context.session.SessionStore
import org.pac4j.core.credentials.{Credentials, UsernamePasswordCredentials}
import org.pac4j.core.credentials.authenticator.Authenticator
import org.pac4j.core.exception.CredentialsException
import org.pac4j.core.profile.{CommonProfile, UserProfile}
import org.pac4j.core.profile.creator.ProfileCreator
import org.pac4j.core.profile.definition.CommonProfileDefinition
import org.pac4j.oauth.client.Google2Client.Google2Scope
import zio.internal.Platform
import zio.{Runtime, Task, UIO, ZIO}

object App extends CoreApp {

  val auth = (Layers.airtable ++ Layers.clearbit ++ Layers.email ++ Layers.handlebars ++ JWTLayers.jwt ++ CoreLayers.standard ++ Layers.mongo ++ Layers.stripeCustomers ++ Layers.stripePrices ++ Layers.stripeProducts ++ Layers.stripeSubscriptions ++ Layers.vertx) >>> LiveAuth.layer
  val billing = (CoreLayers.standard ++ JWTLayers.jwt ++ auth ++ Layers.mongo ++ Layers.stripeCustomers ++ Layers.stripeUI ++ Layers.vertx) >>> LiveBilling.layer
  val logger = LogManager.getLogger("App")
  val runtime = Runtime[Unit]((), Platform.default.withReportFailure(cause => if (!cause.interrupted) logger.error(cause.prettyPrint)))
  
  private def routes = List(
    Route("/", HttpMethod.GET, _ => Task(Response.Template("public/index.hbs")), isSecured = true),
    Route("/logout", HttpMethod.GET, rc => Auth.logout(rc).provideLayer(auth)),
    Route("/token/renew", HttpMethod.GET, rc => Auth.renewToken(rc).provideLayer(auth)),

    Route("/account/signup", HttpMethod.POST, rc => Auth.signup(rc).provideLayer(auth)),
    Route("/account/pause", HttpMethod.GET, rc => Task(Response.Template("public/templates/index.html"))),
    Route("/account/cancel", HttpMethod.GET, rc => Task(Response.Template("public/templates/index.html"))),
    Route("/account/resetPassword", HttpMethod.GET, rc => Task(Response.Template("public/templates/index.html"))),

    Route("/billing/checkout", HttpMethod.GET, rc => Billing.checkout(rc).provideLayer(billing)),
    Route("/billing/checkout/success/:sessionId", HttpMethod.GET, rc => Billing.checkoutSuccess(rc).provideLayer(billing)),
    Route("/billing/portal", HttpMethod.GET, rc => Billing.portal(rc).provideLayer(billing)),

    Route("/billing/subscription/webhook", HttpMethod.POST, rc => Billing.subscriptionWebhook(rc).provideLayer(billing)),
  )


  def startup: Task[Unit] =
    for {
      domain                <- Config.env("harana_domain").provideLayer(CoreLayers.config)
      googleKey             <- Config.secret("google-oauth-key").provideLayer(CoreLayers.config)
      googleSecret          <- Config.secret("google-oauth-secret").provideLayer(CoreLayers.config)
      googleScope           <- Config.string("auth.google.scope", Google2Scope.EMAIL_AND_PROFILE.name).provideLayer(CoreLayers.config)
      googleAuthType        =  AuthType.Google(googleKey, googleSecret, Some(Google2Scope.valueOf(googleScope)))
      formAuthType          =  AuthType.Form("https://harana.com/login", new MongoAuthenticator, new MongoProfileCreator)
      authTypes             =  List(googleAuthType, formAuthType)

      jwtKey                <- JWT.key.provideLayer(JWTLayers.jwt)
      _                     <- Vertx.startHttpServer(
                                s"id.$domain",
                                None,
                                routes,
                                authTypes = authTypes,
                                additionalAllowedHeaders = Set("jwt"),
                                jwtKeySet = Some(new JsonWebKeySet(jwtKey)),
                                postLogin = Some((rc, profile) => Auth.signupOrRedirect(rc, profile.map(_.asInstanceOf[CommonProfile])).provideLayer(auth))
                               ).provideLayer(Layers.vertx).toManaged_.useForever
    } yield ()


  def shutdown: Task[Unit] = {
    for {
      _                     <- Vertx.close.provideLayer(Layers.vertx)
    } yield ()
  }

  class MongoAuthenticator extends Authenticator {
    def validate(credentials: Credentials, context: WebContext, store: SessionStore): Unit = {
      val upCredentials = credentials.asInstanceOf[UsernamePasswordCredentials]
      val password = HashUtils.hash(upCredentials.getPassword, upCredentials.getUsername.reverse)
      val find = Mongo.countEquals("Users", Map("password" -> password.toString)).provideLayer(Layers.mongo)
      if (runtime.unsafeRun(find) == 0) throw new CredentialsException("No user found")
    }
  }

  class MongoProfileCreator extends ProfileCreator {
    def create(credentials: Credentials, context: WebContext, store: SessionStore): Optional[UserProfile] = {
      val find = Mongo.findOne[User]("Users", Map("emailAddress"-> credentials.asInstanceOf[UsernamePasswordCredentials].getUsername)).provideLayer(Layers.mongo)

      runtime.unsafeRun(find) match {
        case Some(user) =>
          val commonProfile = new CommonProfile()
          commonProfile.setId(user.id)
          commonProfile.addAttribute(CommonProfileDefinition.FIRST_NAME, user.firstName)
          commonProfile.addAttribute(CommonProfileDefinition.FAMILY_NAME, user.lastName)
          commonProfile.addAttribute(CommonProfileDefinition.EMAIL, user.emailAddress)
          commonProfile.addAttribute(CommonProfileDefinition.LOCALE, "en")
          if (user.displayName.isDefined) commonProfile.addAttribute(CommonProfileDefinition.DISPLAY_NAME, user.displayName.get)
          Optional.of(commonProfile)

        case None =>
          Optional.empty()
      }
    }
  }
}
