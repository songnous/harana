package com.harana.workflowexecutor.session.storage

import com.harana.sdk.backend.models.designer.flow.DataFrameStorage
import com.harana.sdk.backend.models.designer.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.designer.flow.json.StandardSpec
import com.harana.sdk.shared.models.designer.flow.utils.Id
import org.apache.spark.sql.{DataFrame => SparkDataFrame}
import org.scalatest.BeforeAndAfter
import org.scalatestplus.mockito.MockitoSugar

class DataFrameStorageSpec extends StandardSpec with BeforeAndAfter with MockitoSugar {
  val workflow1Id = Id.randomId
  val workflow2Id = Id.randomId
  val node1Id = Id.randomId
  val node2Id = Id.randomId
  val dataframe1Id = "dataframe1"
  val dataframe2Id = "dataframe2"
  val dataframe3Id = "dataframe3"
  val dataframe1 = mock[DataFrame]
  val dataframe2 = mock[DataFrame]
  val dataframe3 = mock[DataFrame]
  val sparkDataFrame1 = mock[SparkDataFrame]
  val sparkDataFrame2 = mock[SparkDataFrame]
  val sparkDataFrame3 = mock[SparkDataFrame]
  var storage: DataFrameStorage = _

  before {
    storage = new DataFrameStorageImpl
  }

  "DataFrameStorage" should {

    "register input dataFrames" in {
      storage.setInputDataFrame(workflow1Id, node1Id, 0, sparkDataFrame1)
      storage.setInputDataFrame(workflow1Id, node2Id, 0, sparkDataFrame2)
      storage.setInputDataFrame(workflow1Id, node2Id, 1, sparkDataFrame3)

      storage.getInputDataFrame(workflow1Id, node1Id, 0) shouldBe Some(sparkDataFrame1)
      storage.getInputDataFrame(workflow1Id, node1Id, 1) shouldBe None
      storage.getInputDataFrame(workflow1Id, node2Id, 0) shouldBe Some(sparkDataFrame2)
      storage.getInputDataFrame(workflow1Id, node2Id, 1) shouldBe Some(sparkDataFrame3)
      storage.getInputDataFrame(workflow2Id, node2Id, 2) shouldBe None
    }

    "delete input dataFrames" in {
      storage.setInputDataFrame(workflow1Id, node1Id, 0, sparkDataFrame1)
      storage.setInputDataFrame(workflow1Id, node2Id, 0, sparkDataFrame2)
      storage.setInputDataFrame(workflow1Id, node2Id, 1, sparkDataFrame3)
      storage.setInputDataFrame(workflow2Id, node2Id, 0, sparkDataFrame3)

      storage.removeNodeInputDataFrames(workflow1Id, node2Id, 0)

      storage.getInputDataFrame(workflow1Id, node1Id, 0) shouldBe Some(sparkDataFrame1)
      storage.getInputDataFrame(workflow1Id, node1Id, 1) shouldBe None
      storage.getInputDataFrame(workflow1Id, node2Id, 0) shouldBe None
      storage.getInputDataFrame(workflow1Id, node2Id, 1) shouldBe Some(sparkDataFrame3)
      storage.getInputDataFrame(workflow2Id, node2Id, 2) shouldBe None
      storage.getInputDataFrame(workflow2Id, node2Id, 0) shouldBe Some(sparkDataFrame3)
    }

    "register output dataFrames" in {
      storage.setOutputDataFrame(workflow1Id, node1Id, 0, sparkDataFrame1)
      storage.setOutputDataFrame(workflow1Id, node2Id, 0, sparkDataFrame2)
      storage.setOutputDataFrame(workflow1Id, node2Id, 1, sparkDataFrame3)

      storage.getOutputDataFrame(workflow1Id, node1Id, 0) shouldBe Some(sparkDataFrame1)
      storage.getOutputDataFrame(workflow1Id, node1Id, 1) shouldBe None
      storage.getOutputDataFrame(workflow1Id, node2Id, 0) shouldBe Some(sparkDataFrame2)
      storage.getOutputDataFrame(workflow1Id, node2Id, 1) shouldBe Some(sparkDataFrame3)
      storage.getOutputDataFrame(workflow2Id, node2Id, 2) shouldBe None
    }

    "delete some output dataFrames" in {
      storage.setOutputDataFrame(workflow1Id, node1Id, 0, sparkDataFrame1)
      storage.setOutputDataFrame(workflow1Id, node2Id, 0, sparkDataFrame2)
      storage.setOutputDataFrame(workflow1Id, node2Id, 1, sparkDataFrame3)
      storage.setOutputDataFrame(workflow2Id, node2Id, 1, sparkDataFrame3)

      storage.removeNodeOutputDataFrames(workflow1Id, node2Id)

      storage.getOutputDataFrame(workflow1Id, node1Id, 0) shouldBe Some(sparkDataFrame1)
      storage.getOutputDataFrame(workflow1Id, node2Id, 0) shouldBe None
      storage.getOutputDataFrame(workflow1Id, node2Id, 1) shouldBe None
      storage.getOutputDataFrame(workflow2Id, node2Id, 1) shouldBe Some(sparkDataFrame3)
    }
  }
}