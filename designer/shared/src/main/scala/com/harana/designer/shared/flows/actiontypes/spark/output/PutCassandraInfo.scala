package com.harana.designer.shared.flows.actiontypes.spark.output

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.designer.shared.flows.actiontypes.spark.{ActionTypeGroup, logGroup}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes

class PutCassandraInfo extends OutputActionTypeInfo {

  val tags = Set()
  val dataSourceType = DataSourceTypes.Cassandra

  // General
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val keyspaceParameter = Parameter.String("keyspace", required = true)
  val tableParameter = Parameter.String("table", required = true)
  val generalGroup = ParameterGroup("general", List(dataSourceParameter, keyspaceParameter, tableParameter))

  // Advanced
  val consistencyLevelParameter = Parameter.String("consistency-level", options =
    List(
      ("all", "ALL"),
      ("each-quorum", "EACH_QUORUM"),
      ("quorum", "QUORUM"),
      ("local-quorum", "LOCAL_QUORUM"),
      ("one", "ONE"),
      ("two", "TWO"),
      ("local-one", "LOCAL_ONE"),
      ("any", "ANY")
    ))
  val ttlParameter = Parameter.Long("ttl", placeholder = Some(0))
  val concurrentWritesParameter = Parameter.Long("concurrent-writes", placeholder = Some(5))
  val ignoreNullsParameter = Parameter.Boolean("ignore-nulls")
  val timestampParameter = Parameter.Long("timestamp", placeholder = Some(0))
  val batchGroupingBufferSizeParameter = Parameter.Long("batch-grouping-buffer-size", placeholder = Some(1000))
  val batchGroupingKeyParameter = Parameter.String("batch-grouping-key", options =
    List(
      ("none", "none"),
      ("replica-set", "replica_set"),
      ("partition", "partition")
    ))

  val batchSizeBytesParameter = Parameter.Long("batch-size-bytes", placeholder = Some(1024))
  val batchSizeRowsParameter = Parameter.Long("batch-size-rows")
  val throughputMBPerSecParameter = Parameter.Long("throughput-mb-per-sec")
  val directJoinSettingParameter = Parameter.String("direct-join-setting", options =
    List(
      ("on", "on"),
      ("off", "off"),
      ("auto", "auto")
    ))
  val directJoinSizeRatioParameter = Parameter.Decimal("direct-join-size-ratio")
  val ignoreMissingMetaColumnsParameter = Parameter.Boolean("ignore-missing-meta-columns")

  val advancedGroup = ParameterGroup("advanced", List(
    consistencyLevelParameter, ttlParameter, concurrentWritesParameter, ignoreNullsParameter,
    timestampParameter, batchGroupingBufferSizeParameter, batchGroupingKeyParameter, batchSizeBytesParameter, throughputMBPerSecParameter,
    directJoinSettingParameter, directJoinSizeRatioParameter, ignoreMissingMetaColumnsParameter
  ))

  val parameterGroups = List(generalGroup, advancedGroup, logGroup)
}