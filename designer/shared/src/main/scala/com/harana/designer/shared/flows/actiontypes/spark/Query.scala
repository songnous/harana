package com.harana.designer.shared.flows.actiontypes.spark

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup, ParameterValue}
import com.harana.sdk.backend.models.designer.flow.actiontypes.{inputPort, outputPort}
import com.harana.sdk.backend.models.designer.flow.{ActionType, Port}

object Query {

   val types = List(

      ActionType("query-sql", Set(), "transform", inputPort, outputPort, parameterGroups = List(
         ParameterGroup("general", List(
            Parameter.String("query", multiLine = true, required = true),
            Parameter.String("view-name", required = true)
         ))
      ))
   )
}