package com.harana.sdk.shared.models.flow.actiontypes.write

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action1To0TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup
import com.harana.sdk.shared.models.flow.utils.Id
import izumi.reflect.Tag

import java.util.UUID
import scala.reflect.runtime.{universe => ru}

trait WriteDatasourceInfo extends Action1To0TypeInfo[DataFrameInfo] with WriteDatasourceParameters with ActionDocumentation {

  val id: Id = "bf082da2-a0d9-4335-a62f-9804217a1436"
  val name = "write-dataframe"
  val since = Version(1, 4, 0)
  val category = IO

  @transient
  lazy val portI_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]

  override def getDatasourcesIds = get(datasourceIdParameter).toSet
  def setDatasourceId(value: UUID): this.type = set(datasourceIdParameter, value)
  def setDatasourceId(value: String): this.type = setDatasourceId(UUID.fromString(value))
  private def getDatasourceId = $(datasourceIdParameter)

  private def getShouldOverwrite = $(shouldOverwriteParameter)

  override val parameterGroups = List(ParameterGroup("", datasourceIdParameter, shouldOverwriteParameter))
}

object WriteDatasourceInfo extends WriteDatasourceInfo