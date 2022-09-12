package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasFeaturesColumnParameter, HasSeedParameter}

trait LDAModelInfo extends ActionObjectInfo with HasFeaturesColumnParameter with HasSeedParameter {

  val id = "AF4AA9E2-2406-4D28-9BF6-26B94C5135A0"

  val parameters = Array(
    featuresColumnParameter,
    seedParameter
  )
}

object LDAModelInfo extends LDAModelInfo