package com.harana.sdk.backend.models.flow.actiontypes.examples

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.spark.Linalg.Vectors
import com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators.PCA

class PCAExample extends AbstractActionExample[PCA] {

  def action: PCA = {
    val op = new PCA()
    op.estimator.setInputColumn("features").setNoInPlace("pca_features").setK(3)
    op.set(op.estimator.extractParameterMap())
  }

  override def inputDataFrames = {
    val data = Array(
      Vectors.sparse(5, Seq((1, 1.0), (3, 7.0))).toDense,
      Vectors.dense(2.0, 0.0, 3.0, 4.0, 5.0),
      Vectors.dense(4.0, 0.0, 0.0, 6.0, 7.0)
    ).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features")))
  }
}
