package com.harana.sdk.shared.models.jwt

import com.harana.sdk.shared.models.common.{MarketingChannel, UserBilling, UserResources}

import java.time.Instant
import io.circe.generic.JsonCodec

@JsonCodec
case class DesignerClaims(
  audiences: List[String],
  beta: Boolean,
  billing: UserBilling,
  cluster: Option[String],
  emailAddress: String,
  expires: Instant,
  external: Boolean,
  firstName: String,
  imageUrl: Option[String],
  issued: Instant,
  lastName: String,
  marketingChannel: Option[MarketingChannel],
  marketingChannelId: Option[String],
  notBefore: Instant,
  onboarded: Boolean,
  resources: UserResources,
  userId: String) extends JWTClaims {

    type JWTClaimsType = DesignerClaims
    val subject = emailAddress

    def renew(expires: Instant) =
      this.copy(expires = expires, issued = Instant.now, notBefore = Instant.now)

    def isTrialing: Boolean = {
      val now = Instant.now()
      if (billing.subscriptionId.isDefined)
        true
      else
        if (billing.trialStarted.isDefined && billing.trialEnded.isDefined)
          billing.trialStarted.get.isBefore(now) && billing.trialEnded.get.isAfter(now)
        else
          false
    }

    def hasTrialEnded: Boolean =
      billing.trialEnded.map(_.isBefore(Instant.now)).isDefined
}