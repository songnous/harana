package com.harana.sdk.backend.models.flow.actiontypes.examples

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.transformers.RemoveStopWords

class RemoveStopWordsExample extends AbstractActionExample[RemoveStopWords] {

  def action: RemoveStopWords = {
    val op = new RemoveStopWords()
    op.transformer.setSingleColumn("raw", "removed")
    op.set(op.transformer.extractParameterMap())
  }

  override def inputDataFrames = {
    val sparkDataFrame = sparkSQLSession
      .createDataFrame(
        Seq(
          (0, Seq("I", "saw", "the", "red", "baloon")),
          (1, Seq("Mary", "had", "a", "little", "lamb"))
        )
      )
      .toDF("id", "raw")
    Seq(DataFrame.fromSparkDataFrame(sparkDataFrame))
  }
}
