package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.backend.models.flow.UnitSpec
import com.harana.sdk.shared.models.flow.actiontypes.inout.InputStorageTypeChoice.File
import com.harana.sdk.shared.models.designer.flow.parameters._
import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.parameters.{NumericParameter, Parameter, ParameterGroup, ParameterMap, ParameterPair, ParameterType, Parameters}
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.exceptions.ParamValueNotProvidedError
import io.circe.Json
import io.circe.syntax.EncoderOps

class ParametersSpec extends UnitSpec {

  import ParametersSpec._

  "Class with Parameters" should {

    "return array of its parameters ordered asc by index" in {
      val p = WithParameters()
      (p.parameters should contain).theSameElementsInOrderAs(Seq(p.param2, p.param1))
    }
    "validate its parameters" in {
      val p = WithParameters()
      p.set1(4)
      p.validateParameters should contain theSameElementsAs
        new ParamValueNotProvidedError(nameOfParam2) +: p.param1.validate(4)
    }
    "describe its parameters as json ordered as in parameters Array()" in {
      val p = WithParameters()
      p.parametersToJson shouldBe Seq(
        p.param2.asJson,
        p.param1.asJson
      )
    }
    "describe values of its parameters as json" in {
      val p = WithParameters()
      p.set1(4)
      p.parameterValuesToJson shouldBe Json(
        p.param1.name -> 4.asJson
      )
    }
    "set values of its parameters from json" when {
      "some parameters get overwritten" in {
        val p = WithParameters()
        p.set1(4)
        p.setParametersFromJson(
          Json(
            p.param1.name -> 5.asJson,
            p.param2.name -> 6.asJson
          )
        )
        p.get1 shouldBe 5
        p.get2 shouldBe 6
      }
      "json is null" in {
        val p = WithParameters()
        p.set1(4)
        p.setParametersFromJson(Json.Null)
        p.get1 shouldBe 4
      }
      "value of some param is Json.Null" in {
        val p = WithParameters()
        p.set1(4)
        p.setParametersFromJson(
          Json(
            p.param1.name -> Json.Null,
            p.param2.name -> 6.asJson
          )
        )
        p.get1 shouldBe defaultForParam1
        p.get2 shouldBe 6
      }
      "value of some param that doesn't have default is Json.Null" in {
        val p = WithParameters()
        p.set2(17)
        p.setParametersFromJson(
          Json(
            p.param2.name -> Json.Null
          )
        )
        p.is2Defined shouldBe false
      }
      "ignoreNulls is set" in {
        val p = WithParameters()
        p.set1(4)
        p.setParametersFromJson(
          Json(
            p.param1.name -> Json.Null,
            p.param2.name -> 6.asJson
          )
          ignoreNulls = true
        )
        p.get1 shouldBe 4
        p.get2 shouldBe 6
      }
    }
    "throw Deserialization exception" when {
      "unknown param name is used it should be ignored" in {
        val p = WithParameters()
        p.setParametersFromJson(Json("unknownName" -> 5.asJson, p.param2.name -> 6.asJson))
        p.get1 shouldBe defaultForParam1
      }
    }
  }
  "Parameters.isSet" should {
    "return true" when {
      "param value is set" in {
        val p = WithParameters()
        p.set1(3)
        p.is1Set shouldBe true
      }
    }
    "return false" when {
      "param value is not set" in {
        val p = WithParameters()
        p.is1Set shouldBe false
      }
    }
  }
  "Parameters.isDefined" should {
    "return true" when {
      "param value is set" in {
        val p = WithParameters()
        p.set1(3)
        p.is1Defined shouldBe true
      }
      "param value is not set, but default value is" in {
        val p = WithParameters()
        p.is1Defined shouldBe true
      }
    }
    "return false" when {
      "neither value nor default value is set" in {
        val p = WithParameters()
        p.is2Defined shouldBe false
      }
    }
  }
  "Parameters.clear" should {
    "clear param value" in {
      val p = WithParameters()
      p.set1(3)
      p.clear1
      p.is1Set shouldBe false
    }
  }

