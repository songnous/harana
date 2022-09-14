package com.harana.id.services.auth

import com.harana.id.jwt.modules.jwt.JWT
import com.harana.id.services.auth.Auth.Service
import com.harana.id.utils._
import com.harana.modules.airtable.Airtable
import com.harana.modules.clearbit.Clearbit
import com.harana.modules.email.Email
import com.harana.modules.handlebars.Handlebars
import com.harana.modules.mongo.Mongo
import com.harana.modules.stripe.StripeCustomers
import com.harana.modules.vertx.Vertx
import com.harana.modules.vertx.models.Response
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.stripe.{ StripePrices, StripeProducts, StripeSubscriptions}
import com.harana.sdk.shared.models.common.{MarketingChannel, User}
import com.harana.id.jwt.shared.models.DesignerClaims
import io.vertx.core.http.CookieSameSite
import io.vertx.ext.web.RoutingContext
import org.pac4j.core.profile.CommonProfile
import org.pac4j.oauth.profile.google2.Google2ProfileDefinition
import scala.jdk.CollectionConverters._
import zio._

import java.time.Instant
import java.time.temporal.ChronoUnit
import scala.util.Try

object LiveAuth {
  val layer = ZLayer.fromServices { (airtable: Airtable.Service,
                                     clearbit: Clearbit.Service,
                                     config: Config.Service,
                                     email: Email.Service,
                                     handlebars: Handlebars.Service,
                                     jwt: JWT.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service,
                                     stripeCustomers: StripeCustomers.Service,
                                     stripePrices: StripePrices.Service,
                                     stripeProducts: StripeProducts.Service,
                                     stripeSubscriptions: StripeSubscriptions.Service,
                                     vertx: Vertx.Service) => new Service {

    def logout(rc: RoutingContext): Task[Response] = {
      null
    }


    def redirectToApp(user: User, profile: Option[CommonProfile]): Task[Response] =
      for {
        user            <- updateUser(user, profile)
        timeout         <- config.int("web.jwt.sessionTimeout", 900)
        expires         =  Instant.now().plus(timeout, ChronoUnit.SECONDS)
        claims          =  claimsForUser(user, expires, profile.isDefined)
        jwtJson         <- jwt.generate(claims)
        domain          <- config.env("harana_domain")
        jwtCookie       <- jwt.cookie(jwtJson, domain).map(_.setPath("/").setSameSite(CookieSameSite.LAX))
        response        <- Task(Response.Redirect(s"https://designer.$domain/welcome", cookies = List(jwtCookie)))
      } yield response


    def renewToken(rc: RoutingContext): Task[Response] =
      for {
        timeout             <- config.int("web.jwt.sessionTimeout", 900)
        //existingToken       <- ZIO.fromOption(rc.cookieMap.asScala.get("jwt"))
        //expirationTime      =  Instant.now().plus(timeout, ChronoUnit.SECONDS)
        //renewedCookie       <- jwt.renewedJWTCookie[DesignerClaims](existingToken.getValue, expirationTime)
        response            =  Response.Empty(cookies = List())
      } yield response


    def resetPassword(rc: RoutingContext): Task[Response] = {
      null
    }

    def signup(rc: RoutingContext): Task[Response] =
      for {
        firstName             <- IO(rc.request().getParam("firstName")).orElseFail(new IllegalArgumentException("First name not provided"))
        lastName              <- IO(rc.request().getParam("lastName")).orElseFail(new IllegalArgumentException("Last name not provided"))
        emailAddress          <- IO(rc.request().getParam("emailAddress")).orElseFail(new IllegalArgumentException("Email address not provided"))
        password              <- IO(rc.request().getParam("password")).orElseFail(new IllegalArgumentException("Password not provided"))
        ipAddress             <- IO(rc.request().remoteAddress().toString)

        risk                  <- clearbitRisk(clearbit, firstName, lastName, emailAddress, ipAddress)
        _                     <- createAirtableRecord(airtable, config, firstName, lastName, emailAddress, ipAddress, risk._1)

        passwordHash          =  HashUtils.hash(password, emailAddress.reverse)

        trialLength           <- config.int("signup.trial.length")
        user                  =  User(
                                  emailAddress = emailAddress,
                                  firstName = firstName,
                                  lastName = lastName,
                                  marketingChannel = rc.cookieMap.asScala.get("marketing_channel").map(c => MarketingChannel.withName(c.getValue)),
                                  marketingChannelId = rc.cookieMap.asScala.get("marketing_channel_id").map(_.getValue()),
                                  password = Some(passwordHash.toString),
                                  trialStarted = Some(Instant.now),
                                  trialEnded = Some(Instant.now.plus(trialLength, ChronoUnit.DAYS)))
        _                     <- createUser(user)

        _                     <- Task.when(risk._2.equals("medium"))(sendEmail(config, email, handlebars, logger, "confirm.email", firstName, lastName, emailAddress))
        _                     <- Task.when(risk._2.equals("high"))(sendEmail(config, email, handlebars, logger, "blocked.email", firstName, lastName, emailAddress))

        safe                  =  risk._2.equals("low")
        url                   =  risk._2 match {
                                  case "low"    => "/signup/password"
                                  case "medium" => "/signup/verify"
                                  case "high"   => "/signup/verify"
                                }

        response              <-  if (safe) redirectToApp(user, None) else UIO(Response.Redirect(url))
      } yield response


    def signupOrRedirect(rc: RoutingContext, profile: Option[CommonProfile]): Task[Response] =
      for {
        profile               <- ZIO.fromOption(profile).orDieWith(_ => new Exception("Failed to find profile"))
        foundUser             <- mongo.findOne[User]("Users", Map("emailAddress" -> profile.getEmail))
        trialLength           <- config.int("signup.trial.length")
        newUser               =  User(
                                  emailAddress = profile.getEmail,
                                  external = true,
                                  firstName = profile.getFirstName,
                                  lastName = profile.getFamilyName,
                                  displayName = Some(profile.getDisplayName),
                                  marketingChannel = rc.cookieMap.asScala.get("marketing_channel").map(c => MarketingChannel.withName(c.getValue)),
                                  marketingChannelId = rc.cookieMap.asScala.get("marketing_channel_id").map(_.getValue()),
                                  trialStarted = Some(Instant.now),
                                  trialEnded = Some(Instant.now.plus(trialLength, ChronoUnit.DAYS)))
        _                     <- Task.when(foundUser.isEmpty)(createUser(newUser))
        user                  =  if (foundUser.isEmpty) newUser else foundUser.get
        response              <- redirectToApp(user, Some(profile))
      } yield response


    def resendConfirmation(rc: RoutingContext): Task[Response] =
      for {
        id                    <- IO(rc.request().getParam("id")).orElseFail(new IllegalArgumentException("Id not provided"))
        foundUser             <- mongo.findOne[User]("Users", Map("id" -> id)).mapError(e => new Exception(e.toString))
        highRiskUrl           <- config.string("signup.url.high")

        _                     <- Task.when(foundUser.isDefined)(sendEmail(config, email, handlebars, logger, "confirm.email", foundUser.get.firstName, foundUser.get.lastName, foundUser.get.emailAddress))
        response              = if (foundUser.isEmpty) Response.Redirect(highRiskUrl) else Response.Empty()
      } yield response


    def pauseAccount(rc: RoutingContext): Task[Response] = {
      null
    }


    def cancelAccount(rc: RoutingContext): Task[Response] = {
      null
    }

    private def claimsForUser(user: User, expires: Instant, external: Boolean) =
      DesignerClaims(
        audiences = List(),
        beta = user.beta,
        cluster = user.cluster,
        diskSpace = user.diskSpace,
        emailAddress = user.emailAddress,
        executorCount = user.executorCount,
        executorMemory = user.executorMemory,
        expires = expires,
        external = external,
        firstName = user.firstName,
        fsxSpeed = user.fsxSpeed,
        imageUrl = user.imageUrl,
        issued = Instant.now,
        lastName = user.lastName,
        marketingChannel = user.marketingChannel,
        marketingChannelId = user.marketingChannelId,
        notBefore = Instant.now,
        onboarded = user.onboarded,
        subscriptionEnded = user.subscriptionEnded,
        subscriptionCustomerId = user.subscriptionCustomerId,
        subscriptionId = user.subscriptionId,
        subscriptionPrice = user.subscriptionPrice,
        subscriptionPriceId = user.subscriptionPriceId,
        subscriptionProduct = user.subscriptionProduct,
        subscriptionStarted = user.subscriptionStarted,
        trialStarted = user.trialStarted,
        trialEnded = user.trialEnded,
        userId = user.id)

    private def createUser(user: User): Task[Unit] =
      for {
        _                     <- logger.info(s"Creating user: ${user.emailAddress}")
        _                     <- mongo.insert[User]("Users", user)
        _                     <- stripeCustomers.create(email = Some(user.emailAddress), metadata = Map("haranaId" -> user.id), name = Some(s"${user.firstName} ${user.lastName}")).mapError(e => new Exception(e.error.message))
      } yield ()


    private def updateUser(user: User, profile: Option[CommonProfile]): Task[User] =
      for {
        customers                 <- stripeCustomers.list(email = Some(user.emailAddress)).mapBoth(e => new Exception(e.error.message), _.data)
        _                         <- IO.when(customers.isEmpty)(createUser(user))
        customerId                =  Some(customers.head.id)

        subscription              <- stripeSubscriptions.list(customer = customerId).mapBoth(e => new Exception(e.error.message), _.data.headOption)
        price                     <- UIO(subscription.map(_.items.data.head.price))
        productId                 <- IO.foreach(price.map(_.id))(id => stripePrices.byId(id).map(_.product)).mapError(e => new Exception(e.error.message))
        product                   <- IO.foreach(productId)(id => stripeProducts.byId(id)).mapError(e => new Exception(e.error.message))

        updatedUser               =  user.copy(
                                      beta = product.exists(_.metadata("Beta").toBoolean),
                                      cluster = product.map(_.metadata("Cluster")),
                                      diskSpace = product.map(_.metadata("Disk Space").toInt),
                                      executorCount = product.map(_.metadata("Executor Count").toInt),
                                      executorMemory = product.map(_.metadata("Executor Count").toInt),
                                      fsxSpeed = product.map(_.metadata("FSX Speed").toInt),
                                      imageUrl = profile.flatMap(p => Try(p.getAttribute(Google2ProfileDefinition.PICTURE).toString).toOption),
                                      lastLogin = Some(Instant.now),
                                      subscriptionEnded = subscription.flatMap(_.canceledAt.map(Instant.ofEpochSecond)),
                                      subscriptionCustomerId = customerId,
                                      subscriptionId = subscription.map(_.id),
                                      subscriptionPrice = price.map(_.unitAmountDecimal.get),
                                      subscriptionPriceId = price.map(_.id),
                                      subscriptionProduct = product.map(_.name),
                                      subscriptionStarted = subscription.map(s => Instant.ofEpochSecond(s.start)),
                                      updated = Instant.now)

        _                         <- mongo.update[User]("Users", updatedUser)
      } yield updatedUser
  }}
}
