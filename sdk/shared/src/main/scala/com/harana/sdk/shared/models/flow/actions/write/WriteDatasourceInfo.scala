package com.harana.sdk.shared.models.flow.actions.write

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action1To0Info
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.actions.dataframe.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import java.util.UUID
import scala.reflect.runtime.{universe => ru}

trait WriteDatasourceInfo extends Action1To0Info[DataFrameInfo]
  with WriteDatasourceParameters
  with ActionDocumentation {

  @transient
  lazy val portI_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

  val id: Id = "bf082da2-a0d9-4335-a62f-9804217a1436"
  val name = "Write DataFrame"
  val since = Version(1, 4, 0)
  val category = IO

  override def getDatasourcesIds = get(datasourceIdParameter).toSet
  def setDatasourceId(value: UUID): this.type = set(datasourceIdParameter, value)
  def setDatasourceId(value: String): this.type = setDatasourceId(UUID.fromString(value))
  private def getDatasourceId = $(datasourceIdParameter)

  private def getShouldOverwrite = $(shouldOverwriteParameter)
  setDefault(shouldOverwriteParameter, true)

  val parameters = Array(datasourceIdParameter, shouldOverwriteParameter)
}

object WriteDatasourceInfo extends WriteDatasourceInfo