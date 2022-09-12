package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.designer.flow.actionobjects.spark.wrappers.estimators.LDA.OnlineLDAOptimizer
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class LDASmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "LDA"

  val estimator = new LDA()
  import estimator._

  val estimatorParameters = Seq(
    checkpointIntervalParameter      -> 4.0,
    featuresColumnParameter          -> NameSingleColumnSelection("myFeatures"),
    kParameter                       -> 3.0,
    maxIterationsParameter           -> 30.0,
    optimizerParameter               -> OnlineLDAOptimizer().setDocConcentration(Array(0.5, 0.3, 0.2)).setTopicConcentration(0.8),
    seedParameter                    -> 123.0,
    subsamplingRateParameter         -> 0.1,
    topicDistributionColumnParameter -> "cluster"
  )

  override def isAlgorithmDeterministic: Boolean = false
}
