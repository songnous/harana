package com.harana.sdk.backend.models.flow.actions.examples

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.spark.Linalg.Vectors
import com.harana.sdk.backend.models.flow.actions.spark.wrappers.transformers.DCT

class DCTExample extends AbstractActionExample[DCT] {

  def action: DCT = {
    val op = new DCT()
    op.transformer.setSingleColumn("features", "output")
    op.set(op.transformer.extractParameterMap())
  }

  override def inputDataFrames = {
    val data = Seq(Vectors.dense(0.0, 1.0, -2.0, 3.0), Vectors.dense(-1.0, 2.0, 4.0, -7.0), Vectors.dense(14.0, -2.0, -5.0, 1.0)).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features")))
  }
}
