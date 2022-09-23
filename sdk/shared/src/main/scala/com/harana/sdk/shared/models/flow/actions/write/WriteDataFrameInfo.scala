package com.harana.sdk.shared.models.flow.actions.write

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action1To0Info
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.actions.dataframe.DataFrameInfo
import com.harana.sdk.shared.models.flow.actions.inout.OutputStorageTypeChoice
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers.TokenizeWithRegexInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.Parameters
import com.harana.sdk.shared.models.flow.parameters.choice.ChoiceParameter
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.{universe => ru}

trait WriteDataFrameInfo
  extends Action1To0Info[DataFrameInfo]
    with Parameters
    with ActionDocumentation {

  val id: Id = "9e460036-95cc-42c5-ba64-5bc767a40e4e"
  val name = "Write DataFrame"
  val since = Version(0, 4, 0)
  val category = IO

  @transient
  lazy val portI_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

  val storageTypeParameter = ChoiceParameter[OutputStorageTypeChoice]("data storage type")
  setDefault(storageTypeParameter, new OutputStorageTypeChoice.File())
  def getStorageType = $(storageTypeParameter)
  def setStorageType(value: OutputStorageTypeChoice): this.type = set(storageTypeParameter, value)

  override val parameters = Array(storageTypeParameter)
}

object WriteDataFrameInfo extends WriteDataFrameInfo {
  def apply() = new WriteDataFrameInfo {}
}