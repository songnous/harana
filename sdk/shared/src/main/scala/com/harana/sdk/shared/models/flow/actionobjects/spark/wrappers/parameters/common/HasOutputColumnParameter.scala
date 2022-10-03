package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.{Parameters, SingleColumnCreatorParameter}

import scala.language.reflectiveCalls

trait HasOutputColumnParameter extends Parameters {

  val outputColumnParameter = SingleColumnCreatorParameter("output-column")
  def setOutputColumn(value: String): this.type = set(outputColumnParameter, value)

}