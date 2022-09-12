package com.harana.sdk.backend.models.flow.parameters.choice

import scala.reflect.runtime.universe._
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import io.circe.Json
import io.circe.syntax.EncoderOps

class ChoiceParameterSpec extends AbstractChoiceParamSpec[ChoiceABC, ChoiceParameter[ChoiceABC]] {

  def className = "ChoiceParameter"

  className should {

    "throw an exception while deserializing multiple choices" in {
      val param          = paramFixture._1
      val twoChoicesJson = Map(
                              "B" -> Json.Null,
                              "C" -> Json.Null
                            )
      an[DeserializationException] should be thrownBy param.valueFromJson(twoChoicesJson.asJson)
    }
    "serialize default values properly" in {
      val choices  = Seq(OptionA(), OptionB(), OptionC())
      val expected = Seq("A", "B", "C").map(_)
      choices.map(serializeDefaultValue) should contain theSameElementsAs expected
    }

    "validate choice subparameters" in {
      val param = paramFixture._1
      val value = OptionA()
      value.validateParameters should not be empty
      param.validate(value) shouldBe value.validateParameters
    }
  }

  def paramFixture = {
    val singleChoiceParam = ChoiceParameter[ChoiceABC]("name", Some("description"))
    val singleChoiceExpectedJson = Map(
                                      "type"        -> Json.fromString("choice"),
                                      "name"        -> Json.fromString(singleChoiceParam.name),
                                      "description" -> Json.fromString("description"),
                                      "isGriddable" -> Json.False,
                                      "default"     -> Json.Null,
                                    ) ++ ChoiceFixtures.values

    (singleChoiceParam, singleChoiceExpectedJson.asJson)
  }

  def valueFixture = {
    val choice       = OptionA().setBool(true)
    val expectedJson = Map("A" -> Map("bool" -> Json.True).asJson)
    (choice, expectedJson.asJson)
  }

  override def serializeDefaultValue(default: ChoiceABC) =
    default.name

  def createChoiceParam[V <: Choice: TypeTag](name: String, description: String) =
    ChoiceParameter[V](name, Some(description))

}
