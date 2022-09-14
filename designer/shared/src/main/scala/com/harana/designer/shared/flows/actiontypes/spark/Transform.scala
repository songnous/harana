package com.harana.designer.shared.flows.actiontypes.spark

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup, ParameterValue}

object Transform {

   val types = List(

      ActionType("transform-add-column", Set(), "transform", inputPort, outputPort, parameterGroups = List(
         ParameterGroup("general", List(
            Parameter.String("type", required = true, options = List(
               ("value", ParameterValue.String("value")),
               ("uuid", ParameterValue.String("uuid")),
               ("empty", ParameterValue.String("empty")
            ))),
            Parameter.String("name", required = true),
            Parameter.String("valueType", required = true, options = List(
               ("integer",ParameterValue.String("integer")),
               ("double", ParameterValue.String("double")),
               ("string", ParameterValue.String("string"))
            )),
            Parameter.String("value", required = true)
         ))
      )),

      ActionType("transform-deduplicate", Set(), "transform", inputPort, outputPort, parameterGroups = List()),

      ActionType("transform-distinct", Set(), "transform", inputPort, outputPort, parameterGroups = List(
         ParameterGroup("general", List(
            Parameter.StringList("columns")
         ))
      )),

      ActionType("transform-drop-columns", Set(), "transform", inputPort, outputPort, parameterGroups = List(
         ParameterGroup("general", List(
            Parameter.StringList("columns")
         ))
      )),

      ActionType("transform-filter", Set(), "transform", inputPort, outputPort, parameterGroups = List(
         ParameterGroup("general", List(
            Parameter.String("condition", required = true)
         ))
      )),

      ActionType("transform-fork", Set(), "transform", inputPort, outputPort, parameterGroups = List()),

      ActionType("transform-join", Set(), "transform", List(Port.DataFrame("left-in"), Port.DataFrame("right-in")), List(Port.DataFrame("out")), parameterGroups = List(
         ParameterGroup("general", List(
            Parameter.String("mode", Some(ParameterValue.String("left")), required = true, options = List(
               ("inner", ParameterValue.String("inner")),
               ("left", ParameterValue.String("left")),
               ("right", ParameterValue.String("right")),
               ("outer", ParameterValue.String("outer"))
            )),
            Parameter.StringList("columns", required = true)
         ))
      )),

      ActionType("transform-merge", Set(), "transform", inputPort, outputPort, parameterGroups = List()),

      ActionType("transform-rename-columns", Set(), "transform", inputPort, outputPort, parameterGroups = List(
         ParameterGroup("general", List(
            Parameter.String("existing-name", required = true),
            Parameter.String("new-name", required = true)
         ))
      )),

      ActionType("transform-repartition", Set(), "transform", inputPort, outputPort, parameterGroups = List(
         ParameterGroup("general", List(
            Parameter.Integer("partition-count", required = true)
         ))
      )),

      ActionType("transform-select-columns", Set(), "transform", inputPort, outputPort, parameterGroups = List(
         ParameterGroup("general", List(
            Parameter.StringList("columns", required = true)
         ))
      )),

      ActionType("transform-standardise-column-names", Set(), "transform", inputPort, outputPort, parameterGroups = List(
         ParameterGroup("general", List(
            Parameter.StringList("columns", required = true)
         ))
      )),

      ActionType("transform-subtract", Set(), "transform", List(Port.DataFrame("left-in"), Port.DataFrame("right-in")), List(Port.DataFrame("out")), parameterGroups = List(
         ParameterGroup("general", List(
            Parameter.StringList("columns", required = true)
         ))
      )),

      ActionType("transform-transpose", Set(), "transform", inputPort, outputPort, parameterGroups = List(
         ParameterGroup("general", List(
            Parameter.StringList("transposed-columns", required = true),
            Parameter.String("pivot-column", required = true)
         ))
      ))
   )
}