package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasFeaturesColumnParameter, HasSeedParameter}
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup

trait LDAModelInfo extends ActionObjectInfo with HasFeaturesColumnParameter with HasSeedParameter {

  val id = "AF4AA9E2-2406-4D28-9BF6-26B94C5135A0"

  override val parameterGroups = List(ParameterGroup("",
    featuresColumnParameter,
    seedParameter
  ))
}

object LDAModelInfo extends LDAModelInfo