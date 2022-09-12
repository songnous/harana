package com.harana.sdk.backend.models.flow.actions.examples

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.spark.wrappers.transformers.Tokenize

class TokenizeExample extends AbstractActionExample[Tokenize] {

  def action = {
    val op = new Tokenize()
    op.transformer.setSingleColumn("sentence", "tokenized")
    op.set(op.transformer.extractParameterMap())
  }

  override def inputDataFrames = {
    val sparkDataFrame = sparkSQLSession
      .createDataFrame(
        Seq(
          (0, "Hi I heard about Spark"),
          (1, "I wish Java could use case classes"),
          (2, "Logistic,regression,models,are,neat")
        )
      )
      .toDF("label", "sentence")
    Seq(DataFrame.fromSparkDataFrame(sparkDataFrame))
  }
}
