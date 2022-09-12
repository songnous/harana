package com.harana.workflowexecutor.pythongateway

import com.harana.sdk.backend.models.designer.flow.ActionExecutionDispatcher
import com.harana.sdk.backend.models.designer.flow.json.StandardSpec
import com.harana.sdk.shared.models.designer.flow.utils.Id
import org.scalatest.BeforeAndAfter
import org.scalatestplus.mockito.MockitoSugar

class ActionExecutionDispatcherSpec extends StandardSpec with MockitoSugar with BeforeAndAfter {
  val workflowId = Id.randomId
  val nodeId = Id.randomId
  var dispatcher: ActionExecutionDispatcher = _

  before {
    dispatcher = new ActionExecutionDispatcher
  }

  "ActionExecutionDispatcher" should {

    "execute action and finish" when {

      "notified of success with proper workflow and node id" in {
        val future = dispatcher.executionStarted(workflowId, nodeId)
        future.isCompleted shouldBe false

        dispatcher.executionEnded(workflowId, nodeId, Right(()))
        future.isCompleted shouldBe true
      }

      "notified of failure with proper workflow and node id" in {
        val future = dispatcher.executionStarted(workflowId, nodeId)
        future.isCompleted shouldBe false

        dispatcher.executionEnded(workflowId, nodeId, Left("A stacktrace"))
        future.isCompleted shouldBe true
        future.value.get.get shouldBe Left("A stacktrace")
      }
    }

    "throw an exception" when {

      "multiple executions of the same node are started" in {
        dispatcher.executionStarted(workflowId, nodeId)

        an[IllegalArgumentException] shouldBe thrownBy {
          dispatcher.executionStarted(workflowId, nodeId)
        }
      }

      "notified with non-existing workflow id" in {
        val future = dispatcher.executionStarted(workflowId, nodeId)
        future.isCompleted shouldBe false

        an[IllegalArgumentException] shouldBe thrownBy {
          dispatcher.executionEnded(Id.randomId, nodeId, Right(()))
        }
      }

      "notified with non-existing node id" in {
        val future = dispatcher.executionStarted(workflowId, nodeId)
        future.isCompleted shouldBe false

        an[IllegalArgumentException] shouldBe thrownBy {
          dispatcher.executionEnded(workflowId, Id.randomId, Right(()))
        }
      }
    }
  }
}