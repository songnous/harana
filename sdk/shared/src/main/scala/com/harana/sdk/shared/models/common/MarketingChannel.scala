package com.harana.sdk.shared.models.common

import enumeratum._

sealed trait MarketingChannel extends EnumEntry
case object MarketingChannel extends Enum[MarketingChannel] with CirceEnum[MarketingChannel] {
	case object EmailCampaign extends MarketingChannel
	case object EmailDirect extends MarketingChannel
	case object EmailSignature extends MarketingChannel
	case object BingAd extends MarketingChannel
	case object BingContent extends MarketingChannel
	case object FacebookAd extends MarketingChannel
	case object FacebookContent extends MarketingChannel
	case object GoogleAd extends MarketingChannel
	case object GoogleContent extends MarketingChannel
	case object InstagramAd extends MarketingChannel
	case object InstagramContent extends MarketingChannel
	case object LinkedinAd extends MarketingChannel
	case object LinkedinContent extends MarketingChannel
	case object RedditAd extends MarketingChannel
	case object RedditContent extends MarketingChannel
	case object TwitterAd extends MarketingChannel
	case object TwitterContent extends MarketingChannel
	val values = findValues
}