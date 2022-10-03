package com.harana.sdk.backend.models.flow.actiontypes.examples

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.ArrayType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.{DataFrame, DataFrameBuilder}
import com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators.CountVectorizer

class CountVectorizerExample extends AbstractActionExample[CountVectorizer] {

  def action: CountVectorizer = {
    val op = new CountVectorizer()
    op.estimator.setInputColumn("lines").setNoInPlace("lines_out").setMinTF(3)
    op.set(op.estimator.extractParameterMap())
  }

  override def inputDataFrames = {
    val rows = Seq(
      Row("a a a b b c c c d ".split(" ").toSeq),
      Row("c c c c c c".split(" ").toSeq),
      Row("a".split(" ").toSeq),
      Row("e e e e e".split(" ").toSeq)
    )
    val rdd = sparkContext.parallelize(rows)
    val schema = StructType(Seq(StructField("lines", ArrayType(StringType, containsNull = true))))
    Seq(DataFrameBuilder(sparkSQLSession).buildDataFrame(schema, rdd))
  }
}
