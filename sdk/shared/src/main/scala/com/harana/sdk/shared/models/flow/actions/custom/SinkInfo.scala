package com.harana.sdk.shared.models.flow.actions.custom

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action1To1Info
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.actions.dataframe.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.parameters.Parameter

import scala.reflect.runtime.{universe => ru}

trait SinkInfo extends Action1To1Info[DataFrameInfo, DataFrameInfo] {

  val id = "e652238f-7415-4da6-95c6-ee33808561b2"
  val name = "Sink"
  val since = Version(1, 0, 0)
  val parameters = Array.empty[Parameter[_]]
  val category = IO

  @transient
  lazy val portI_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

  @transient
  lazy val portO_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

}

object SinkInfo extends SinkInfo