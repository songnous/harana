package com.harana.executor.spark.actiontypes.input.next

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.designer.data.DataSourceTypes.Elasticsearch._
import com.harana.sdk.backend.models.designer.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.designer.flow.execution.ExecutionError
import com.harana.sdk.backend.models.designer.flow.{ActionType, FlowContext}
import com.harana.sdk.backend.models.designer.flow.actiontypes.input.next.GetElasticsearchInfo
import com.harana.sdk.backend.models.designer.flow.actiontypes.pathParameter
import com.harana.executor.spark.actiontypes.dataSourceParameterValues
import zio.{IO, Task, UIO}

class GetElasticsearch extends GetElasticsearchInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession
    val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)

    val hosts = dsParameterValues(hostsParameter)
    val ssl = dsParameterValues(sslParameter)
    val username = dsParameterValues(usernameParameter)
    val password = dsParameterValues(passwordParameter)

    val queryRetries = dsParameterValues(queryRetriesParameter)
    val queryTimeout = dsParameterValues(queryTimeoutParameter)
    val discovery = dsParameterValues(discoveryParameter)
    val clientOnly = dsParameterValues(clientOnlyParameter)
    val pathPrefix = dsParameterValues(pathPrefixParameter)
    val autoCreateIndex = dsParameterValues(autoCreateIndexParameter)
    val dataNodesOnly = dsParameterValues(dataNodesOnlyParameter)
    val ingestNodesOnly = dsParameterValues(ingestNodesOnlyParameter)
    val wanNodesOnly = dsParameterValues(wanNodesOnlyParameter)
    val resolveHostname = dsParameterValues(resolveHostnameParameter)

    val path = parameters(pathParameter)
    val query = parameters(queryParameter)
    val includeMetadata = parameters(includeMetadataParameter)
    val includeMetadataVersion = parameters(includeMetadataVersionParameter)
    val metadataField = parameters(metadataFieldParameter)
    val pushdown = parameters(pushdownParameter)
    val fieldsToInclude = parameters(fieldsToIncludeParameter)
    val fieldsToExclude = parameters(fieldsToExcludeParameter)
    val fieldsToIncludeAsArray = parameters(fieldsToIncludeAsArrayParameter)
    val fieldsToExcludeAsArray = parameters(fieldsToExcludeAsArrayParameter)
    
    
    IO.none
  }
}