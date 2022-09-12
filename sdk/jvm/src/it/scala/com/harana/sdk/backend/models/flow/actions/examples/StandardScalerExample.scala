package com.harana.sdk.backend.models.flow.actions.examples

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.spark.Linalg.Vectors
import com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators.StandardScaler

class StandardScalerExample extends AbstractActionExample[StandardScaler] {

  def action: StandardScaler = {
    val op = new StandardScaler()
    op.estimator.setInputColumn("features").setNoInPlace("scaled")
    op.set(op.estimator.extractParameterMap())
  }

  override def inputDataFrames = {
    val data = Array(
      Vectors.dense(-2.0, 2.3, 0.0),
      Vectors.dense(0.0, -5.1, 1.0),
      Vectors.dense(1.7, -0.6, 3.3)
    ).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features")))
  }

}
