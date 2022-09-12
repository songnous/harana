package com.harana.sdk.backend.models.flow

import com.harana.sdk.shared.models.flow.utils.Id
import org.apache.spark.sql.{DataFrame => SparkDataFrame}

trait DataFrameStorage {
  def getInputDataFrame(workflowId: Id, nodeId: Id, portNumber: Int): Option[SparkDataFrame]
  def setInputDataFrame(workflowId: Id, nodeId: Id, portNumber: Int, dataFrame: SparkDataFrame): Unit

  def removeNodeInputDataFrames(workflowId: Id, nodeId: Id, portNumber: Int): Unit
  def removeNodeInputDataFrames(workflowId: Id, nodeId: Id): Unit

  def getOutputDataFrame(workflowId: Id, nodeId: Id, portNumber: Int): Option[SparkDataFrame]
  def setOutputDataFrame(workflowId: Id, nodeId: Id, portNumber: Int, dataFrame: SparkDataFrame): Unit

  def removeNodeOutputDataFrames(workflowId: Id, nodeId: Id): Unit
}

object DataFrameStorage {
  type DataFrameName = String
  type DataFrameId = (Id, DataFrameName)
}
