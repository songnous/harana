package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.flow.ActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Other
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Other
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.{universe => ru}

trait UnknownActionInfo extends ActionInfo {

  val id: Id = "08752b37-3f90-4b8d-8555-e911e2de5662"
  val name = "Unknown Action"
  val category = Other

  val inArity = 0
  val outArity = 0

  @transient
  override lazy val inputPorts: List[ru.TypeTag[_]] = List.empty

  @transient
  override lazy val outputPorts: List[ru.TypeTag[_]] = List.empty

  val parameters = Left(List.empty[Parameter[_]])

}

object UnknownActionInfo extends UnknownActionInfo {
  def apply(pos: (Int, Int), color: Option[String] = None) = new UnknownActionInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}