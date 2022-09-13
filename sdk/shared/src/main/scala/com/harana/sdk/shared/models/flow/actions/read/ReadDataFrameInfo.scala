package com.harana.sdk.shared.models.flow.actions.read

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action0To1Info
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.actions.dataframe.DataFrameInfo
import com.harana.sdk.shared.models.flow.actions.inout.InputStorageTypeChoice
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.Parameters
import com.harana.sdk.shared.models.flow.parameters.choice.ChoiceParameter
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.{universe => ru}

// TODO Remake this case class into class
trait ReadDataFrameInfo
  extends Action0To1Info[DataFrameInfo]
    with Parameters
    with ActionDocumentation {

  val id: Id = "c48dd54c-6aef-42df-ad7a-42fc59a09f0e"
  val name = "Read DataFrame"
  val description = "Reads a DataFrame from a file or database"
  val since = Version(0, 4, 0)
  val category = IO

  val storageTypeParameter = ChoiceParameter[InputStorageTypeChoice]("data storage type", Some("Storage type."))
  def getStorageType = $(storageTypeParameter)
  def setStorageType(value: InputStorageTypeChoice): this.type = set(storageTypeParameter, value)

  override val parameters = Array(storageTypeParameter)
  setDefault(storageTypeParameter, new InputStorageTypeChoice.File())

  @transient
  lazy val portO_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]
}

object ReadDataFrameInfo extends ReadDataFrameInfo