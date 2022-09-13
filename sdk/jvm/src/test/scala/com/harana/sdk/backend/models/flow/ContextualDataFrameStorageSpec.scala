package com.harana.sdk.backend.models.flow

import org.mockito.Mockito._
import org.mockito.ArgumentMatchers.any
import org.scalatest.BeforeAndAfter
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.exceptions.CustomActionExecutionError
import com.harana.sdk.backend.models.flow.actions.exceptions.CustomActionExecutionError
import com.harana.sdk.shared.models.flow.utils.Id

class ContextualDataFrameStorageSpec extends UnitSpec with BeforeAndAfter with TestSupport {

  val workflowId = Id.randomId
  val nodeId = Id.randomId
  val portNumber = 332
  val dataFrame = createDataFrame()
  val sparkDataFrame = dataFrame.sparkDataFrame
  var dataFrameStorage: DataFrameStorage = _
  var storage: ContextualDataFrameStorage = _

  before {
    dataFrameStorage = mock[DataFrameStorage]
    storage = new ContextualDataFrameStorage(dataFrameStorage, workflowId, nodeId)
  }

  "ContextualDataFrameStorage" should {

    "store input dataFrame" in {
      storage.setInputDataFrame(portNumber, sparkDataFrame)
      verify(dataFrameStorage).setInputDataFrame(workflowId, nodeId, portNumber, sparkDataFrame)
    }

    "delete input dataFrame" in {
      storage.removeNodeInputDataFrames(portNumber)
      verify(dataFrameStorage).removeNodeInputDataFrames(workflowId, nodeId, portNumber)
    }

    "store output dataFrame" in {
      storage.setOutputDataFrame(portNumber, sparkDataFrame)
      verify(dataFrameStorage).setOutputDataFrame(workflowId, nodeId, portNumber, sparkDataFrame)
    }

    "get output dataFrame" in {
      when(dataFrameStorage.getOutputDataFrame(workflowId, nodeId, portNumber)).thenReturn(Some(sparkDataFrame))
      storage.getOutputDataFrame(portNumber) shouldBe Some(sparkDataFrame)
    }

    "delete output dataFrame" in {
      storage.removeNodeOutputDataFrames()
      verify(dataFrameStorage).removeNodeOutputDataFrames(workflowId, nodeId)
    }

    "return data frame" in {
      val dataFrame    = mock[DataFrame]
      val retDataFrame = storage.withInputDataFrame(portNumber, sparkDataFrame)(dataFrame)

      assert(retDataFrame == dataFrame)
      verify(dataFrameStorage).setInputDataFrame(workflowId, nodeId, portNumber, sparkDataFrame)
      verify(dataFrameStorage).removeNodeInputDataFrames(workflowId, nodeId, portNumber)
    }

    "throw an exception" in {
      intercept[CustomActionExecutionError] {
        storage.withInputDataFrame(portNumber, sparkDataFrame) {
          throw CustomActionExecutionError("Test exception").toException
        }
      }

      verify(dataFrameStorage).setInputDataFrame(workflowId, nodeId, portNumber, sparkDataFrame)
      verify(dataFrameStorage).removeNodeInputDataFrames(workflowId, nodeId, portNumber)
    }

    "throw exception thrown by removeInputDataFrames and not from block" in {
      when(dataFrameStorage.removeNodeInputDataFrames(any(), any(), any())).thenThrow(new RuntimeException())

      intercept[RuntimeException] {
        storage.withInputDataFrame(portNumber, sparkDataFrame) {
          throw CustomActionExecutionError("Test exception").toException
        }
      }

      verify(dataFrameStorage).setInputDataFrame(workflowId, nodeId, portNumber, sparkDataFrame)
      verify(dataFrameStorage).removeNodeInputDataFrames(workflowId, nodeId, portNumber)
    }
  }
}