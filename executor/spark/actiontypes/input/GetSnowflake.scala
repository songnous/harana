package com.harana.executor.spark.actiontypes.input

import com.harana.sdk.shared.models.common.Parameter
import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.designer.data.DataSourceTypes.Snowflake._
import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.actiontypes.input.GetSnowflakeInfo
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.sdk.backend.models.flow.{ActionType, FlowContext}
import com.harana.executor.spark.actiontypes.dataSourceParameterValues
import zio.{IO, Task, UIO}

class GetSnowflake extends GetSnowflakeInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession
    val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)

    val region = dsParameterValues(regionParameter)
    val accountName = dsParameterValues(accountNameParameter)
    val username = dsParameterValues(usernameParameter)
    val password = dsParameterValues.opt(passwordParameter)
    val authenticationType = dsParameterValues.opt(authenticationTypeParameter)
    val privateKey = dsParameterValues.opt(privateKeyParameter)
    val oauthToken = dsParameterValues.opt(oauthTokenParameter)
//    val schema = dsParameterValues(schemaParameter)
    val warehouse = dsParameterValues.opt(warehouseParameter)
    val role = dsParameterValues.opt(roleParameter)
    val timezone = dsParameterValues.opt(timezoneParameter)
    val customTimezone = dsParameterValues.opt(customTimezoneParameter)

    val mode = parameters(modeParameter)
    val query = parameters.opt(queryParameter)
    val table = parameters.opt(tableParameter)
    val compress = parameters.opt(compressParameter)
    val maxFileSize = parameters.opt(maxFileSizeParameter)
    val parallelism = parameters.opt(parallelismParameter)

    val sfOptions = Map(
      "sfURL" -> s"$accountName.$region.snowflakecomputing.com",
      "sfUser" -> username,
      "sfPassword" -> password,
//      "pem_private_key" -> pkb,
      "sfDatabase" -> "<database>",
      "sfSchema" -> "<schema>",
      "sfWarehouse" -> warehouse,
//      "awsAccessKey" -> sc.hadoopConfiguration.get("fs.s3n.awsAccessKeyId"),
//      "awsSecretKey" -> sc.hadoopConfiguration.get("fs.s3n.awsSecretAccessKey"),
      "tempdir" -> "s3n://<temp-bucket-name>",
      "s3MaxFileSize" -> maxFileSize,
      "parallelism" -> parallelism)

    val df = spark.read
      .format("net.snowflake.spark.snowflake")
//      .options(sfOptions)
//      .option("dbtable", table)
//      .option("query", table)
      .load()

    IO.some(new Outputs(Map(outputPorts.head -> df)))
  }
}