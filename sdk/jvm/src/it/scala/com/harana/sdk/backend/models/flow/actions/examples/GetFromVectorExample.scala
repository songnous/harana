package com.harana.sdk.backend.models.flow.actions.examples

import com.harana.spark.Linalg.Vectors
import com.harana.sdk.backend.models.flow.actionobjects.GetFromVectorTransformer
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.GetFromVector

class GetFromVectorExample extends AbstractActionExample[GetFromVector] {

  def action: GetFromVector = {
    val op = new GetFromList.empty
    op.transformer.setIndex(1)
    op.transformer.setSingleColumn("features", "second_feature")
    op.set(op.transformer.extractParameterMap())
  }

  override def inputDataFrames = {
    val data = Seq(
      Vectors.sparse(3, Seq((0, -2.0), (1, 2.3))),
      Vectors.dense(0.01, 0.2, 3.0),
      null,
      Vectors.sparse(3, Seq((1, 0.91), (2, 3.2))),
      Vectors.sparse(3, Seq((0, 5.7), (2, 2.7))),
      Vectors.sparse(3, Seq()).toDense
    ).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features")))
  }
}
