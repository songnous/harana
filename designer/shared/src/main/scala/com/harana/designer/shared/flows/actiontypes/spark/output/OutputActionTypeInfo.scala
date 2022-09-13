package com.harana.designer.shared.flows.actiontypes.spark.output

import com.harana.sdk.shared.models.common.Parameter
import com.harana.designer.shared.flows.actiontypes.spark.ActionTypeGroup
import com.harana.sdk.backend.models.flow.{ActionTypeInfo, Port}

abstract class OutputActionTypeInfo extends ActionTypeInfo {
  val group = ActionTypeGroup.Output
  val inputPorts = List(Port.DataFrame("in"))
  val outputPorts = List()

  val saveModeParameter = Parameter.String("save-mode", required = true, options = List(
    ("overwrite", "overwrite"),
    ("append", "append"),
    ("error-if-exists", "errorIfExists"),
    ("ignore", "ignore")
  ))
}