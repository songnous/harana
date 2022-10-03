package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.FeatureSubsetStrategy.Auto
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, Parameters}

import scala.language.reflectiveCalls

trait HasFeatureSubsetStrategyParameter extends Parameters {

  val featureSubsetStrategyParameter = ChoiceParameter[FeatureSubsetStrategy.Option]("feature-subset-strategy", default = Some(Auto()))

}

object FeatureSubsetStrategy {

  sealed abstract class Option(val name: String) extends Choice {
    override val parameterGroups = List.empty[ParameterGroup]

    val choiceOrder: List[ChoiceOption] = List(
      classOf[Auto],
      classOf[OneThird],
      classOf[Sqrt],
      classOf[Log2]
    )
  }

  case class Auto() extends Option("auto")
  case class OneThird() extends Option("onethird")
  case class Sqrt() extends Option("sqrt")
  case class Log2() extends Option("log2")
}