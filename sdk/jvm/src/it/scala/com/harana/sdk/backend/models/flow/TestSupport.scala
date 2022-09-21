package com.harana.sdk.backend.models.flow

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.utils.DataFrameMatchers
import com.harana.sdk.shared.models.flow.utils.Id
import com.harana.spark.spi.SparkSessionInitializer
import org.apache.spark.sql.SparkSession
import org.scalatest.BeforeAndAfterAll

import scala.concurrent.Future
import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

trait IntegratedTestSupport extends UnitSpec with BeforeAndAfterAll with LocalExecutionContext {
  def executeAction(op: ActionType, dfs: DataFrame*) = op.executeUntyped(dfs.toList)(executionContext).head.asInstanceOf[DataFrame]
  def createDir(path: String) = new java.io.File(path + "/id").getParentFile.mkdirs()
  def createDataFrame[T <: Product: TypeTag: ClassTag](seq: Seq[T]) = DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(sparkContext.parallelize(seq)))
}

object IntegratedTestSupport extends UnitSpec with DataFrameMatchers {}

class TestUDFRegistrator extends SparkSessionInitializer {
  val myOp = (d: Double) => d.toInt
  def init(sparkSession: SparkSession) = sparkSession.udf.register("myOp", myOp)
}

private class MockedCodeExecutor extends CustomCodeExecutor {
  def isValid(code: String) = true
  def run(workflowId: String, nodeId: String, code: String) = ()
}

class MockedContextualCodeExecutor extends ContextualCustomCodeExecutor(new MockedCustomCodeExecutionProvider, Id.randomId, Id.randomId)

private class MockedCustomCodeExecutionProvider
    extends CustomCodeExecutionProvider(
      new MockedCodeExecutor,
      new MockedCodeExecutor,
      new MockedCustomActionExecutor)

private class MockedCustomActionExecutor extends ActionExecutionDispatcher {
  override def executionStarted(workflowId: Id, nodeId: Id) = Future.successful(Right(()))
}
