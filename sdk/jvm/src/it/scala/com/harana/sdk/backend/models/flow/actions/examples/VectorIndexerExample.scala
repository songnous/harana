package com.harana.sdk.backend.models.flow.actions.examples

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.spark.Linalg.Vectors
import com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators.VectorIndexer

class VectorIndexerExample extends AbstractActionExample[VectorIndexer] {

  def action: VectorIndexer = {
    val op = new VectorIndexer()
    op.estimator.setMaxCategories(3).setInputColumn("vectors").setNoInPlace("indexed")
    op.set(op.estimator.extractParameterMap())
  }

  override def inputDataFrames = {
    val data = Seq(Vectors.dense(1.0, 1.0, 0.0, 1.0), Vectors.dense(0.0, 1.0, 1.0, 1.0), Vectors.dense(-1.0, 1.0, 2.0, 0.0)).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("vectors")))
  }

}