package com.harana.designer.shared.flows.actiontypes.spark.output

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup, ParameterValue}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes
import com.harana.designer.shared.flows.actiontypes.spark.logGroup

class PutMongoDbInfo extends OutputActionTypeInfo {

  val tags = Set()
  val dataSourceType = DataSourceTypes.MongoDb

  // General
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val databaseParameter = Parameter.String("database", required = true)
  val collectionParameter = Parameter.String("collection", required = true)
  val generalGroup = ParameterGroup("general", List(dataSourceParameter, databaseParameter, collectionParameter))

  // Advanced
  val extendedBsonTypesParameter = Parameter.Boolean("extended-bson-types", Some(ParameterValue.Boolean(true)))
  val localThresholdInMsParameter = Parameter.Integer("local-threshold", Some(ParameterValue.Integer(15)))
  val replaceDocumentParameter = Parameter.Boolean("replace-document", Some(ParameterValue.Boolean(true)))
  val maxBatchSizeParameter = Parameter.Integer("max-batch-size", Some(ParameterValue.Integer(512)))
  val writeConcernParameter = Parameter.String("write-concern", Some(ParameterValue.String("majority")))
  val writeConcernAcknowledgeJournalParameter = Parameter.Boolean("write-concern-acknowledge-journal", Some(ParameterValue.Boolean(true)))
  val writeConcernTimeoutParameter = Parameter.Integer("write-concern-timeout")

  val shardKeyParameter = Parameter.String("shard-key", Some(ParameterValue.String("_id")))
  val forceInsertParameter = Parameter.Boolean("force-insert", Some(ParameterValue.Boolean(false)))
  val orderedParameter = Parameter.Boolean("ordered", Some(ParameterValue.Boolean(true)))

  val parameterGroups = List(ParameterGroup("general", List(dataSourceParameter)), logGroup)

}