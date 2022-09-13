package com.harana.sdk.backend.models.flow.parameters.choice

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameter}
import com.harana.sdk.shared.models.flow.parameters.choice.Choice
import io.circe.Json
import io.circe.syntax.EncoderOps

sealed trait ChoiceABC extends Choice {
  val choiceOrder: List[ChoiceOption] = List(classOf[OptionB], classOf[OptionC], classOf[OptionA])
}

case class OptionA() extends ChoiceABC {
  val name = "A"
  val bool = BooleanParameter("bool", Some("description"))
  val parameters = Array(bool)
  def setBool(b: Boolean) = set(bool, b)
}

case class OptionB() extends ChoiceABC {
  val name = "B"
  val parameters = Array.empty[Parameter[_]]
}

case class OptionC() extends ChoiceABC {
  val name = "C"
  val parameters = Array.empty[Parameter[_]]
}

sealed trait BaseChoice extends Choice {
  val choiceOrder: List[ChoiceOption] = List(classOf[ChoiceWithoutNoArgConstructor])
}

case class ChoiceWithoutNoArgConstructor(x: String) extends BaseChoice {
  val name = "choiceWithoutNoArgConstructor"
  val parameters = Array.empty[Parameter[_]]
}

sealed trait ChoiceWithoutDeclaration extends Choice {
  val choiceOrder: List[Class[_ <: ChoiceWithoutDeclaration]] = List()
}

case class ChoiceWithoutDeclarationInstance() extends ChoiceWithoutDeclaration {
  val name = "choiceWithoutDeclarationInstance"
  val parameters = Array.empty[Parameter[_]]
}

object ChoiceFixtures {

  val values = Map("values" ->
                Seq(
                  Map(
                    "name"   -> Json.fromString("B"),
                    "schema" -> Json.Null
                  ),
                  Map(
                    "name"   -> Json.fromString("C"),
                    "schema" -> Json.Null
                  ),
                  Map(
                    "name"   -> Json.fromString("A"),
                    "schema" -> Seq(
                                  Map(
                                    "type"        -> Json.fromString("boolean"),
                                    "name"        -> Json.fromString("bool"),
                                    "description" -> Json.fromString("description"),
                                    "isGriddable" -> Json.False,
                                    "default"     -> Json.Null
                                  )
                                ).asJson
                  )
                ).asJson
    )
  }