package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup, Parameters}

trait KMeansModelInfo extends ActionObjectInfo with Parameters {

  val id = "E6A8B2FF-A894-42FB-9F08-2FA380434BD2"

  val parameterGroups = List.empty[ParameterGroup]

}

object KMeansModelInfo extends KMeansModelInfo