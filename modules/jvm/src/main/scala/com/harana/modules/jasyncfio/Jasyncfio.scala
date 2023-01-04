package com.harana.modules.jasyncfio

import com.facebook.ads.sdk.Campaign.{EnumBidStrategy, EnumObjective, EnumSpecialAdCategory}
import com.facebook.ads.sdk.{Ad, AdAccount, AdAccountAdRulesHistory, AdAccountAdVolume, AdAccountDeliveryEstimate, AdAccountMatchedSearchApplicationsEdgeData, AdAccountMaxBid, AdAccountReachEstimate, AdAccountSubscribedApps, AdAccountTargetingUnified, AdAccountTrackingData, AdAccountUser, AdActivity, AdCreative, AdImage, AdLabel, AdPlacePageSet, AdPreview, AdRule, AdSet, AdStudy, AdVideo, AdsInsights, AdsPixel, Album, AppRequest, Application, AssignedUser, AsyncRequest, Business, BusinessAssetGroup, BusinessOwnedObjectOnBehalfOfRequest, BusinessUser, Campaign, ContentDeliveryReport, CustomAudience, CustomAudiencesTOS, CustomConversion, Event, InstagramUser, MinimumBudget, OfflineConversionDataSet, Page, PageUserMessageThreadLabel, PlayableContent, ProductCatalog, PublisherBlockList, ReachFrequencyPrediction, SavedAudience, TargetingSentenceLine, UnifiedThread, User, VideoThumbnail}
import zio.{Has, Task}
import zio.macros.accessible

@accessible
object Jasyncfio {
  type OHC = Has[Jasyncfio.Service]

  trait Service {



  }
}