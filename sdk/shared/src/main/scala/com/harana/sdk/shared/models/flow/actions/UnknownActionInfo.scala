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
  override lazy val inPortTypes: Vector[ru.TypeTag[_]] = Vector()

  @transient
  override lazy val outPortTypes: Vector[ru.TypeTag[_]] = Vector()

  val parameters = Array.empty[Parameter[_]]
}