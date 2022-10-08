package com.harana.id.jwt.modules.jwt

import com.harana.sdk.shared.models.jwt.JWTClaims

import java.time.Instant
import io.circe.{Decoder, Encoder}
import io.vertx.core.http.Cookie
import io.vertx.ext.web.RoutingContext
import org.jose4j.jwk.{HttpsJwks, RsaJsonWebKey}
import org.jose4j.jwt.consumer.JwtConsumer
import zio.macros.accessible
import zio.{Has, Task, UIO}

@accessible
object JWT {
  type JWT = Has[JWT.Service]

  trait Service {
    def key: Task[RsaJsonWebKey]

    def verify(jwt: String, jwksUrl: String): Task[Boolean]
    def generate[C <: JWTClaims](claims: C)(implicit e: Encoder[C]): Task[String]

    def claims[C <: JWTClaims](jwt: String)(implicit d: Decoder[C]): Task[C]
    def claims[C <: JWTClaims](rc: RoutingContext)(implicit d: Decoder[C]): Task[C]

    def buildConsumer: Task[JwtConsumer]
    def buildConsumer(jwks: HttpsJwks, audience: List[String]): Task[JwtConsumer]

    def cookie(jwt: String, domain: String): UIO[Cookie]
    def renewedCookie[C <: JWTClaims](jwt: String, domain: String, expires: Instant)(implicit d: Decoder[C], e: Encoder[C]): Task[Cookie]
  }
}