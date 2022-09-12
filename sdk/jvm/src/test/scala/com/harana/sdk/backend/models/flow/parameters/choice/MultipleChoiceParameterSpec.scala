package com.harana.sdk.backend.models.flow.parameters.choice

import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter, MultipleChoiceParameter}
import io.circe.Json
import io.circe.syntax.EncoderOps

import scala.reflect.runtime.universe._

class MultipleChoiceParameterSpec extends AbstractChoiceParamSpec[Set[ChoiceABC], MultipleChoiceParameter[ChoiceABC]] {

  def className = "MultipleChoiceParameter"

  className should {
    "serialize default values properly" in {
      val allChoicesSelection: Set[ChoiceABC] = Set(OptionA(), OptionB(), OptionC())
      val expected = Seq(Json.fromString("A"), Json.fromString("B"), Json.fromString("C"))
      val serializedArray = serializeDefaultValue(allChoicesSelection)
      serializedArray.elements should contain theSameElementsAs expected.elements
    }
  }

  def paramFixture: (MultipleChoiceParameter[ChoiceABC], Json) = {
    val description = "description"
    val multipleChoiceParam = MultipleChoiceParameter[ChoiceABC]("name", Some(description))
    val multipleChoiceExpectedJson = Map(
                                        "type"        -> Json.fromString("multipleChoice"),
                                        "name"        -> Json.fromString(multipleChoiceParam.name),
                                        "description" -> Json.fromString(description),
                                        "isGriddable" -> Json.False,
                                        "default"     -> Json.Null,
                                      ) ++ ChoiceFixtures.values
    (multipleChoiceParam, multipleChoiceExpectedJson.asJson)
  }

  def valueFixture: (Set[ChoiceABC], Json) = {
    val choices = Set[ChoiceABC](OptionA().setBool(true), OptionC())
    val expectedJson = Map(
                          "A" -> Map("bool" -> Json.True).asJson,
                          "C" -> Json.Null
                        )
    (choices, expectedJson.asJson)
  }

  override def serializeDefaultValue(default: Set[ChoiceABC]) =
    Seq(default.toSeq.map(_.name).map(_)): _*

  def createChoiceParam[V <: Choice: TypeTag](name: String, description: String) =
    ChoiceParameter[V](name, Some(description))
}
