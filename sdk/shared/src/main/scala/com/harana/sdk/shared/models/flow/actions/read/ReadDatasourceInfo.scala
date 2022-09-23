package com.harana.sdk.shared.models.flow.actions.read

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action0To1TypeInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.actions.dataframe.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.datasource.DatasourceIdForReadParameter
import com.harana.sdk.shared.models.flow.utils.Id

import java.util.UUID
import scala.reflect.runtime.{universe => ru}

trait ReadDatasourceInfo extends Action0To1TypeInfo[DataFrameInfo] with ActionDocumentation {

  val id: Id = "1a3b32f0-f56d-4c44-a396-29d2dfd43423"
  val name = "Read DataFrame"
  val since = Version(1, 4, 0)
  val category = IO

  @transient
  lazy val portO_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

  val datasourceIdParameter = DatasourceIdForReadParameter(name = "data source")
  def getDataSourceId = $(datasourceIdParameter)

  val parameters = Array(datasourceIdParameter)

  override def getDatasourcesIds: Set[UUID] = get(datasourceIdParameter).toSet
  def setDatasourceId(value: UUID): this.type = set(datasourceIdParameter, value)
  def setDatasourceId(value: String): this.type = setDatasourceId(UUID.fromString(value))
  private def getDatasourceId = $(datasourceIdParameter)
}

object ReadDatasourceInfo extends ReadDatasourceInfo {
  def apply() = new ReadDatasourceInfo {}
}