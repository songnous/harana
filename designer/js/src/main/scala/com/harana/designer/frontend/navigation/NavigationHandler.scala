package com.harana.designer.frontend.navigation

import com.harana.designer.frontend.{Circuit, Globals, Router, State}
import com.harana.designer.frontend.analytics.Analytics
import NavigationStore._
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.user.UserStore.SetPreference
import com.harana.designer.frontend.utils.http.Http
import diode._
import diode.AnyAction._
import io.circe.optics.JsonPath.root
import org.scalajs.dom
import sttp.model.Header
import typings.stripeV3.global.{Stripe_ => Stripe}
import typings.stripeV3.stripe.StripeServerCheckoutOptions

import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

class NavigationHandler extends ActionHandler(zoomTo(_.navigationState)) {

  private val routePreferenceId = "designer.route"

  override def handle = {

    case Init(preferences) =>
      effectOnly(Effect.action(
        preferences.get(routePreferenceId) match {
          case Some(r) => OpenRoute(r)
          case None => OpenRoute("/")
        }
      ))


    case OpenCheckout =>
      Analytics.checkout()
      updated(value.copy(openingCheckout = true),
        Effect(Http.getAsJson(s"https://${Globals.authDomain}/billing/checkout", List(Header("jwt", Globals.initialJwt))).map { json =>
          if (json.nonEmpty) {
            val stripe = Stripe(root.publishableKey.string.getOption(json.get).get)
            val serverOptions = StripeServerCheckoutOptions(root.sessionId.string.getOption(json.get).get)
            stripe.redirectToCheckout(serverOptions)
          }
          updated(value.copy(openingCheckout = false))
        })
      )


    case OpenRoute(route) =>
      Router.browserHistory.push(route)
      noChange


    case SaveRoute =>
      effectOnly(Effect.action(SetPreference(routePreferenceId, Some(dom.window.location.pathname))))


    case UpdateOpeningCheckout(opening) =>
        updated(value.copy(openingCheckout = opening))
  }
}