package com.harana.designer.frontend.analytics

import scala.scalajs.js

trait Event extends js.Object {
  val event: String
  val ecommerce: js.UndefOr[Ecommerce] = js.undefined

  val page_hostname: js.UndefOr[String] = js.undefined
  val page_path: js.UndefOr[String] = js.undefined
  val page_url: js.UndefOr[String] = js.undefined
  val referrer: js.UndefOr[String] = js.undefined

  val email_address: js.UndefOr[String] = js.undefined
  val first_name: js.UndefOr[String] = js.undefined
  val last_name: js.UndefOr[String] = js.undefined
  val user_id: js.UndefOr[String] = js.undefined

  val app_count: js.UndefOr[String] = js.undefined
  val app_id: js.UndefOr[String] = js.undefined
  val app_name: js.UndefOr[String] = js.undefined
  val datasource_count: js.UndefOr[String] = js.undefined
  val datasource_type: js.UndefOr[String] = js.undefined
  val file_usage: js.UndefOr[String] = js.undefined
  val flow_id: js.UndefOr[String] = js.undefined
  val flow_acceptance_duration: js.UndefOr[String] = js.undefined
  val flow_accepted: js.UndefOr[String] = js.undefined
  val flow_actions_count: js.UndefOr[String] = js.undefined
  val flow_completed: js.UndefOr[String] = js.undefined
  val flow_count: js.UndefOr[String] = js.undefined
  val flow_execution_count: js.UndefOr[String] = js.undefined
  val flow_execution_duration: js.UndefOr[String] = js.undefined
  val flow_stop_cause: js.UndefOr[String] = js.undefined
  val flow_total_duration: js.UndefOr[String] = js.undefined
  val query_count: js.UndefOr[String] = js.undefined
  val query_duration: js.UndefOr[String] = js.undefined

  val last_login: js.UndefOr[String] = js.undefined
  val last_session: js.UndefOr[String] = js.undefined
  val marketing_channel: js.UndefOr[String] = js.undefined
  val marketing_channel_id: js.UndefOr[String] = js.undefined
  val schedule_count: js.UndefOr[String] = js.undefined
  val subscription_ended: js.UndefOr[String] = js.undefined
  val subscription_customer_id: js.UndefOr[String] = js.undefined
  val subscription_duration: js.UndefOr[String] = js.undefined
  val subscription_id: js.UndefOr[String] = js.undefined
  val subscription_price: js.UndefOr[String] = js.undefined
  val subscription_price_id: js.UndefOr[String] = js.undefined
  val subscription_product: js.UndefOr[String] = js.undefined
  val subscription_started: js.UndefOr[String] = js.undefined
  val trial_duration: js.UndefOr[String] = js.undefined
  val trial_ended: js.UndefOr[String] = js.undefined
  val trial_started: js.UndefOr[String] = js.undefined
}


trait Ecommerce extends js.Object {
  val purchase: js.UndefOr[Purchase] = js.undefined
  val items: js.Array[Item]
}


trait Item extends js.Object {
  val index: Int
  val item_name: js.UndefOr[String] = js.undefined
  val item_id: js.UndefOr[String] = js.undefined
  val price: js.UndefOr[String] = js.undefined
  val quantity: js.UndefOr[String] = js.undefined
  val item_brand: js.UndefOr[String] = js.undefined
  val item_category: js.UndefOr[String] = js.undefined
  val item_category_2: js.UndefOr[String] = js.undefined
  val item_category_3: js.UndefOr[String] = js.undefined
  val item_category_4: js.UndefOr[String] = js.undefined
  val item_variant: js.UndefOr[String] = js.undefined
  val item_list_name: js.UndefOr[String] = js.undefined
  val item_list_id: js.UndefOr[String] = js.undefined
}


trait Purchase extends js.Object {
  val transaction_id: js.UndefOr[String] = js.undefined
  val affiliation: js.UndefOr[String] = js.undefined
  val value: js.UndefOr[String] = js.undefined
  val tax: js.UndefOr[String] = js.undefined
  val shipping: js.UndefOr[String] = js.undefined
  val currency: js.UndefOr[String] = js.undefined
  val coupon: js.UndefOr[String] = js.undefined
  val items: js.Array[Item]
}