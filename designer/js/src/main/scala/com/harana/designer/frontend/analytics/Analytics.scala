package com.harana.designer.frontend.analytics

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.{Circuit, Main}
import org.scalajs.dom
import slinky.history.History

import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId, ZoneOffset}
import scala.collection.mutable.ListBuffer
import scala.scalajs.js
import scala.scalajs.js.Dynamic
import scala.scalajs.js.JSConverters._

object Analytics {

  private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.from(ZoneOffset.UTC))
  private val claims = Main.claims
  private val pageHistory = new ListBuffer[String]

  def history = {
    val history = History.createBrowserHistory()
    history.listen(() => {
      Analytics.pageView(
        pageHostname = dom.window.location.hostname,
        pagePath = dom.window.location.pathname,
        pageReferrer = pageHistory.lastOption,
        pageUrl = dom.window.location.href
      )
      pageHistory += dom.window.location.href
    })
    history
  }


  def receiveEvent(eventType: String, eventParameters: Map[String, String]) =
    eventType match {
      case "subscribe" => Analytics.subscribe()
      case "unsubscribe" => Analytics.unsubscribe()
      case _ =>
    }


  def appStart(appId: String, appName: String) =
    sendEvent("app_start", appId = Some(appId), appName = Some(appName))


  def appStop(appId: String, appName: String) =
    sendEvent("app_stop", appId = Some(appId), appName = Some(appName))


  def checkout() =
    sendEvent("checkout")


  def dataSourceCreate() =
    sendEvent("datasource_create")


  def dataSourceDelete() =
    sendEvent("datasource_delete")


  def fileUpload() =
    sendEvent("file_upload")


  def flowCreate() =
    sendEvent("flow_create")


  def flowDelete() =
    sendEvent("flow_delete")


  def flowStart(flowId: String, actionsCount: Int) =
    sendEvent("flow_start", flowId = Some(flowId), flowActionsCount = Some(actionsCount.toString))


  def flowStop(flowId: String, created: Instant, actionsCount: Int, accepted: Option[Instant], completed: Option[Instant], stopCause: String) =
    sendEvent("flow_stop",
      flowId = Some(flowId),
      flowAccepted = accepted.map(_.toString),
      flowAcceptanceDuration = Some(duration(Some(created), accepted, false)),
      flowActionsCount = Some(actionsCount.toString),
      flowExecutionDuration = Some(duration(accepted, completed, false)),
      flowStopCause = Some(stopCause),
      flowTotalDuration = Some(duration(Some(created), accepted, false) + duration(accepted, completed, false)))


  def init() =
    sendEvent("init")


  def pageView(pageHostname: String, pagePath: String, pageReferrer: Option[String], pageUrl: String) =
    sendEvent("page_view", pageHostname = Some(pageHostname), pagePath = Some(pagePath), pageReferrer = pageReferrer, pageUrl = Some(pageUrl))


  def scheduleCreate() =
    sendEvent("schedule_create")


  def scheduleDelete() =
    sendEvent("schedule_delete")


  def session() =
    sendEvent("session", newSession = true)


  def subscribe() =
    sendEvent("subscribe")


  def unsubscribe() =
    sendEvent("unsubscribe")


  private def sendEvent(eventName: String,
                        appId: Option[String] = None,
                        appName: Option[String] = None,
                        dataSourceType: Option[String] = None,
                        flowId: Option[String] = None,
                        flowAccepted: Option[String] = None,
                        flowAcceptanceDuration: Option[String] = None,
                        flowActionsCount: Option[String] = None,
                        flowCompleted: Option[String] = None,
                        flowExecutionDuration: Option[String] = None,
                        flowStopCause: Option[String] = None,
                        flowTotalDuration: Option[String] = None,
                        newSession: Boolean = false,
                        pageHostname: Option[String] = None,
                        pagePath: Option[String] = None,
                        pageReferrer: Option[String] = None,
                        pageUrl: Option[String] = None): Unit =

    try {
      val dataLayer = Dynamic.global.window.dataLayer.asInstanceOf[js.Array[js.Any]]
      val event = new Event {

        val ecommerceItems = js.Array(
          new Item {
            override val index = 1
            override val item_name = claims.subscriptionProduct
            override val item_id = claims.subscriptionPriceId
            override val price = claims.subscriptionPrice.map(_.toString)
            override val quantity = "1"
          }
        )

        override val event = eventName
        override val ecommerce = new Ecommerce {
          override val items = ecommerceItems

          override val purchase = new Purchase {
            override val transaction_id = claims.subscriptionId
            override val value = claims.subscriptionPrice.map(_.toString)
            override val currency = "USD"
            override val items = ecommerceItems
          }
        }

        override val page_hostname = pageHostname
        override val page_path = pagePath
        override val page_url = pageUrl
        override val referrer = pageReferrer

        override val email_address = claims.emailAddress
        override val first_name = claims.firstName
        override val last_name = claims.lastName
        override val user_id = claims.userId

        override val app_count = Circuit.state(zoomTo(_.appListState), false).items.size.toString
        override val app_id = appId
        override val app_name = appName
        override val datasource_count = Circuit.state(zoomTo(_.dataSourceListState), false).items.size.toString
        override val datasource_type = dataSourceType
        override val file_usage = Circuit.state(zoomTo(_.filesState), false).files.map(_.size).sum.toString
        override val flow_id = flowId
        override val flow_acceptance_duration = flowAcceptanceDuration
        override val flow_accepted = flowAccepted
        override val flow_actions_count = flowActionsCount
        override val flow_completed = flowCompleted
        override val flow_count = Circuit.state(zoomTo(_.flowListState), false).items.size.toString
        override val flow_execution_count = Circuit.state(zoomTo(_.appListState), false).items.size.toString
        override val flow_execution_duration = flowExecutionDuration
        override val flow_stop_cause = flowStopCause
        override val flow_total_duration = flowTotalDuration
        override val last_login = date(Some(claims.issued))
        override val last_session = if (newSession) date(Some(Instant.now)) else js.undefined
        override val marketing_channel = claims.marketingChannel.map(_.entryName).orUndefined
        override val marketing_channel_id = claims.marketingChannelId
        override val schedule_count = Circuit.state(zoomTo(_.scheduleListState), false).items.size.toString
        override val subscription_id = claims.subscriptionId
        override val subscription_ended = date(claims.subscriptionEnded)
        override val subscription_customer_id = claims.subscriptionCustomerId
        override val subscription_duration = duration(claims.subscriptionStarted, claims.subscriptionEnded)
        override val subscription_price = claims.subscriptionPrice.map(p => (p / 100).toString)
        override val subscription_price_id = claims.subscriptionPriceId
        override val subscription_product = claims.subscriptionProduct
        override val subscription_started = date(claims.subscriptionStarted)
        override val trial_duration = duration(claims.trialStarted, claims.trialEnded)
        override val trial_ended = date(claims.trialEnded)
        override val trial_started = date(claims.trialStarted)
      }
      dataLayer.push(event)
    } catch {
      case e: Exception => println(s"Failed to send event: ${e.getMessage}")
    }


  private def duration(start: Option[Instant], end: Option[Instant], inDays: Boolean = true) = {
    val duration = (start, end) match {
      case (Some(start), Some(end)) => ((end.toEpochMilli - start.toEpochMilli)/1000)
      case (Some(start), None) => ((Instant.now().toEpochMilli - start.toEpochMilli)/1000)
      case _ => 0
    }
    (if (inDays) duration / (3600*24) else duration).toString
  }


  private implicit def optional(o: Option[String]): js.UndefOr[String] =
    o.getOrElse("").asInstanceOf[js.UndefOr[String]]


  private def date(time: Option[Instant]): String =
    if (time.isDefined) dateFormatter.format(time.get) else ""
}