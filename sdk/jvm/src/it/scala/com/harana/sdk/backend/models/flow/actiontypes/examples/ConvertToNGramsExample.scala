package com.harana.sdk.backend.models.flow.actiontypes.examples

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.transformers.ConvertToNGrams

class ConvertToNGramsExample extends AbstractActionExample[ConvertToNGrams] {

  def action: ConvertToNGrams = {
    val op = new ConvertToNGrams()
    op.transformer.setSingleColumn("words", "output").setN(3)
    op.set(op.transformer.extractParameterMap())
  }

  override def inputDataFrames = {
    Seq(
      DataFrame.fromSparkDataFrame(
        sparkSQLSession
          .createDataFrame(
            Seq(
              (0, Array("Hi", "I", "heard", "about", "Spark")),
              (1, Array("I", "wish", "Java", "could", "use", "case", "classes")),
              (2, Array("Logistic", "regression", "models", "are", "neat"))
            )
          )
          .toDF("label", "words")
      )
    )
  }
}
