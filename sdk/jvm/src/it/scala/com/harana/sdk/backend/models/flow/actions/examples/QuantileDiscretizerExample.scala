package com.harana.sdk.backend.models.flow.actions.examples

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators.QuantileDiscretizer

class QuantileDiscretizerExample extends AbstractActionExample[QuantileDiscretizer] {

  def action: QuantileDiscretizer = {
    val op = new QuantileDiscretizer()
    op.estimator.setInputColumn("features").setNoInPlace("discretized_features").setNumBuckets(3)
    op.set(op.estimator.extractParameterMap())
  }

  override def inputDataFrames = {
    val data = Array(1.0, 2.0, 3.0, 4.0, 5.0, 6.0).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features")))
  }

}