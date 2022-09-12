package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.Metadata
import org.apache.spark.sql.types.StructType
import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.parameters.common._
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.utils.DataFrameUtils
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class GBTClassifierSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "GBTClassifier"

  val estimator = new VanillaGBTClassifier()
  import estimator._

  private val labelColumnName = "myRating"

  val estimatorParameters = Seq(
    featuresColumnParameter      -> NameSingleColumnSelection("myFeatures"),
    impurityParameter            -> ClassificationImpurity.Entropy(),
    labelColumnParameter         -> NameSingleColumnSelection(labelColumnName),
    lossTypeParameter            -> GBTClassifier.Logistic(),
    maxBinsParameter             -> 2.0,
    maxDepthParameter            -> 6.0,
    maxIterationsParameter       -> 10.0,
    minInfoGainParameter         -> 0.0,
    minInstancesPerNodeParameter -> 1,
    predictionColumnParameter    -> "prediction",
    seedParameter                -> 100.0,
    stepSizeParameter            -> 0.11,
    subsamplingRateParameter     -> 0.999
  )

  override def assertTransformedDF(dataFrame: DataFrame) = {
    val possibleValues = DataFrameUtils.collectValues(dataFrame, labelColumnName)
    val actualValues   = DataFrameUtils.collectValues(dataFrame, "prediction")

    actualValues.diff(possibleValues) shouldBe empty
  }

  override def assertTransformedSchema(schema: StructType) = {
    val predictionColumn = schema.fields.last
    predictionColumn.name shouldBe "prediction"
    predictionColumn.dataType shouldBe DoubleType
    predictionColumn.metadata shouldBe Metadata.empty
  }
}