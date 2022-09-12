package com.harana.executor.spark.actiontypes.input

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.designer.data.DataSourceTypes.Cassandra._
import com.harana.sdk.backend.models.designer.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.designer.flow.actiontypes.input.GetCassandraInfo
import com.harana.sdk.backend.models.designer.flow.execution.ExecutionError
import com.harana.sdk.backend.models.designer.flow.{ActionType, FlowContext}
import com.harana.executor.spark.actiontypes.{dataSourceParameterValues, log}
import zio.{IO, Task, UIO}

class GetCassandra extends GetCassandraInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession
    val conf = spark.conf
    val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)

    conf.set("spark.cassandra.connection.host", dsParameterValues(hostsParameter).mkString(","))
    conf.set("spark.cassandra.auth.username", dsParameterValues(usernameParameter))
    conf.set("spark.cassandra.auth.password", dsParameterValues(passwordParameter))
    conf.set("spark.cassandra.connection.ssl.enabled", dsParameterValues(sslParameter))

    val compression = dsParameterValues.opt(compressionParameter)
    if (compression.isDefined) conf.set("spark.cassandra.connection.compression", compression.get)

    val reconnectionDelayMSMin = dsParameterValues.opt(minReconnectionDelayParameter)
    if (reconnectionDelayMSMin.isDefined) conf.set("spark.cassandra.connection.reconnectionDelayMS.min", reconnectionDelayMSMin.get)

    val reconnectionDelayMSMax = dsParameterValues.opt(maxReconnectionDelayParameter)
    if (reconnectionDelayMSMax.isDefined) conf.set("spark.cassandra.connection.reconnectionDelayMS.max", reconnectionDelayMSMax.get)

    val connectionTimeout = dsParameterValues.opt(connectionTimeoutParameter)
    if (connectionTimeout.isDefined) conf.set("spark.cassandra.connection.timeoutMS", connectionTimeout.get)

    val readTimeout = dsParameterValues.opt(readTimeoutParameter)
    if (readTimeout.isDefined) conf.set("spark.cassandra.read.timeoutMS", readTimeout.get)

    val queryRetryCount = dsParameterValues.opt(queryRetryCountParameter)
    if (queryRetryCount.isDefined) conf.set("spark.cassandra.query.retry.count", queryRetryCount.get)

    val consistencyLevel = parameters.opt(consistencyLevelParameter)
    if (consistencyLevel.isDefined) conf.set("spark.cassandra.input.consistency.level", consistencyLevel.get)

    val concurrentReads = parameters.opt(concurrentReadsParameter)
    if (concurrentReads.isDefined) conf.set("spark.cassandra.concurrent.reads", concurrentReads.get)

    val fetchSizeInRows = parameters.opt(fetchSizeInRowsParameter)
    if (fetchSizeInRows.isDefined) conf.set("spark.cassandra.input.fetch.sizeInRows", fetchSizeInRows.get)

    val recordMetrics = parameters.opt(recordMetricsParameter)
    if (recordMetrics.isDefined) conf.set("spark.cassandra.input.metrics", fetchSizeInRows.get)

    val maxReadsPerSec = parameters.opt(maxReadsPerSecParameter)
    if (maxReadsPerSec.isDefined) conf.set("spark.cassandra.input.readsPerSec", maxReadsPerSec.get)

    val splitSizeInMB = parameters.opt(splitSizeInMBParameter)
    if (splitSizeInMB.isDefined) conf.set("spark.cassandra.input.split.sizeInMB", splitSizeInMB.get)

    val throughputMBPerSec = parameters.opt(throughputMBPerSecParameter)
    if (throughputMBPerSec.isDefined) conf.set("spark.cassandra.input.throughputMBPerSec", throughputMBPerSec.get)

    val directJoinSetting = parameters.opt(directJoinSettingParameter)
    if (directJoinSetting.isDefined) conf.set("directJoinSetting", directJoinSetting.get)

    val directJoinSizeRatio = parameters.opt(directJoinSizeRatioParameter)
    if (directJoinSizeRatio.isDefined) conf.set("directJoinSizeRatio", directJoinSizeRatio.get.toString)

    val ignoreMissingMetaColumns = parameters.opt(ignoreMissingMetaColumnsParameter)
    if (ignoreMissingMetaColumns.isDefined) conf.set("ignoreMissingMetaColumns", ignoreMissingMetaColumns.get)

    val outputDf = spark.read
      .format("cassandra")
      .option("keyspace", parameters(keyspaceParameter))
      .option("table", parameters(tableParameter))
      .load()

    log(outputDf, parameters) *>
    IO.some(new Outputs(Map(outputPorts.head -> outputDf)))
  }
}