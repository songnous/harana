package com.harana.executor.spark.actiontypes.output

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.designer.data.DataSourceTypes.Cassandra._
import com.harana.sdk.backend.models.designer.flow.actiontypes.output.PutCassandraInfo
import com.harana.sdk.backend.models.designer.flow.execution.ExecutionError
import com.harana.sdk.backend.models.designer.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.designer.flow.{ActionType, FlowContext}
import com.harana.executor.spark.actiontypes.log
import com.harana.executor.spark.actiontypes.dataSourceParameterValues
import zio.{IO, Task, UIO}

class PutCassandra extends PutCassandraInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession
    val conf = spark.conf
    val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)

    val inputDf = inputs(inputPorts.head)
    log(inputDf, parameters)

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

    val consistencyLevel = parameters.opt(consistencyLevelParameter)
    if (consistencyLevel.isDefined) conf.set("spark.cassandra.input.consistency.level", consistencyLevel.get)

    val concurrentWrites = parameters.opt(concurrentWritesParameter)
    if (concurrentWrites.isDefined) conf.set("spark.cassandra.output.concurrent.writes", concurrentWrites.get)

    val ignoreNulls = parameters.opt(ignoreNullsParameter)
    if (ignoreNulls.isDefined) conf.set("spark.cassandra.output.ignoreNulls", ignoreNulls.get)

    val timestamp = parameters.opt(timestampParameter)
    if (timestamp.isDefined) conf.set("spark.cassandra.output.timestamp", timestamp.get)

    val ttl = parameters.opt(ttlParameter)
    if (ttl.isDefined) conf.set("spark.cassandra.output.ttl", ttl.get)

    val batchGroupingBufferSize = parameters.opt(batchGroupingBufferSizeParameter)
    if (batchGroupingBufferSize.isDefined) conf.set("spark.cassandra.output.batch.grouping.buffer.size", batchGroupingBufferSize.get)

    val batchGroupingKey = parameters.opt(batchGroupingKeyParameter)
    if (batchGroupingKey.isDefined) conf.set("spark.cassandra.output.batch.grouping.key", batchGroupingKey.get)

    val batchSizeBytes = parameters.opt(batchSizeBytesParameter)
    if (batchSizeBytes.isDefined) conf.set("spark.cassandra.output.batch.size.bytes", batchSizeBytes.get)

    val batchSizeRows = parameters.opt(batchSizeRowsParameter)
    if (batchSizeRows.isDefined) conf.set("spark.cassandra.output.batch.size.rows", batchSizeRows.get)

    val throughputMBPerSec = parameters.opt(throughputMBPerSecParameter)
    if (throughputMBPerSec.isDefined) conf.set("spark.cassandra.output.throughputMBPerSec", throughputMBPerSec.get)

    val directJoinSetting = parameters.opt(directJoinSettingParameter)
    if (directJoinSetting.isDefined) conf.set("directJoinSetting", directJoinSetting.get)

    val directJoinSizeRatio = parameters.opt(directJoinSizeRatioParameter)
    if (directJoinSizeRatio.isDefined) conf.set("directJoinSizeRatio", directJoinSizeRatio.get.toString)

    val ignoreMissingMetaColumns = parameters.opt(ignoreMissingMetaColumnsParameter)
    if (ignoreMissingMetaColumns.isDefined) conf.set("ignoreMissingMetaColumns", ignoreMissingMetaColumns.get)

    inputDf.write
      .format("cassandra")
      .option("keyspace", parameters(keyspaceParameter))
      .option("table", parameters(tableParameter))
      .save()

    IO.none
  }
}