  "Parameters.$" should {
    "return param value" when {
      "it is defined" in {
        val p = WithParameters()
        p.set1(3)
        p.get1 shouldBe 3
      }
    }

    "return default value" when {
      "value is not defined, but default is" in {
        val p = WithParameters()
        p.get1 shouldBe defaultForParam1
      }
    }

    "throw exception" when {
      "neither param value nor default is defined" in {
        val p         = WithParameters()
        val exception = intercept[ParamValueNotProvidedError] {
          p.get2
        }
        exception.name shouldBe p.param2.name
      }
    }
  }
  "Parameters.extractParamMap" should {
    "return enriched paramMap" in {
      val p          = WithParameters()
      p.set2(8)
      val extraParam = MockParameter("c")
      val extraPair  = ParameterPair(extraParam, 9)
      val extraMap   = ParameterMap(extraPair)
      p.extractParameterMap(extraMap) shouldBe ParameterMap(
        ParameterPair(p.param1, defaultForParam1),
        ParameterPair(p.param2, 8),
        extraPair
      )
    }
  }
  "Parameters.sameAs" should {
    val valueOne = 100
    val valueTwo = 5

    "return true" when {

      "classes of both objects are the same and object have the same parameters with the same values" in {
          val withParametersA1 = new WithParametersA
          val withParametersA2 = new WithParametersA
          withParametersA1.sameAs(withParametersA2) shouldBe true

          withParametersA1.set1(valueOne)
          withParametersA2.set1(valueOne)
          withParametersA1.sameAs(withParametersA2) shouldBe true

          withParametersA1.set2(valueTwo)
          withParametersA2.set2(valueTwo)
          withParametersA1.sameAs(withParametersA2) shouldBe true
        }
      "comparing replicated Parameters" in {
        val withParametersA1 = new WithParametersA
        val withParametersA2 = withParametersA1.replicate()
        withParametersA1.eq(withParametersA2) shouldBe false
        withParametersA1.sameAs(withParametersA2) shouldBe true
      }
    }

    "return false" when {

      "classes of both objects are different" in {
        val withParametersA = new WithParametersA
        val withParametersB = new WithParametersB
        withParametersA.sameAs(withParametersB) shouldBe false
      }

      "parameters have different values" in {
        val file1 = new File()
        val file2 = new File()
        file1.setSourceFile("path")

        file1.sameAs(file2) shouldBe false

        file1.setSourceFile("path1")
        file2.setSourceFile("path2")

        file1.sameAs(file2) shouldBe false
      }
    }
  }
}

object ParametersSpec extends UnitSpec {

  case class MockError(override val message: String) extends FlowError(message)

  case class MockParameter(name: String,
                           required: Boolean = false,
                           default: Option[Int] = None) extends Parameter[Int] {
    val parameterType = mock[ParameterType]

    def valueToJson(value: Int) = value.asJson
    def valueFromJson(jsValue: Json) = jsValue.as[Int]
    override def validate(value: Int) = List(MockError(name))
    override def replicate(name: String) = copy(name = name)
  }

  val defaultForParam1 = 1
  val nameOfParam2 = "name of param2"

  case class WithParameters() extends Parameters {
    val param1 = MockParameter("name-of-param1", default = Some(defaultForParam1))
    val param2 = MockParameter(nameOfParam2)
    override val parameterGroups = List(ParameterGroup("", param2, param1))

    def set1(v: Int) = set(param1 -> v)
    def set2(v: Int) = set(param2 -> v)
    def get1 = $(param1)
    def get2 = $(param2)
    def is1Set: Boolean = isSet(param1)
    def is1Defined: Boolean = isDefined(param1)
    def is2Defined: Boolean = isDefined(param2)
    def clear1 = clear(param1)
  }

  class WithParametersA extends WithParameters
  class WithParametersB extends WithParameters

  case class ParametersWithChoice() extends Parameters {
    val choiceParameter = ChoiceParameter[ChoiceWithRepeatedParameter]("choice")
    def setChoice(v: ChoiceWithRepeatedParameter) = set(choiceParameter, v)
    override val parameterGroups = List(ParameterGroup("", choiceParameter))
  }

  sealed trait ChoiceWithRepeatedParameter extends Choice {
    val choiceOrder: List[ChoiceOption] = List(classOf[ChoiceOne], classOf[ChoiceTwo])
  }

  case class ChoiceOne() extends ChoiceWithRepeatedParameter {
    val name = "one"
    val numericParameter = NumericParameter("x"))
    override val parameterGroups = List(ParameterGroup("", numericParameter))
  }

  case class ChoiceTwo() extends ChoiceWithRepeatedParameter {
    val name = "two"
    val numericParameter = NumericParameter("x")
    override val parameterGroups = List(ParameterGroup("", numericParameter))
  }

  object DeclareParametersFixtures {
    val outsideParameter = MockParameter("outside name")

    class ParametersFromOutside extends Parameters {
      val nameParameter = MockParameter("name")
      override val parameterGroups = List(ParameterGroup("", outsideParameter, nameParameter))
    }

    class ParametersWithNotUniqueNames extends Parameters {
      val param1 = MockParameter("some name")
      val param2 = MockParameter(param1.name)
      override val parameterGroups = List(ParameterGroup("", param1, param2))
    }

    class NotparametersDeclared extends Parameters {
      val param1 = MockParameter("some name")
      val param2 = MockParameter("some other name")
      override val parameterGroups = List(ParameterGroup("", param1))
    }

    class ParametersRepeated extends Parameters {
      val param1 = MockParameter("some name")
      val param2 = MockParameter("some other name")
      override val parameterGroups = List(ParameterGroup("", param1, param2, param1))
    }
  }
}
