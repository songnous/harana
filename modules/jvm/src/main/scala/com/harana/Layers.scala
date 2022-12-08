package com.harana

import com.harana.modules.airbyte.LiveAirbyte
import com.harana.modules.airtable.LiveAirtable
import com.harana.modules.alluxiofs.LiveAlluxioFs
import com.harana.modules.argo.LiveArgo
import com.harana.modules.aws.LiveAWS
import com.harana.modules.clearbit.LiveClearbit
import com.harana.modules.email.LiveEmail
import com.harana.modules.handlebars.LiveHandlebars
import com.harana.modules.kubernetes.LiveKubernetes
import com.harana.modules.mixpanel.LiveMixpanel
import com.harana.modules.mongo.LiveMongo
import com.harana.modules.stripe._
import com.harana.modules.vertx.LiveVertx
import com.harana.modules.zendesk.LiveZendesk
import com.harana.modules.core.{Layers => CoreLayers}
import com.harana.modules.core.okhttp.LiveOkHttp
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console.Console

object Layers {

  val okhttp = CoreLayers.standard >>> LiveOkHttp.layer

  val airtable = CoreLayers.standard >>> LiveAirtable.layer
  val alluxioFs = CoreLayers.standard >>> LiveAlluxioFs.layer
  val aws = Blocking.live ++ CoreLayers.standard >>> LiveAWS.layer
  val clearbit = (CoreLayers.standard ++ okhttp) >>> LiveClearbit.layer
  val email = CoreLayers.standard >>> LiveEmail.layer
  val handlebars = LiveHandlebars.layer
  val kubernetes = Clock.live ++ CoreLayers.standard >>> LiveKubernetes.layer
  val mixpanel = CoreLayers.standard >>> LiveMixpanel.layer
  val mongo = CoreLayers.standard >>> LiveMongo.layer
  val vertx = (Blocking.live ++ CoreLayers.standard) >>> LiveVertx.layer
  val zendesk = CoreLayers.standard >>> LiveZendesk.layer

  val airbyte = (CoreLayers.standard ++ kubernetes) >>> LiveAirbyte.layer
  val argo = (CoreLayers.standard ++ kubernetes) >>> LiveArgo.layer

  val stripeAccounts = CoreLayers.standard >>> LiveStripeAccounts.layer
  val stripeApplicationFeeRefunds = CoreLayers.standard >>> LiveStripeApplicationFeeRefunds.layer
  val stripeApplicationFees = CoreLayers.standard >>> LiveStripeApplicationFees.layer
  val stripeBalance = CoreLayers.standard >>> LiveStripeBalance.layer
  val stripeCharges = CoreLayers.standard >>> LiveStripeCharges.layer
  val stripeCountrySpecs = CoreLayers.standard >>> LiveStripeCountrySpecs.layer
  val stripeCoupons = CoreLayers.standard >>> LiveStripeCoupons.layer
  val stripeCustomerBankAccount = CoreLayers.standard >>> LiveStripeCustomerBankAccounts.layer
  val stripeCustomerCreditCards = CoreLayers.standard >>> LiveStripeCustomerCreditCards.layer
  val stripeCustomers = CoreLayers.standard >>> LiveStripeCustomers.layer
  val stripeDiscounts = CoreLayers.standard >>> LiveStripeDiscounts.layer
  val stripeDisputes = CoreLayers.standard >>> LiveStripeDisputes.layer
  val stripeEvents = CoreLayers.standard >>> LiveStripeEvents.layer
  val stripeExternalBankAccounts = CoreLayers.standard >>> LiveStripeExternalBankAccounts.layer
  val stripeExternalCreditCards = CoreLayers.standard >>> LiveStripeExternalCreditCards.layer
  val stripeInvoiceItems = CoreLayers.standard >>> LiveStripeInvoiceItems .layer
  val stripeInvoices = CoreLayers.standard >>> LiveStripeInvoices.layer
  val stripePlans = CoreLayers.standard >>> LiveStripePlans.layer
  val stripePrices = CoreLayers.standard >>> LiveStripePrices.layer
  val stripeProducts = CoreLayers.standard >>> LiveStripeProducts.layer
  val stripeRefunds = CoreLayers.standard >>> LiveStripeRefunds.layer
  val stripeSubscriptionItems = CoreLayers.standard >>> LiveStripeSubscriptionItems.layer
  val stripeSubscriptions = CoreLayers.standard >>> LiveStripeSubscriptions.layer
  val stripeTokens = CoreLayers.standard >>> LiveStripeTokens.layer
  val stripeTransferReversals = CoreLayers.standard >>> LiveStripeTransferReversals.layer
  val stripeTransfers = CoreLayers.standard >>> LiveStripeTransfers.layer
  val stripeUI = CoreLayers.standard ++ okhttp >>> LiveStripeUI.layer

}
