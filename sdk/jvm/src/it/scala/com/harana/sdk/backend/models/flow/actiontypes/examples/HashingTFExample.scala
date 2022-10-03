package com.harana.sdk.backend.models.flow.actiontypes.examples

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.transformers.HashingTF

class HashingTFExample extends AbstractActionExample[HashingTF] {

  def action: HashingTF = {
    val op = new HashingTF()
    op.transformer.setSingleColumn("signal", "hash").setNumFeatures(5)
    op.set(op.transformer.extractParameterMap())
  }

  override def inputDataFrames = {
    val data = Seq("a a b b c d".split(" ").toSeq).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("signal")))
  }
}
