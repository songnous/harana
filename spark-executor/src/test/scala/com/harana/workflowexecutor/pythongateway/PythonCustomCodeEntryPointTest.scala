package com.harana.workflowexecutor.pythongateway

import java.util.concurrent.TimeoutException

import scala.concurrent.duration._

import org.apache.spark.SparkContext
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import com.harana.sdk.backend.models.flow.CustomCodeExecutor
import com.harana.sdk.backend.models.flow.DataFrameStorage
import com.harana.sdk.backend.models.flow.ActionExecutionDispatcher
import com.harana.spark.SparkSQLSession
import com.harana.workflowexecutor.customcode.CustomCodeEntryPoint

class PythonCustomCodeEntryPointTest extends AnyWordSpec with MockitoSugar with Matchers {

  "PythonEntryPoint" should {

    "throw on uninitialized code executor" in {
      val entryPoint = createEntryPoint
      a[TimeoutException] shouldBe thrownBy {
        entryPoint.getCodeExecutor(100.millis)
      }
    }

    "throw on uninitialized callback server port" in {
      val entryPoint = createEntryPoint
      a[TimeoutException] shouldBe thrownBy {
        entryPoint.getPythonPort(100.millis)
      }
    }

    "return initialized code executor" in {
      val entryPoint = createEntryPoint
      val mockExecutor = mock[CustomCodeExecutor]
      entryPoint.registerCodeExecutor(mockExecutor)
      entryPoint.getCodeExecutor(100.millis) shouldBe mockExecutor
    }

    "return initialized callback server port" in {
      val entryPoint = createEntryPoint
      entryPoint.registerCallbackServerPort(4412)
      entryPoint.getPythonPort(100.millis) shouldBe 4412
    }

    "return code executor initialized while waiting on it" in {
      val entryPoint   = createEntryPoint
      val mockExecutor = mock[CustomCodeExecutor]

      new Thread(() => {
        Thread.sleep(1000)
        entryPoint.registerCodeExecutor(mockExecutor)
      }).start()

      entryPoint.getCodeExecutor(2.seconds) shouldBe mockExecutor
    }
  }

  private def createEntryPoint = new CustomCodeEntryPoint(
    mock[SparkContext],
    mock[SparkSQLSession],
    mock[DataFrameStorage],
    mock[ActionExecutionDispatcher]
  )
}