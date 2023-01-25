package com.harana.executor.spark.actiontypes

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.backend.models.flow.actiontypes.input.JdbcInputActionTypeInfo
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import org.apache.spark.sql.{DataFrame, DataFrameReader, SparkSession}
import com.harana.executor.spark.utils.PathUtils
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import zio.{IO, Task, UIO}

package object input {

  def setInputJdbcOptions(reader: DataFrameReader, parameterValues: ParameterValues, actionType: JdbcInputActionTypeInfo) = {
    val database = parameterValues.opt(actionType.databaseParameter)
    val table = parameterValues.opt(actionType.tableParameter)
    val query = parameterValues.opt(actionType.queryParameter)
    val partitionColumn = parameterValues.opt(actionType.partitionColumnParameter)
    val partitionUpperBound = parameterValues.opt(actionType.partitionUpperBoundParameter)
    val partitionLowerBound = parameterValues.opt(actionType.partitionLowerBoundParameter)
    val fetchSize = parameterValues.opt(actionType.fetchSizeParameter)

    if (database.nonEmpty && table.nonEmpty) reader.option("dbtable", s"${database.get}.${table.get}")
    if (query.nonEmpty) reader.option("query", query.get)
    if (partitionColumn.nonEmpty) reader.option("partitionColumn", partitionColumn.get)
    if (partitionLowerBound.nonEmpty) reader.option("lowerBound", partitionLowerBound.get)
    if (partitionUpperBound.nonEmpty) reader.option("upperBound", partitionUpperBound.get)
    if (fetchSize.nonEmpty) reader.option("fetchsize", partitionLowerBound.get)
  }

  def readFile(spark: SparkSession, format: String, path: String): Task[DataFrame] =
    Task {
      format match {
        case "avro" => spark.read.format("avro").load(path)
        case "binary-file" => spark.read.format("binaryFile").load(path)
        case "csv" => spark.read.csv(path)
        case "image" => spark.read.format("image").load(path)
        case "json" => spark.read.json(path)
        case "libsvm" => spark.read.format("libsvm").load(path)
        case "orc" => spark.read.orc(path)
        case "parquet" => spark.read.parquet(path)
        case "text" => spark.read.text(path)
      }
    }
}