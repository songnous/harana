package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import FeatureSubsetStrategy.Auto
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, Parameters}

import scala.language.reflectiveCalls

trait HasFeatureSubsetStrategyParameter extends Parameters {

  val featureSubsetStrategyParameter = ChoiceParameter[FeatureSubsetStrategy.Option]("feature subset strategy")
  setDefault(featureSubsetStrategyParameter, Auto())

}

object FeatureSubsetStrategy {

  sealed abstract class Option(val name: String) extends Choice {
    val parameters = Array.empty[Parameter[_]]

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