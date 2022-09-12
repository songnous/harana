package com.harana.workflowexecutor.executor

import com.harana.sdk.backend.models.designer.flow._
import com.harana.sdk.backend.models.designer.flow.actionobjects.dataframe.DataFrameBuilder
import com.harana.sdk.backend.models.designer.flow.client.datasources.DatasourceClientFactory
import com.harana.sdk.backend.models.designer.flow.mail.EmailSender
import com.harana.sdk.backend.models.designer.flow.utils.Logging
import com.harana.sdk.shared.BuildInfo
import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.designer.flow
import com.harana.sdk.shared.models.designer.flow.{ActionObjectCatalog, ExecutionMode}
import com.harana.spark.SparkSQLSession
import org.apache.spark.{SparkConf, SparkContext}

trait Executor extends Logging {

  def currentVersion: Version =
    Version(BuildInfo.apiVersionMajor, BuildInfo.apiVersionMinor, BuildInfo.apiVersionPatch)

  def createExecutionContext(
      dataFrameStorage: DataFrameStorage,
      executionMode: ExecutionMode,
      emailSender: Option[EmailSender],
      datasourceClientFactory: DatasourceClientFactory,
      customCodeExecutionProvider: CustomCodeExecutionProvider,
      sparkContext: SparkContext,
      sparkSQLSession: SparkSQLSession,
      tempPath: String,
      libraryPath: String,
      actionObjectCatalog: Option[ActionObjectCatalog] = None
  ): CommonExecutionContext = {

    val innerWorkflowExecutor = new InnerWorkflowExecutorImpl
    val inferContext = flow.inference.InferContext(
      DataFrameBuilder(sparkSQLSession),
      datasourceClientFactory.createClient
    )

    CommonExecutionContext(
      sparkContext,
      sparkSQLSession,
      inferContext,
      executionMode,
      FileSystemClientStub(), // temporarily mocked
      tempPath,
      libraryPath,
      innerWorkflowExecutor,
      dataFrameStorage,
      emailSender,
      customCodeExecutionProvider
    )
  }

  def createSparkContext(): SparkContext = {
    val sparkConf = new SparkConf()
    sparkConf
      .setAppName("Harana Workflow Executor")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .registerKryoClasses(Array())

    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("WARN")
    sc
  }

  def createSparkSQLSession(sparkContext: SparkContext): SparkSQLSession = {
    val sparkSQLSession = new SparkSQLSession(sparkContext)
    sparkSQLSession
  }
}

object Executor extends Executor
