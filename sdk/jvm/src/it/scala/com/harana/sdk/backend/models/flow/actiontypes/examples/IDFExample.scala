package com.harana.sdk.backend.models.flow.actiontypes.examples

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.spark.Linalg.Vectors
import com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators.IDF

class IDFExample extends AbstractActionExample[IDF] {

  def action: IDF = {
    val op = new IDF()
    op.estimator.setInputColumn("features").setNoInPlace("values")
    op.set(op.estimator.extractParameterMap())
  }

  override def inputDataFrames = {
    val numOfFeatures = 4
    val data = Seq(
      Vectors.sparse(numOfFeatures, Array(1, 3), Array(1.0, 2.0)).toDense,
      Vectors.dense(0.0, 1.0, 2.0, 3.0),
      Vectors.sparse(numOfFeatures, Array(1), Array(1.0)).toDense
    ).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features")))
  }

}