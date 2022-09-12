package com.harana.designer.backend.services.settings.models

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}

object NotifierType {

  val email = ParameterGroup("email", List(
    Parameter.String("host", required = true),
    Parameter.String("from", required = true),
    Parameter.String("to", required = true),
    Parameter.String("authUsername"),
    Parameter.String("authPassword"),
    Parameter.String("authIdentity"),
    Parameter.String("authSecret"),
    Parameter.Boolean("requireTls"),
    Parameter.Boolean("supportsHtml"))
  )

  val opsGenie = ParameterGroup("opsGenie", List(
    Parameter.String("apiUrl"),
    Parameter.String("apiKey"),
    Parameter.StringList("usernames")
  ))

  val pagerDuty = ParameterGroup("pagerDuty", List(
    Parameter.String("apiUrl"),
    Parameter.String("routingKey"),
    Parameter.String("serviceKey"),
    Parameter.String("client"),
    Parameter.String("severity")
  ))

  val pushOver = ParameterGroup("pushOver", List(
    Parameter.String("apiUrl"),
    Parameter.String("userKey"),
    Parameter.String("token"),
    Parameter.String("priority"),
    Parameter.String("retry"),
    Parameter.String("expire")
  ))

  val slack = ParameterGroup("slack", List(
    Parameter.String("apiUrl"),
    Parameter.String("channel"),
    Parameter.String("username")
  ))

  val splunkOnCall = ParameterGroup("splunkOnCall", List(
    Parameter.String("apiUrl"),
    Parameter.String("apiKey"),
    Parameter.String("routingKey"),
    Parameter.String("messageType"),
    Parameter.String("entityDisplayName"),
    Parameter.String("monitoringTool")
  ))

  val webHook = ParameterGroup("webHook", List(
    Parameter.String("apiUrl"),
    Parameter.String("authUsername"),
    Parameter.String("authPassword")
  ))

  val weChat = ParameterGroup("weChat", List(
    Parameter.String("apiUrl"),
    Parameter.String("apiSecret"),
    Parameter.String("agentId"),
    Parameter.String("corpId"),
    Parameter.String("toUser"),
    Parameter.String("toParty"),
    Parameter.String("toTag")
  ))
}