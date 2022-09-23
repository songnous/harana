package com.harana.executor.spark.actiontypes.output

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.designer.data.DataSourceTypes.MongoDb.hostParameter
import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.actiontypes.output.PutMongoDbInfo
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.sdk.backend.models.flow.{Action, FlowContext}
import com.harana.executor.spark.actiontypes.{dataSourceParameterValues, log}
import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.{WriteConcernConfig, WriteConfig}
import zio.{IO, Task, UIO}

class PutMongoDb extends PutMongoDbInfo with Action {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)

    val inputDf = inputs(inputPorts.head)
    log(inputDf, parameters)

    val writeConfig = WriteConfig(
      databaseName = parameters(databaseParameter),
      collectionName = parameters(collectionParameter),
      connectionString = Some(s"mongodb://${dsParameterValues(hostParameter).toString}/"),
      replaceDocument = parameters(replaceDocumentParameter).value,
      maxBatchSize = parameters(maxBatchSizeParameter),
      localThreshold = parameters(localThresholdInMsParameter),
      writeConcernConfig = WriteConcernConfig(),
      shardKey = Some(parameters(shardKeyParameter)),
      forceInsert = parameters(forceInsertParameter),
      ordered = parameters(orderedParameter)
    )

    MongoSpark.save(inputDf, writeConfig)
    IO.none
  }
}