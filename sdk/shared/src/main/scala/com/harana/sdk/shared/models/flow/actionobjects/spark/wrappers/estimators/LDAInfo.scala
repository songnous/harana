package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.{ActionObjectInfo, EstimatorInfo}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.LDAInfo.OnlineLDAOptimizer
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common._
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.validators.{ArrayLengthValidator, ComplexArrayValidator, RangeValidator}
import com.harana.sdk.shared.models.flow.parameters.{DoubleArrayParameter, DoubleParameter, ParameterGroup, SingleColumnCreatorParameter}

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

  val optimizerParameter = ChoiceParameter[OnlineLDAOptimizer]("optimizer", default = Some(OnlineLDAOptimizer()))
  val subsamplingRateParameter = DoubleParameter("subsampling-rate", default = Some(0.05), validator = RangeValidator(0.0, 1.0, beginIncluded = false))
  val topicDistributionColumnParameter = SingleColumnCreatorParameter("topic-distribution-column", default = Some("topicDistribution"))

  override val parameterGroups = List(ParameterGroup("",
    checkpointIntervalParameter,
    kParameter,
    maxIterationsParameter,
    optimizerParameter,
    subsamplingRateParameter,
    topicDistributionColumnParameter,
    featuresColumnParameter,
    seedParameter
  ))
}

object LDAInfo extends LDAInfo {

  class DocConcentrationParameter(name: String,
                                  required: Boolean = false,
                                  default: Option[Array[Double]] = None,
                                  validator: ComplexArrayValidator[Double]) extends DoubleArrayParameter(name, required, default, validator)

  class TopicConcentrationParameter(name: String,
                                    required: Boolean = false,
                                    default: Option[Double] = None,
                                    validator: RangeValidator[Double]) extends DoubleParameter(name, required, default, validator)

  sealed trait LDAOptimizer extends Choice {
    val docConcentrationParameter = createDocumentConcentrationParam()
    def setDocConcentration(v: Array[Double]): this.type = set(docConcentrationParameter, v)

    val topicConcentrationParameter = createTopicConcentrationParam()
    def setTopicConcentration(v: Double): this.type = set(topicConcentrationParameter, v)

    def createDocumentConcentrationParam(): DocConcentrationParameter
    def createTopicConcentrationParam(): TopicConcentrationParameter

    val choiceOrder: List[ChoiceOption] = List(classOf[OnlineLDAOptimizer], classOf[ExpectationMaximizationLDAOptimizer])
    override val parameterGroups = List(ParameterGroup("", docConcentrationParameter, topicConcentrationParameter))
  }

  case class OnlineLDAOptimizer() extends LDAOptimizer {
    val name = "online"

    def createDocumentConcentrationParam() = new DocConcentrationParameter("doc-concentration", default = Some(Array(0.5, 0.5)), validator = ComplexArrayValidator(
          rangeValidator = RangeValidator(0.0, Double.MaxValue),
          lengthValidator = ArrayLengthValidator.withAtLeast(1)
        )
      )

    def createTopicConcentrationParam() = new TopicConcentrationParameter("topic-concentration", default = Some(0.5), validator = RangeValidator(0.0, Double.MaxValue))
  }

  case class ExpectationMaximizationLDAOptimizer() extends LDAOptimizer {
    val name = "em"

    def createDocumentConcentrationParam() = new DocConcentrationParameter("doc-concentration",
      default = Some(Array(26.0, 26.0)),
      validator = ComplexArrayValidator(
          rangeValidator = RangeValidator(1.0, Double.MaxValue, beginIncluded = false),
          lengthValidator = ArrayLengthValidator.withAtLeast(1)
        )
      )

    def createTopicConcentrationParam() = new TopicConcentrationParameter("topic-concentration", default = Some(1.1), validator = RangeValidator(1.0, Double.MaxValue, beginIncluded = false))
  }
}