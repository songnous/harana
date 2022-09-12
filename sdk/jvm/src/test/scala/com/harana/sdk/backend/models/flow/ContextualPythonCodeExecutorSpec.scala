package com.harana.sdk.backend.models.flow

import com.harana.sdk.shared.models.flow.utils.Id
import scala.concurrent.Future
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter

class ContextualPythonCodeExecutorSpec extends UnitSpec with BeforeAndAfter {

  val workflowId = Id.randomId
  val nodeId = Id.randomId

  val code = "some code"
  val pythonCodeExecutor = mock[CustomCodeExecutor]
  var executor: ContextualCustomCodeExecutor = _

  val actionExecutionDispatcher = mock[ActionExecutionDispatcher]
  val customCodeExecutionProvider = mock[CustomCodeExecutionProvider]

  before {
    executor = new ContextualCustomCodeExecutor(customCodeExecutionProvider, workflowId, nodeId)
    when(customCodeExecutionProvider.pythonCodeExecutor).thenReturn(pythonCodeExecutor)
    when(customCodeExecutionProvider.actionExecutionDispatcher).thenReturn(actionExecutionDispatcher)
  }

  "ContextualPythonCodeExecutor" should {

    "validate code" in {
      when(pythonCodeExecutor.isValid(code)).thenReturn(true)
      executor.isPythonValid(code) shouldBe true
    }

    "execute code" in {
      when(actionExecutionDispatcher.executionStarted(workflowId, nodeId)).thenReturn(Future.successful(Right(())))
      doNothing().when(pythonCodeExecutor).run(workflowId.toString, nodeId.toString, code)
      executor.runPython(code)
    }
  }
}
