package com.harana.sdk.backend.models.flow

import com.harana.sdk.backend.models.flow
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.scalatestplus.mockito.MockitoSugar._
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.{DataFrame, DataFrameBuilder}
import com.harana.sdk.backend.models.flow.client.datasources.{DatasourceClient, DatasourceInMemoryClientFactory}
import com.harana.sdk.backend.models.flow.filesystemclients.LocalFileSystemClient
import com.harana.sdk.shared.models.flow.ExecutionMode
import com.harana.spark.SparkSQLSession

trait LocalExecutionContext {
  implicit lazy val executionContext: ExecutionContext = LocalExecutionContext.createExecutionContext()
  implicit lazy val sparkContext = LocalExecutionContext.sparkContext
  lazy val sparkSQLSession = LocalExecutionContext.sparkSQLSession
  lazy val createDataFrame = LocalExecutionContext.createDataFrame _
}

object LocalExecutionContext {

  def createDataFrame(rows: Seq[Row], schema: StructType) = {
    val rdd: RDD[Row]  = sparkContext.parallelize(rows)
    val sparkDataFrame = sparkSQLSession.createDataFrame(rdd, schema)
    DataFrame.fromSparkDataFrame(sparkDataFrame)
  }

  lazy val commonExecutionContext = new CommonExecutionContext(
    sparkContext,
    LocalExecutionContext.sparkSQLSession,
    inferContext,
    ExecutionMode.Batch,
    LocalFileSystemClient(),
    "/tmp",
    "/tmp/library",
    mock[InnerWorkflowExecutor],
    mock[DataFrameStorage],
    None,
    mock[CustomCodeExecutionProvider]
  )

  def createExecutionContext(datasourceClient: DatasourceClient = defaultDatasourceClient) =
    flow.ExecutionContext(
      sparkContext,
      LocalExecutionContext.sparkSQLSession,
      flow.MockedInferContext(
        dataFrameBuilder = DataFrameBuilder(LocalExecutionContext.sparkSQLSession),
        datasourceClient = datasourceClient
      ),
      ExecutionMode.Batch,
      LocalFileSystemClient(),
      "/tmp",
      "/tmp/library",
      mock[InnerWorkflowExecutor],
      mock[ContextualDataFrameStorage],
      None,
      new MockedContextualCodeExecutor
    )

  private val defaultDatasourceClient: DatasourceClient =
    new DatasourceInMemoryClientFactory(List.empty).createClient

  private def inferContext = flow.MockedInferContext(
    dataFrameBuilder = DataFrameBuilder(LocalExecutionContext.sparkSQLSession)
  )

  // One per JVM
  private lazy val sparkConf: SparkConf = new SparkConf()
    .setMaster("local[4]")
    .setAppName("TestApp")
    .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .registerKryoClasses(Array())

  lazy val sparkContext: SparkContext = {
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("WARN")
    sc
  }

  lazy val sparkSQLSession: SparkSQLSession = {
    val sqlSession = new SparkSQLSession(sparkContext)
    sqlSession
  }
}
