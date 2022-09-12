package com.harana.executor.spark

import com.harana.sdk.shared.models.common.Parameter
import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.designer.data.{DataSourceType, JdbcDataSourceType}
import com.harana.sdk.backend.models.designer.flow.FlowContext
import com.harana.sdk.backend.models.designer.flow.actiontypes.{profileParameter, sampleParameter, schemaParameter}
import com.harana.sdk.backend.models.designer.flow.execution.ExecutionError
import com.harana.sdk.backend.models.designer.flow.execution.ExecutionError.InvalidParameter
import org.apache.commons.io.FilenameUtils
import org.apache.log4j.LogManager
import org.apache.spark.sql.{DataFrame, DataFrameReader, SparkSession}
import org.apache.spark.sql.AccessShowString.showString
import sourcecode._
import zio.{IO, Task, UIO}

package object actiontypes {

  def param[A <: Parameter](values: ParameterValues, parameter: A): IO[InvalidParameter, Option[A#Value]] =
    try {
      IO.succeed(values.get(parameter))
    } catch {
      case e: Exception => IO.fail(InvalidParameter(parameter.name, ""))
    }


  def dataSourceParameterValues(context: FlowContext, parameters: ParameterValues, dataSourceType: DataSourceType, dataSourceParameter: Parameter.DataSource): ParameterValues = {
    val dataSource = context.dataSources(parameters(dataSourceParameter))
    val dataSourceParameterValues = dataSourceType.parameterGroups.flatMap(_.parameters)
    new ParameterValues(dataSourceParameterValues.map { p => (p, dataSource.parameterValues.get(p.name))}.collect { case (k, Some(v)) => k -> v }.toMap)
  }


  def setCommonJdbcOptions(reader: DataFrameReader, parameterValues: ParameterValues, dataSource: JdbcDataSourceType) = {
    val username = parameterValues(dataSource.usernameParameter)
    val password = parameterValues(dataSource.passwordParameter)
    val sessionInitStatement = parameterValues.opt(dataSource.sessionInitStatementParameter)
    val numPartitions = parameterValues.opt(dataSource.numPartitionsParameter)
    val queryTimeout = parameterValues.opt(dataSource.queryTimeoutParameter)
    val isolationLevel = parameterValues.opt(dataSource.isolationLevelParameter)

    reader.option("user", username)
    reader.option("password", password)
    if (sessionInitStatement.isDefined) reader.option("sessionInitStatement", sessionInitStatement.get)
    if (numPartitions.isDefined) reader.option("numPartitions", numPartitions.get)
    if (queryTimeout.isDefined) reader.option("queryTimeout", queryTimeout.get)
    if (isolationLevel.isDefined) reader.option("isolationLevel", isolationLevel.get)
  }

  def log(df: DataFrame, parameterValues: ParameterValues)(implicit file: File): IO[ExecutionError, Unit] = {
    val output = for {
      name      <- UIO(FilenameUtils.getBaseName(file.value))
      logger    <- UIO(LogManager.getLogger(name))
      profile   <- Task(parameterValues.opt(profileParameter).exists(_.value))
      sample    <- Task(parameterValues.opt(sampleParameter).exists(_.value))
      schema    <- Task(parameterValues.opt(schemaParameter).exists(_.value))
      _         <- Task.when(profile)(UIO(logger.info(df.describe().schema.treeString)))
      _         <- Task.when(sample)(UIO(logger.info(showString(df, 20))))
      _         <- Task.when(schema)(UIO(logger.info(df.schema.treeString)))
    } yield ()
    output.mapError(e => ExecutionError.Unknown(e))
  }

  def writeFile(spark: SparkSession, df: DataFrame, format: String, path: String): Task[DataFrame] =
    Task {
      format match {
        case "avro" => df.write.format("avro").save(path)
        case "binary-file" => df.write.format("binaryFile").save(path)
        case "csv" => df.write.csv(path)
        case "image" => df.write.format("image").save(path)
        case "json" => df.write.json(path)
        case "libsvm" => df.write.format("libsvm").save(path)
        case "orc" => df.write.orc(path)
        case "parquet" => df.write.parquet(path)
        case "text" => df.write.text(path)
        case _ => 
      }
      df
    }
}