package com.harana.executor.spark.actiontypes.input

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.designer.data.DataSourceTypes.MongoDb.hostParameter
import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.actiontypes.input.GetMongoDbInfo
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.sdk.backend.models.flow.{ActionType, FlowContext}
import com.harana.executor.spark.actiontypes.{dataSourceParameterValues, log}
import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.ReadConfig
import zio.{IO, Task, UIO}

class GetMongoDb extends GetMongoDbInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession
    val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)

    var readConfig = ReadConfig(
      databaseName = parameters(databaseParameter),
      collectionName = parameters(collectionParameter),
      connectionString = Some(s"mongodb://${dsParameterValues(hostParameter).toString}/")
    )

    val sampleSize = parameters.opt(sampleSizeParameter)
    val localThreshold = parameters.opt(localThresholdInMsParameter)
    val readPreference = parameters.opt(readPreferenceParameter)
    val readConcern = parameters.opt(readConcernParameter)
    val samplePoolSize = parameters.opt(samplePoolSizeParameter)
    val batchSize = parameters.opt(batchSizeParameter)

    if (sampleSize.isDefined) readConfig = readConfig.withOption(ReadConfig.sampleSizeProperty, sampleSize.get.toString)
    if (localThreshold.isDefined) readConfig = readConfig.withOption(ReadConfig.localThresholdProperty, localThreshold.get.toString)
    if (readPreference.isDefined) readConfig = readConfig.withOption(ReadConfig.readPreferenceNameProperty, readPreference.get)
    if (readConcern.isDefined) readConfig = readConfig.withOption(ReadConfig.readConcernLevelProperty, readConcern.get)
    if (samplePoolSize.isDefined) readConfig = readConfig.withOption(ReadConfig.samplePoolSizeProperty, samplePoolSize.get.toString)
    if (batchSize.isDefined) readConfig = readConfig.withOption(ReadConfig.batchSizeProperty, batchSize.get.toString)

    val outputDf = MongoSpark.load(spark, readConfig)
    log(outputDf, parameters) *>
    IO.some(new Outputs(Map(outputPorts.head -> outputDf)))
  }
}