package com.harana.sdk.backend.models.flow.actiontypes.examples

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.spark.Linalg.Vectors
import com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.transformers.PolynomialExpand

class PolynomialExpandExample extends AbstractActionExample[PolynomialExpand] {

  def action: PolynomialExpand = {
    val op = new PolynomialExpand()
    op.transformer.setSingleColumn("input", "expanded")
    op.set(op.transformer.extractParameterMap())
  }

  override def inputDataFrames = {
    val data = Seq(
      Vectors.sparse(3, Seq((0, -2.0), (1, 2.3))).toDense,
      Vectors.dense(-2.0, 2.3),
      Vectors.dense(0.0, 0.0, 0.0),
      Vectors.dense(0.6, -1.1, -3.0),
      Vectors.sparse(3, Seq()).toDense
    ).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("input")))
  }

}