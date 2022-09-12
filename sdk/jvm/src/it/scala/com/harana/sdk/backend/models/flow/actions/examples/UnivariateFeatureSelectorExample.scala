package com.harana.sdk.backend.models.flow.actions.examples

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.spark.Linalg.Vectors
import com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators.UnivariateFeatureSelector
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class UnivariateFeatureSelectorExample extends AbstractActionExample[UnivariateFeatureSelector] {

  def action: UnivariateFeatureSelector = {
    val op = new UnivariateFeatureSelector()
    op.estimator
      .setFeaturesColumn(NameSingleColumnSelection("features"))
      .setLabelColumn(NameSingleColumnSelection("label"))
      .setOutputColumn("selected_features")
      .setNumTopFeatures(1)
    op.set(op.estimator.extractParameterMap())
  }

  override def inputDataFrames = {
    val data = Seq(
      (Vectors.dense(0.0, 0.0, 18.0, 1.0), 1.0),
      (Vectors.dense(0.0, 1.0, 12.0, 0.0), 0.0),
      (Vectors.dense(1.0, 0.0, 15.0, 0.1), 0.0)
    )
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features", "label")))
  }

}