package com.harana.sdk.shared.models.flow.actiontypes.read

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action0To1TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.flow.parameters.datasource.DatasourceIdForReadParameter
import com.harana.sdk.shared.models.flow.utils.Id
import izumi.reflect.Tag
import shapeless.HMap

import java.util.UUID
import scala.reflect.runtime.{universe => ru}

trait ReadDatasourceInfo extends Action0To1TypeInfo[DataFrameInfo] with ActionDocumentation {

  val id: Id = "1a3b32f0-f56d-4c44-a396-29d2dfd43423"
  val name = "read-dataframe"
  val since = Version(1, 4, 0)
  val category = IO

  @transient
  lazy val portO_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]

  val datasourceIdParameter = DatasourceIdForReadParameter(name = "data-source-id")
  def getDataSourceId = $(datasourceIdParameter)

  override val parameterGroups = List(ParameterGroup("", datasourceIdParameter))

  override def getDatasourcesIds: Set[UUID] = get(datasourceIdParameter).toSet
  def setDatasourceId(value: UUID): this.type = set(datasourceIdParameter, value)
  def setDatasourceId(value: String): this.type = setDatasourceId(UUID.fromString(value))
  private def getDatasourceId = $(datasourceIdParameter)
}

object ReadDatasourceInfo extends ReadDatasourceInfo