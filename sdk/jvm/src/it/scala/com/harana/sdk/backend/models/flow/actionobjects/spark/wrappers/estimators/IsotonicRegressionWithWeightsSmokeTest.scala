package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.OptionalWeightColumnChoice
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class IsotonicRegressionWithWeightsSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "IsotonicRegression"

  val estimator = new IsotonicRegression()
  import estimator._

  val weightColumnName = "myWeight"

  val weightColumnChoice = OptionalWeightColumnChoice
    .WeightColumnYesOption()
    .setWeightColumn(NameSingleColumnSelection(weightColumnName))

  val estimatorParameters = Seq(
    featureIndexParameter         -> 1,
    featuresColumnParameter       -> NameSingleColumnSelection("myFeatures"),
    isotonicParameter             -> true,
    labelColumnParameter          -> NameSingleColumnSelection("myLabel"),
    predictionColumnParameter     -> "isotonicPrediction",
    optionalWeightColumnParameter -> weightColumnChoice
  )

  className should {
    "pass weight column value to wrapped model" in {
      val estimatorWithParameters = estimator.set(estimatorParameters: _*)
      val sparkEstimator = estimatorWithParameters.sparkEstimator
      val sparkParamMap = estimatorWithParameters.sparkParamMap(sparkEstimator, dataFrame.sparkDataFrame.schema)
      sparkParamMap.get(estimator.sparkEstimator.weightCol) shouldBe Some(weightColumnName)
    }
  }
}
