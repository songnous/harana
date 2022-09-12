package com.harana.sdk.shared.models.jwt

import com.harana.sdk.shared.models.common.MarketingChannel
import java.time.Instant

import io.circe.generic.JsonCodec

@JsonCodec
case class DesignerClaims(
  audiences: List[String],
  beta: Boolean,
  cluster: Option[String],
  diskSpace: Option[Int],
  emailAddress: String,
  executorCount: Option[Int],
  executorMemory: Option[Int],
  expires: Instant,
  external: Boolean,
  firstName: String,
  fsxSpeed: Option[Int],
  imageUrl: Option[String],
  issued: Instant,
  lastName: String,
  marketingChannel: Option[MarketingChannel],
  marketingChannelId: Option[String],
  notBefore: Instant,
  onboarded: Boolean,
  subscriptionEnded: Option[Instant],
  subscriptionCustomerId: Option[String],
  subscriptionId: Option[String],
  subscriptionPrice: Option[BigDecimal],
  subscriptionPriceId: Option[String],
  subscriptionProduct: Option[String],
  subscriptionStarted: Option[Instant],
  trialEnded: Option[Instant],
  trialStarted: Option[Instant],
  userId: String) extends JWTClaims {

    type JWTClaimsType = DesignerClaims
    val subject = emailAddress

    def renew(expires: Instant) =
      this.copy(expires = expires, issued = Instant.now, notBefore = Instant.now)

    def isTrialing: Boolean = {
      val now = Instant.now()
      if (subscriptionId.isDefined)
        true
      else
        if (trialStarted.isDefined && trialEnded.isDefined)
          trialStarted.get.isBefore(now) && trialEnded.get.isAfter(now)
        else
          false
    }

    def hasTrialEnded: Boolean =
      trialEnded.map(_.isBefore(Instant.now)).isDefined
}