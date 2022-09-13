package com.harana.designer.backend.services.flows.argo.actiontypes

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup, ParameterValue}
import com.harana.sdk.backend.models.flow.{ActionType, Port}

object IO {

  val types = List(
    ActionType("command", Set(), "general", nonePort, nonePort, parameterGroups = List(
      ParameterGroup("general", List(
        Parameter.String("command", required = true),
        Parameter.String("shell", options =
          List(
            ("bash", ParameterValue.String("bash")),
            ("ksh", ParameterValue.String("ksh")),
            ("sh", ParameterValue.String("sh"))
          ), required = true),
        Parameter.StringList("arguments"),
      )),
      environmentParameterGroup
    )),
  )
}