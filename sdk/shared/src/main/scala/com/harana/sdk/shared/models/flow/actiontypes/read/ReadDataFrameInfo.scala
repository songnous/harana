package com.harana.sdk.shared.models.flow.actiontypes.read

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action0To1TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.actiontypes.inout.InputStorageTypeChoice
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.choice.ChoiceParameter
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, Parameters}
import com.harana.sdk.shared.models.flow.utils.Id
import izumi.reflect.Tag

import scala.reflect.runtime.{universe => ru}

// TODO Remake this case class into class
trait ReadDataFrameInfo
  extends Action0To1TypeInfo[DataFrameInfo]
    with Parameters
    with ActionDocumentation {

  val id: Id = "c48dd54c-6aef-42df-ad7a-42fc59a09f0e"
  val name = "read-dataframe"
  val since = Version(0, 4, 0)
  val category = IO

  val storageTypeParameter = ChoiceParameter[InputStorageTypeChoice]("data-storage-type", default = Some(new InputStorageTypeChoice.File()))
  def getStorageType = $(storageTypeParameter)
  def setStorageType(value: InputStorageTypeChoice): this.type = set(storageTypeParameter, value)

  override val parameterGroups = List(ParameterGroup("", storageTypeParameter))

  @transient
  lazy val portO_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]
}

object ReadDataFrameInfo extends ReadDataFrameInfo