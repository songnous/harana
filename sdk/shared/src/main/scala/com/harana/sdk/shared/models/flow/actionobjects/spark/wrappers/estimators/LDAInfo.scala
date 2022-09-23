package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.LDAInfo.OnlineLDAOptimizer
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common._
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.validators.{ArrayLengthValidator, ComplexArrayValidator, RangeValidator}
import com.harana.sdk.shared.models.flow.parameters.{DoubleArrayParameter, DoubleParameter, SingleColumnCreatorParameter}

trait LDAInfo
    extends ActionObjectInfo
    with EstimatorInfo
    with HasCheckpointIntervalParameter
    with HasFeaturesColumnParameter
    with HasNumberOfClustersParameter
    with HasMaxIterationsParameter
    with HasSeedParameter {

  val id = "380DF834-7337-4F24-B538-47162FFA317E"

  override val maxIterationsDefault = 20

  val optimizerParameter = ChoiceParameter[OnlineLDAOptimizer]("optimizer")
  setDefault(optimizerParameter, OnlineLDAOptimizer())

  val subsamplingRateParameter = DoubleParameter("subsampling rate", validator = RangeValidator(0.0, 1.0, beginIncluded = false))
  setDefault(subsamplingRateParameter, 0.05)

  val topicDistributionColumnParameter = SingleColumnCreatorParameter("topic distribution column")
  setDefault(topicDistributionColumnParameter, "topicDistribution")

  val parameters = Array(
    checkpointIntervalParameter,
    kParameter,
    maxIterationsParameter,
    optimizerParameter,
    subsamplingRateParameter,
    topicDistributionColumnParameter,
    featuresColumnParameter,
    seedParameter
  )
}

object LDAInfo extends LDAInfo {

  class DocConcentrationParameter(name: String, validator: ComplexArrayValidator[Double]) extends DoubleArrayParameter(
      name = name,
      validator = validator
    )

  class TopicConcentrationParameter(name: String, validator: RangeValidator[Double]) extends DoubleParameter(
      name = name,
      validator = validator)

  sealed trait LDAOptimizer extends Choice {
    val docConcentrationParameter = createDocumentConcentrationParam()
    def setDocConcentration(v: Array[Double]): this.type = set(docConcentrationParameter, v)

    val topicConcentrationParameter = createTopicConcentrationParam()
    def setTopicConcentration(v: Double): this.type = set(topicConcentrationParameter, v)

    def createDocumentConcentrationParam(): DocConcentrationParameter
    def createTopicConcentrationParam(): TopicConcentrationParameter

    val choiceOrder: List[ChoiceOption] = List(classOf[OnlineLDAOptimizer], classOf[ExpectationMaximizationLDAOptimizer])
    val parameters = Array(docConcentrationParameter, topicConcentrationParameter)
  }

  case class OnlineLDAOptimizer() extends LDAOptimizer {
    val name = "online"

    def createDocumentConcentrationParam() = new DocConcentrationParameter("doc concentration", validator = ComplexArrayValidator(
          rangeValidator = RangeValidator(0.0, Double.MaxValue),
          lengthValidator = ArrayLengthValidator.withAtLeast(1)
        )
      )

    setDefault(docConcentrationParameter, Array(0.5, 0.5))

    def createTopicConcentrationParam() = new TopicConcentrationParameter("topic concentration", validator = RangeValidator(0.0, Double.MaxValue))
    setDefault(topicConcentrationParameter, 0.5)
  }

  case class ExpectationMaximizationLDAOptimizer() extends LDAOptimizer {
    val name = "em"

    def createDocumentConcentrationParam() = new DocConcentrationParameter("doc concentration",
        validator = ComplexArrayValidator(
          rangeValidator = RangeValidator(1.0, Double.MaxValue, beginIncluded = false),
          lengthValidator = ArrayLengthValidator.withAtLeast(1)
        )
      )
    setDefault(docConcentrationParameter, Array(26.0, 26.0))

    def createTopicConcentrationParam() = new TopicConcentrationParameter("topic concentration", RangeValidator(1.0, Double.MaxValue, beginIncluded = false))
    setDefault(topicConcentrationParameter, 1.1)
  }
}