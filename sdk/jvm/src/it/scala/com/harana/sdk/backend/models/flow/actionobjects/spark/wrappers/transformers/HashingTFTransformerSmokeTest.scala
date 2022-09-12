package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import org.apache.spark.ml.feature.{HashingTF => SparkHashingTF}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection
import com.harana.spark.Linalg

class HashingTFTransformerSmokeTest
    extends AbstractTransformerWrapperSmokeTest[HashingTFTransformer]
    with MultiColumnTransformerWrapperTestSupport {

  import HashingTFTransformerSmokeTest.NumFeatures

  def transformerWithParameters: HashingTFTransformer = {
    val inPlace = NoInPlaceChoice().setOutputColumn("mapped")
    val single = SingleColumnChoice().setInputColumn(NameSingleColumnSelection("as")).setInPlaceChoice(inPlace)

    val transformer = new HashingTFTransformer()
    transformer.set(
      Seq(
        transformer.singleOrMultiChoiceParameter -> single,
        transformer.numFeaturesParameter -> NumFeatures
      ): _*
    )
  }

  def testValues: Seq[(Any, Any)] = {
    val arrays = Seq(
      Array("John", "likes", "to", "watch", "movies", "John"),
      Array("Mary", "likes", "movies", "too"),
      Array("guitar", "guitar", "guitar", "guitar")
    )

    val outputArray = {
      // unfortunately, we cannot write outputs explicitly, because the behaviour changes between Spark 1.6 and 2.0
      val inputCol = "test_input"
      val outputCol = "test_output"
      val sparkHashingTF = new SparkHashingTF().setNumFeatures(NumFeatures.toInt).setInputCol(inputCol).setOutputCol(outputCol)
      val inputDF = sparkSQLSession.createDataFrame(sparkContext.parallelize(arrays.map(Row(_))),
        StructType(Seq(StructField(inputCol, dataType = ArrayType(StringType))))
      )
      val outputDF = sparkHashingTF.transform(inputDF).select(outputCol)
      outputDF.rdd.map(r => r.getAs[Linalg.Vector](0)).collect
    }
    arrays.zip(outputArray)
  }

  def inputType: DataType = ArrayType(StringType)
  def outputType: DataType = new com.harana.spark.Linalg.VectorUDT
}

object HashingTFTransformerSmokeTest {
  val NumFeatures = 20.0
}
