package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasItemColumnParameter, HasPredictionColumnCreatorParameter, HasUserColumnParameter}

trait ALSModelInfo
    extends ActionObjectInfo
    with HasItemColumnParameter
    with HasPredictionColumnCreatorParameter
    with HasUserColumnParameter {

  val id = "F098E949-BAE3-4C97-985F-A7B76A3C480E"

  val parameters = Left(List(
    itemColumnParameter,
    predictionColumnParameter,
    userColumnParameter
  ))
}

object ALSModelInfo extends ALSModelInfo