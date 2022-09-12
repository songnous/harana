package com.harana.executor.spark.actiontypes

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.backend.models.designer.flow.actiontypes.output.JdbcOutputActionTypeInfo
import com.harana.sdk.backend.models.designer.flow.execution.ExecutionError
import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.{DataFrame, DataFrameReader, SparkSession}
import com.harana.executor.spark.utils.PathUtils
import zio.IO

package object output {

  def setOutputJdbcOptions(reader: DataFrameReader, parameterValues: ParameterValues, actionType: JdbcOutputActionTypeInfo) = {
    val database = parameterValues.opt(actionType.databaseParameter)
    val table = parameterValues.opt(actionType.tableParameter)
    val batchSize = parameterValues.opt(actionType.batchSizeParameter)

    if (database.isDefined && table.isDefined) reader.option("dbtable", s"${database.get}.${table.get}")
    if (batchSize.isDefined) reader.option("batchsize", batchSize.get)
  }
}