package com.harana.sdk.backend.models.flow

import com.harana.sdk.backend.models.flow.Catalog.ActionObjectCatalog
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.backend.models.flow.Catalog.ActionObjectCatalog
import com.harana.sdk.backend.models.flow.inference.InferContext
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame => SparkDataFrame}
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar

trait TestSupport extends MockitoSugar {

  def createInferContext(actionObjectCatalog: ActionObjectCatalog): InferContext = MockedInferContext(
    actionObjectCatalog
  )

  def createExecutionContext: ExecutionContext = {
    val mockedExecutionContext = mock[ExecutionContext]
    val mockedInferContext = mock[InferContext]
    when(mockedExecutionContext.inferContext).thenReturn(mockedInferContext)
    mockedExecutionContext
  }

  def createSchema(fields: Array[String] = Array[String]()): StructType = {
    val schemaMock = mock[StructType]
    when(schemaMock.fieldNames).thenReturn(fields)
    schemaMock
  }

  def createSparkDataFrame(schema: StructType = createSchema()) = {
    val sparkDataFrameMock = mock[SparkDataFrame]
    when(sparkDataFrameMock.schema).thenReturn(schema)
    when(sparkDataFrameMock.toDF).thenReturn(sparkDataFrameMock)
    sparkDataFrameMock
  }

  def createDataFrame(fields: Array[String] = Array[String]()): DataFrame = {
    val schema = createSchema(fields)
    createDataFrame(schema)
  }

  def createDataFrame(schema: StructType): DataFrame = {
    val sparkDataFrameMock = createSparkDataFrame(schema)
    val dataFrameMock = mock[DataFrame]
    when(dataFrameMock.sparkDataFrame).thenReturn(sparkDataFrameMock)
    when(dataFrameMock.schema).thenReturn(Some(schema))
    dataFrameMock
  }
}
