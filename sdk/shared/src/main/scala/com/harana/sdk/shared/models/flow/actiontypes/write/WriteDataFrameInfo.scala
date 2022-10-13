package com.harana.sdk.shared.models.flow.actiontypes.write

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action1To0TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.actiontypes.inout.OutputStorageTypeChoice
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.choice.ChoiceParameter
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, Parameters}
import com.harana.sdk.shared.models.flow.utils.Id
import izumi.reflect.Tag

import scala.reflect.runtime.{universe => ru}

trait WriteDataFrameInfo extends Action1To0TypeInfo[DataFrameInfo] with Parameters with ActionDocumentation {

  val id: Id = "9e460036-95cc-42c5-ba64-5bc767a40e4e"
  val name = "write-dataframe"
  val since = Version(0, 4, 0)
  val category = IO

  @transient
  lazy val portI_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]

  val storageTypeParameter = ChoiceParameter[OutputStorageTypeChoice]("data-storage-type", default = Some(new OutputStorageTypeChoice.File()))
  def getStorageType = $(storageTypeParameter)
  def setStorageType(value: OutputStorageTypeChoice): this.type = set(storageTypeParameter, value)

  override val parameterGroups = List(ParameterGroup("", storageTypeParameter))
}

object WriteDataFrameInfo extends WriteDataFrameInfo