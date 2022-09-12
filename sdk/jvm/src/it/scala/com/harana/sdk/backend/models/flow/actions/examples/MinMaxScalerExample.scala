package com.harana.sdk.backend.models.flow.actions.examples

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.spark.Linalg.Vectors
import com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators.MinMaxScaler

class MinMaxScalerExample extends AbstractActionExample[MinMaxScaler] {

  def action: MinMaxScaler = {
    val op = new MinMaxScaler()
    op.estimator.setInputColumn("features").setNoInPlace("scaled").setMax(5).setMin(-5)
    op.set(op.estimator.extractParameterMap())
  }

  override def inputDataFrames = {
    val data = Array(
      Vectors.dense(1, 0, Long.MinValue),
      Vectors.dense(2, 0, 0),
      Vectors.sparse(3, Array(0, 2), Array(3, Long.MaxValue)).toDense,
      Vectors.sparse(3, Array(0), Array(1.5)).toDense
    ).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features")))
  }

}