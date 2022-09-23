package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameter, Parameters, ParametersSequence, StringParameter}
import com.harana.sdk.shared.models.flow.parameters.exceptions.NoArgumentConstructorRequiredError
import io.circe.Json
import io.circe.syntax.EncoderOps


case class ClassWithParameters() extends Parameters {
  val string = StringParameter("string")
  val bool = BooleanParameter("bool")

  val parameters = Left(Array(string, bool))

  def setBool(b: Boolean) = set(bool, b)
  def setString(s: String): this.type = set(string, s)
}

case class ParametersWithoutNoArgConstructor(x: String) extends Parameters {
  val parameters = Left(Array.empty[Parameter[_]])
}

class ParametersSequenceSpec extends AbstractParameterSpec[Seq[ClassWithParameters], ParametersSequence[ClassWithParameters]] {
  def className = "ParametersSequence"

  className should {
    "throw an exception when parameters don't have no-arg constructor" in {
      an[NoArgumentConstructorRequiredError] should be thrownBy
        ParametersSequence[ParametersWithoutNoArgConstructor](name = "parametersSequence")
    }
  }

  def paramFixture: (ParametersSequence[ClassWithParameters], Json) = {
    val parametersSequence = ParametersSequence[ClassWithParameters]("Parameters sequence name")
    val expectedJson = Map(
      "type"        -> Json.fromString("multiplier"),
      "name"        -> Json.fromString(parametersSequence.name),
      "default"     -> Json.Null,
      "isGriddable" -> Json.False,
      "values"      -> Seq(
                        Map(
                          "type"        -> Json.fromString("string"),
                          "name"        -> Json.fromString("string"),
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