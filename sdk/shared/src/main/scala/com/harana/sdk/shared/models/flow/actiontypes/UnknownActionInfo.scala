package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.flow.ActionTypeInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Other
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup
import com.harana.sdk.shared.models.flow.utils.Id
import izumi.reflect.Tag

import scala.reflect.runtime.{universe => ru}

trait UnknownActionInfo extends ActionTypeInfo {

  val id: Id = "08752b37-3f90-4b8d-8555-e911e2de5662"
  val name = "unknown-action"
  override val parameterGroups = List.empty[ParameterGroup]
  val category = Other

  val inArity = 0
  val outArity = 0

  @transient
  override lazy val inputPorts: List[Tag[_]] = List.empty

  @transient
  override lazy val outputPorts: List[Tag[_]] = List.empty

}

object UnknownActionInfo extends UnknownActionInfo