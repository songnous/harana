package com.harana.designer.backend.services.settings.models

import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, ParameterGroup, StringArrayParameter, StringParameter}

object NotifierType {

  val email = ParameterGroup("email",
    StringParameter("host", required = true),
    StringParameter("from", required = true),
    StringParameter("to", required = true),
    StringParameter("authUsername"),
    StringParameter("authPassword"),
    StringParameter("authIdentity"),
    StringParameter("authSecret"),
    BooleanParameter("requireTls"),
    BooleanParameter("supportsHtml")
  )

  val opsGenie = ParameterGroup("opsGenie",
    StringParameter("apiUrl"),
    StringParameter("apiKey"),
    StringArrayParameter("usernames")
  )

  val pagerDuty = ParameterGroup("pagerDuty",
    StringParameter("apiUrl"),
    StringParameter("routingKey"),
    StringParameter("serviceKey"),
    StringParameter("client"),
    StringParameter("severity")
  )

  val pushOver = ParameterGroup("pushOver",
    StringParameter("apiUrl"),
    StringParameter("userKey"),
    StringParameter("token"),
    StringParameter("priority"),
    StringParameter("retry"),
    StringParameter("expire")
  )

  val slack = ParameterGroup("slack",
    StringParameter("apiUrl"),
    StringParameter("channel"),
    StringParameter("username")
  )

  val splunkOnCall = ParameterGroup("splunkOnCall",
    StringParameter("apiUrl"),
    StringParameter("apiKey"),
    StringParameter("routingKey"),
    StringParameter("messageType"),
    StringParameter("entityDisplayName"),
    StringParameter("monitoringTool")
  )

  val webHook = ParameterGroup("webHook",
    StringParameter("apiUrl"),
    StringParameter("authUsername"),
    StringParameter("authPassword")
  )

  val weChat = ParameterGroup("weChat",
    StringParameter("apiUrl"),
    StringParameter("apiSecret"),
    StringParameter("agentId"),
    StringParameter("corpId"),
    StringParameter("toUser"),
    StringParameter("toParty"),
    StringParameter("toTag")
  )
}