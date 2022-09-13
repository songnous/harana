package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameter, Parameters, ParametersSequence, StringParameter}
import com.harana.sdk.shared.models.flow.parameters.exceptions.NoArgumentConstructorRequiredError
import io.circe.Json
import io.circe.syntax.EncoderOps


case class ClassWithParameters() extends Parameters {
  val string = StringParameter("string", None)
  val bool = BooleanParameter("bool", None)

  val parameters = Array(string, bool)

  def setBool(b: Boolean) = set(bool, b)
  def setString(s: String): this.type = set(string, s)
}

case class ParametersWithoutNoArgConstructor(x: String) extends Parameters {
  val parameters = Array.empty[Parameter[_]]
}

class ParametersSequenceSpec extends AbstractParameterSpec[Seq[ClassWithParameters], ParametersSequence[ClassWithParameters]] {
  def className = "ParametersSequence"

  className should {
    "throw an exception when parameters don't have no-arg constructor" in {
      an[NoArgumentConstructorRequiredError] should be thrownBy
        ParametersSequence[ParametersWithoutNoArgConstructor](name = "parametersSequence", description = None)
    }
  }

  def paramFixture: (ParametersSequence[ClassWithParameters], Json) = {
    val description = "Parameters sequence description"
    val parametersSequence = ParametersSequence[ClassWithParameters]("Parameters sequence name", Some(description))
    val expectedJson = Map(
      "type"        -> Json.fromString("multiplier"),
      "name"        -> Json.fromString(parametersSequence.name),
      "description" -> Json.fromString(description),
      "default"     -> Json.Null,
      "isGriddable" -> Json.False,
      "values"      -> Seq(
                        Map(
                          "type"        -> Json.fromString("string"),
                          "name"        -> Json.fromString("string"),
                          "description" -> Json.fromString(""),
                          "default"     -> Json.Null,
                          "isGriddable" -> Json.False,
                          "validator"   -> Map(
                                            "type" -> Json.fromString("regex"),
                                            "configuration" -> Map("regex" -> ".*").asJson
                                           ).asJson
                        ),
                        Map(
                          "type"        -> Json.fromString("boolean"),
                          "name"        -> Json.fromString("bool"),
                          "description" -> Json.fromString(""),
                          "isGriddable" -> Json.False,
                          "default"     -> Json.Null
                        )
                      ).asJson
    )
    (parametersSequence, expectedJson.asJson)
  }

  def valueFixture = {
    val customParameters = Seq(ClassWithParameters().setBool(true).setString("aaa"), ClassWithParameters().setBool(false).setString("bbb"))
    val expectedJson = Seq(
      Map(
        "string" -> Json.fromString("aaa"),
        "bool"   -> Json.True
      ),
      Map(
        "string" -> Json.fromString("bbb"),
        "bool"   -> Json.False
      )
    )
    (customParameters, expectedJson.asJson)
  }
}