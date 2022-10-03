package com.harana.sdk.backend.models.flow.actiontypes.examples

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators.Word2Vec

class Word2VecExample extends AbstractActionExample[Word2Vec] {

  def action: Word2Vec = {
    val op = new Word2Vec()
    op.estimator
      .setInputColumn("words")
      .setNoInPlace("vectors")
      .setMinCount(2)
      .setVectorSize(5)
    op.set(op.estimator.extractParameterMap())
  }

  override def inputDataFrames = {
    val data = Seq(
      "Lorem ipsum at dolor".split(" "),
      "Nullam gravida non ipsum".split(" "),
      "Etiam at nunc lacinia".split(" ")
    ).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("words")))
  }

}