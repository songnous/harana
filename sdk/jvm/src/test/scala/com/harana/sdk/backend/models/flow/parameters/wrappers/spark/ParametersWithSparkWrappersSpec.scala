package com.harana.sdk.backend.models.flow.parameters.wrappers.spark

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameter, StringParameter}
import org.apache.spark.ml
import org.apache.spark.ml.param._
import org.apache.spark.sql.types.StructType
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar

class ParametersWithSparkWrappersSpec extends AnyWordSpec with Matchers with MockitoSugar {

  import ParametersWithSparkWrappersSpec._

  "ParametersWithSparkWrappers" should {

    "calculate sparkParamWrappers" in {
      val parametersWithSparkWrappers = ParametersWithSparkWrappersClass()
      parametersWithSparkWrappers.sparkParamWrappers shouldBe Array(parametersWithSparkWrappers.paramA, parametersWithSparkWrappers.paramB)
    }

    "return parameter values" in {
      val parametersWithSparkWrappers = ParametersWithSparkWrappersClass().setParamA("a").setParamB(0.0)
      parametersWithSparkWrappers
        .sparkParamMap(parametersWithSparkWrappers.exampleSparkParameters, StructType(Seq()))
        .toSeq
        .toSet shouldBe
        Set(
          parametersWithSparkWrappers.exampleSparkParameters.sparkParamA -> "a",
          parametersWithSparkWrappers.exampleSparkParameters.sparkParamB -> 0
        )
    }

    "return wrappers nested in choice parameter values" in {
      val parametersWithSparkWrappers = ParametersWithSparkWrappersClass().setChoice(OneParamChoiceWithWrappers().setParamC("c"))
      parametersWithSparkWrappers
        .sparkParamMap(parametersWithSparkWrappers.exampleSparkParameters, StructType(Seq()))
        .toSeq
        .toSet shouldBe
        Set(parametersWithSparkWrappers.exampleSparkParameters.sparkParamC -> "c")
    }
  }
}

object ParametersWithSparkWrappersSpec {

  class ExampleSparkParameters extends ml.param.Params {
    val uid = "id"
    val sparkParamA = new Param[String]("", "paramA", "descA")
    val sparkParamB = new IntParam("", "paramB", "descB")
    val sparkParamC = new Param[String]("", "paramC", "descC")
    def copy(extra: ParamMap): Params = ???

  }

  case class ParametersWithSparkWrappersClass() extends ParametersWithSparkWrappers {

    val exampleSparkParameters = new ExampleSparkParameters

    val paramA = StringParameter("paramA", Some("descA"))
    val paramB = new IntParameter[ExampleSparkParameters]("paramB", Some("descB"), _.sparkParamB)

    val choiceWithParametersInValues = new ChoiceParameter[ChoiceWithWrappers]("choice", Some("descChoice"))
    val notWrappedParameter = BooleanParameter("booleanparameterName", Some("booleanParamDescription"))

    val parameters = Array(paramA, paramB, choiceWithParametersInValues, notWrappedParameter)

    def setParamA(v: String): this.type = set(paramA, v)
    def setParamB(v: Double) = set(paramB, v)
    def setChoice(v: ChoiceWithWrappers) = set(choiceWithParametersInValues, v)
  }

  sealed trait ChoiceWithWrappers extends Choice {
    val choiceOrder: List[ChoiceOption] = List(classOf[OneParamChoiceWithWrappers], classOf[EmptyChoiceWithWrappers])
  }

  case class OneParamChoiceWithWrappers() extends ChoiceWithWrappers {
    val name = "one param"
    val paramC = StringParameter("paramC", Some("descC"))
    def setParamC(v: String): this.type = set(paramC, v)
    val parameters = Array(paramC)
  }

  case class EmptyChoiceWithWrappers() extends ChoiceWithWrappers {
    val name = "no parameters"
    val parameters = Array.empty[Parameter[_]]
  }
}
