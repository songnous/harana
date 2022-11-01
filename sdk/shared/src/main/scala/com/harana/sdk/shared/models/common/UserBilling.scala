package com.harana.sdk.shared.models.common

import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class UserBilling(subscriptionEnded: Option[Instant] = None,
											 subscriptionCustomerId: Option[String] = None,
											 subscriptionId: Option[String] = None,
											 subscriptionPrice: Option[BigDecimal] = None,
											 subscriptionPriceId: Option[String] = None,
											 subscriptionProduct: Option[String] = None,
											 subscriptionStarted: Option[Instant] = None,
											 trialEnded: Option[Instant] = None,
											 trialStarted: Option[Instant] = None)