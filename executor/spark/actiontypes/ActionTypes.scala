package com.harana.executor.spark.actiontypes

import com.harana.sdk.backend.models.designer.flow.ActionTypeInfo
import com.harana.executor.spark.actiontypes.input._
import com.harana.executor.spark.actiontypes.input.next.GetElasticsearch
import com.harana.executor.spark.actiontypes.output._
import com.harana.executor.spark.actiontypes.output.next.PutElasticsearch
import com.harana.executor.spark.actiontypes.query._
import com.harana.executor.spark.actiontypes.transform._

object ActionTypes {

  private val actionTypes = List(
    new GetCassandra,
    new GetElasticsearch,
    new GetHaranaFiles,
    new GetMongoDb,
    new GetMySql,
    new GetOracle,
    new GetPostgreSql,
    new GetRedshift,
    new GetS3,
    new GetSftp,
    new GetSnowflake,
    new GetSqlServer,
    new PutCassandra,
    new PutElasticsearch,
    new PutHaranaFiles,
    new PutMongoDb,
    new PutMySql,
    new PutOracle,
    new PutPostgreSql,
    new PutRedshift,
    new PutS3,
    new PutSftp,
    new PutSnowflake,
    new PutSqlServer,
    new ExecuteSQL,
    new AddColumn,
    new Deduplicate,
    new Distinct,
    new DropColumns,
    new Filter,
    new Fork,
    new Join,
    new Merge,
    new RenameColumn,
    new Repartition,
    new SelectColumns,
    new StandardiseColumnNames,
    new Subtract,
    new Transpose
  )

  private val actionTypesById = actionTypes.map(at => at.getClass.getSimpleName -> at).toMap

  def list = actionTypes

  def get(name: String) = actionTypesById(name)

  def get(actionTypeInfo: ActionTypeInfo) =
    actionTypesById(name(actionTypeInfo))

  def name(actionTypeInfo: ActionTypeInfo) = {
    val cls = actionTypeInfo.getClass.getSimpleName
    cls.substring(0, cls.length - 4)
  }
}