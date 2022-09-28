package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.flow.ActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Other
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Other
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.{universe => ru}

trait UnknownActionInfo extends ActionInfo {

  val id: Id = "08752b37-3f90-4b8d-8555-e911e2de5662"
  val name = "Unknown Action"
  val parameterGroups = List.empty[ParameterGroup]
  val category = Other

  val inArity = 0
  val outArity = 0

  @transient
  override lazy val inputPorts: List[ru.TypeTag[_]] = List.empty

  @transient
  override lazy val outputPorts: List[ru.TypeTag[_]] = List.empty

}

object UnknownActionInfo extends UnknownActionInfo with UIActionInfo[UnknownActionInfo] {
  def apply(pos: (Int, Int), color: Option[String] = None) = new UnknownActionInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}