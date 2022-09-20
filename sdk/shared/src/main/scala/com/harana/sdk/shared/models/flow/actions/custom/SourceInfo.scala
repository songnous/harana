package com.harana.sdk.shared.models.flow.actions.custom

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action0To1Info
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.actions.dataframe.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.parameters.Parameter

import scala.reflect.runtime.{universe => ru}

trait SourceInfo extends Action0To1Info[DataFrameInfo] {

  val id = "f94b04d7-ec34-42f7-8100-93fe235c89f8"
  val name = "Source"
  val since = Version(1, 0, 0)
  val parameters = Array.empty[Parameter[_]]
  val category = IO

  @transient
  lazy val portO_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

}

object SourceInfo extends SourceInfo