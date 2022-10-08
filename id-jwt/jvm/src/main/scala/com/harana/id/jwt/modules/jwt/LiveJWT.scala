package com.harana.id.jwt.modules.jwt

import java.net.URL
import java.nio.charset.StandardCharsets
import java.security.spec.PKCS8EncodedKeySpec
import java.security.{KeyFactory, PrivateKey, SecureRandom}
import java.time.Instant
import java.util.concurrent.atomic.AtomicReference
import java.util.Base64
import com.github.blemale.scaffeine.{Cache => SCache}
import com.google.common.io.Resources
import com.harana.id.jwt.modules.jwt.JWT.Service
import com.harana.modules.core.cache.Cache
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.sdk.shared.models.jwt.JWTClaims
import com.nimbusds.jose.crypto.{RSASSASigner, RSASSAVerifier}
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.shaded.json.JSONObject
import com.nimbusds.jose.{JWSAlgorithm, JWSHeader}
import com.nimbusds.jwt.{JWTClaimsSet, SignedJWT}
import io.circe.{Decoder, Encoder}
import io.circe.parser._
import io.circe.syntax._
import io.vertx.core.http.{Cookie, CookieSameSite}
import io.vertx.ext.web.RoutingContext
import org.jose4j.jwk.{HttpsJwks, RsaJsonWebKey, RsaJwkGenerator}
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jwt.consumer.{JwtConsumer, JwtConsumerBuilder}
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver
import org.nustaq.serialization.FSTConfiguration
import zio.{Task, UIO, ZLayer}

import scala.jdk.CollectionConverters._

object LiveJWT {

  private val jwtKeyRef = new AtomicReference[Option[RsaJsonWebKey]](None)
  private val jwtCacheRef = new AtomicReference[Option[SCache[String, String]]](None)
  private val privateKeyRef = new AtomicReference[Option[PrivateKey]](None)
  private val fstConf = FSTConfiguration.createDefaultConfiguration

