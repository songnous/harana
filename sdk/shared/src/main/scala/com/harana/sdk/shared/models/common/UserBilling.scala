package com.harana.sdk.shared.models.common

import java.time.Instant

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.User.UserId
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._
import com.harana.sdk.shared.utils.Random

@JsonCodec
case class User(
	beta: Boolean = false,
	cluster: Option[String] = None,
	created: Instant = Instant.now,
	createdBy: Option[UserId] = None,
	displayName: Option[String] = None,
	emailAddress: String,
	external: Boolean = false,
	firstName: String,
	groups: Set[UserId] = Set(),
	id: UserId = Random.long,
	imageUrl: Option[String] = None,
	lastLogin: Option[Instant] = None,
	lastName: String,
	lastSession: Option[Instant] = None,
  marketingChannel: Option[MarketingChannel] = None,
  marketingChannelId: Option[String] = None,
	onboarded: Boolean = false,
	password: Option[String] = None,
	publicKey: Option[String] = None,
	preferences: Map[String, String] = Map(),
	relationships: Map[String, EntityId] = Map(),
  status: Status = Status.Active,
	subscriptionEnded: Option[Instant] = None,
	subscriptionCustomerId: Option[String] = None,
	subscriptionId: Option[String] = None,
	subscriptionPrice: Option[BigDecimal] = None,
	subscriptionPriceId: Option[String] = None,
	subscriptionProduct: Option[String] = None, 
	subscriptionStarted: Option[Instant] = None,
	tags: Set[String] = Set(),
	trialEnded: Option[Instant] = None,
	trialStarted: Option[Instant] = None,
	updated: Instant = Instant.now,
	updatedBy: Option[UserId] = None,
	version: Long = 1L,
	visibility: Visibility = Visibility.Owner) extends Entity with Serializable {
	type EntityType = User
}

object User {
	type UserId = String
}