  val layer = ZLayer.fromServices { (cache: Cache.Service,
                                     config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    def key: Task[RsaJsonWebKey] =
      for {
        key            <- if (jwtKeyRef.get.isDefined) Task(jwtKeyRef.get.get) else
                              for {
                                random                    <- Task(SecureRandom.getInstanceStrong)
                                tokenLength               <- config.int("web.jwt.tokenLength", 64)
                                allowedTokenCharacters    <- config.string("web.jwt.allowedTokenCharacters", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789")
                                privateKey                <- privateKey
                                rsaJsonWebKey             <- Task {
                                                                val buf = new Array[Char](tokenLength)
                                                                for (i <- 0 until tokenLength) buf(i) = allowedTokenCharacters.toCharArray()(random.nextInt(allowedTokenCharacters.length))
                                                                val rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048)
                                                                rsaJsonWebKey.setKeyId(new String(buf))
                                                                rsaJsonWebKey.setAlgorithm(AlgorithmIdentifiers.RSA_USING_SHA512)
                                                                rsaJsonWebKey.setUse("sig")
                                                                rsaJsonWebKey.setPrivateKey(privateKey)
                                                                rsaJsonWebKey
                                                             }
                                bytes                     =  fstConf.asByteArray(rsaJsonWebKey)
                                // _                         <- vertx.putMapValue("jwt", "key", bytes)
                                // maybeJsonWebKey           <- vertx.getMapValue[String, Array[Byte]]("jwt", "key")
                                // jsonWebKey                =  maybeJsonWebKey.map(fstConf.asObject(_).asInstanceOf[RsaJsonWebKey]).getOrElse(rsaJsonWebKey)
                              } yield rsaJsonWebKey
        _                 =  jwtKeyRef.set(Some(key))
      } yield key


    private def jwtCache =
      for {
        jwtCache          <- if (jwtCacheRef.get.isDefined) Task(jwtCacheRef.get.get) else
                              for {
                                timeout   <- config.int("web.jwt.sessionRefreshTimeout", 15)
                                cache     <- cache.newCache[String, String](timeout * 60)
                              } yield cache
        _                 =  jwtCacheRef.set(Some(jwtCache))
      } yield jwtCache


    private def privateKey =
      for {
        privateKey          <- if (privateKeyRef.get.isDefined) Task(privateKeyRef.get.get) else
                                for {
                                  pemStr                <- config.string("web.jwt.privateKeyFile", "private.pem")
                                  pem                   <- Task(Resources.toString(Resources.getResource(pemStr), StandardCharsets.UTF_8))
                                  cleanPem              =  pem.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s+","")
                                  base64Decoded         <- Task(Base64.getDecoder.decode(cleanPem))
                                  keySpec               <- Task(new PKCS8EncodedKeySpec(base64Decoded))
                                  keyFactory            <- Task(KeyFactory.getInstance("RSA"))
                                  privateKey            <- Task(keyFactory.generatePrivate(keySpec))
                                } yield privateKey
        _                   =  privateKeyRef.set(Some(privateKey))
      } yield privateKey


    def claims[C <: JWTClaims](jwt: String)(implicit d: Decoder[C]): Task[C] =
      for {
        claimSet          <- Task(SignedJWT.parse(jwt).getJWTClaimsSet)
        claims            <- Task.fromEither(decode[C](JSONObject.toJSONString(claimSet.toJSONObject)))
      } yield claims


    def claims[C <: JWTClaims](rc: RoutingContext)(implicit d: Decoder[C]): Task[C] =
      for {
        jwt               <- Task(rc.getCookie("jwt").getValue)
        claims            <- claims(jwt)
      } yield claims


    def generate[C <: JWTClaims](claims: C)(implicit e: Encoder[C]): Task[String] =
      for {
        key               <- key
        signer            <- Task(new RSASSASigner(key.getRsaPrivateKey))
        claims            <- Task(claims.asJson.noSpaces)
        claimSet          <- Task(JWTClaimsSet.parse(claims))
        signedJWT         <- Task(new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(key.getKeyId).build(), claimSet))
        _                 <- Task(signedJWT.sign(signer))
        jwt               <- Task(signedJWT.serialize())
      } yield jwt


    def verify(jwt: String, jwksUrl: String): Task[Boolean] =
      for {
        cache       <-    jwtCache
        verified    <-    cache.getIfPresent(jwt) match {
                            case Some(_) => Task(true)
                            case None =>
                              for {
                                jwkSet    <-  Task(JWKSet.load(new URL(jwksUrl)))
                                verifier  <-  Task(new RSASSAVerifier(jwkSet.getKeys.asScala.head.toRSAKey))
                                verified  <-  Task(SignedJWT.parse(jwt).verify(verifier))
                              } yield verified
                          }
        _          <-     UIO(cache.put(jwt, "")).when(verified)
        _          <-     logger.debug(s"JWT: $jwt is verified: $verified")
      } yield verified


    def buildConsumer: Task[JwtConsumer] =
      buildConsumer(null, null)


    def buildConsumer(jwks: HttpsJwks, audience: List[String]): Task[JwtConsumer] =
      Task {
        val builder = new JwtConsumerBuilder().setRequireJwtId()
        if (jwks != null) builder.setVerificationKeyResolver(new HttpsJwksVerificationKeyResolver(jwks))
        else builder.setSkipSignatureVerification()
        if (audience != null) builder.setExpectedAudience(audience: _*)
        else builder.setSkipDefaultAudienceValidation()
        builder.build
      }


    def cookie(jwt: String, domain: String): UIO[Cookie] =
      for {
        timeout   <- config.int("web.jwt.sessionTimeout", 15)
        useSSL    <- config.boolean("web.useSSL", default = false)
        cookie    <- UIO {
                      val cookie = Cookie.cookie("jwt", jwt)
                      cookie.setDomain(domain)
                      cookie.setHttpOnly(true)
                      cookie.setMaxAge(timeout * 60)
                      cookie.setPath("/")
                      cookie.setSameSite(CookieSameSite.LAX)
                      cookie.setSecure(useSSL)
                    }
      } yield cookie


    def renewedCookie[C <: JWTClaims](jwt: String, domain: String, expires: Instant)(implicit d: Decoder[C], e: Encoder[C]): Task[Cookie] =
      for {
        claims            <- claims[C](jwt)
        renewedClaims     =  claims.renew(expires)
        renewedJwt        <- generate[C](renewedClaims.asInstanceOf[C])
        renewedCookie     <- cookie(renewedJwt, domain)
      } yield renewedCookie
  }}
}