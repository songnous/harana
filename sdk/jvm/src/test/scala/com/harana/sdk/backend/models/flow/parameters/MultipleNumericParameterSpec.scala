package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.shared.models.designer.flow
import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.MultipleNumericParameter
import com.harana.sdk.shared.models.flow.parameters.validators.{ArrayLengthValidator, ComplexArrayValidator, RangeValidator}
import io.circe.Json
import io.circe.syntax.EncoderOps

class MultipleNumericParameterSpec extends AbstractParameterSpec[Array[Double], MultipleNumericParameter] {

  def className = "NumericParameter"

  className should {
    "validate its values" when {
      val (param, _) = paramFixture
      "empty value set is too short" in {
        param.validate(Array()) should have size 1
      }
      "values are correct" in {
        param.validate(Array(1.0, 2.0, 2.5)) shouldBe empty
      }
      "two values are incorrect" in {
        param.validate(Array(1.0, 100.0, 200.0)) should have size 2
      }
      "array is too long" in {
        param.validate(Array(1.0, 1.0, 1.0, 1.0, 1.0, 1.0)) should have size 1
      }
      "array is too long and all six values are incorrect" in {
        param.validate(Array(4.0, 5.0, 6.0, 7.5, 100.0, -2.0)) should have size 7
      }
    }
  }

  def paramFixture: (MultipleNumericParameter, Json) = {
      val param       = MultipleNumericParameter(
      name = "Multiple numeric parameter",
      validator = ComplexArrayValidator(
        rangeValidator = RangeValidator(1.0, 3.0, beginIncluded = true, endIncluded = false),
        lengthValidator = ArrayLengthValidator(min = 2, max = 4)
      )
    )
    val json = Map(
      "type"        -> Json.fromString("multipleNumeric"),
      "name"        -> Json.fromString(param.name),
      "description" -> Json.fromString(param.constraints),
      "default"     -> Json.Null,
      "isGriddable" -> Json.False,
      "validator"   -> Map(
                        "type"          -> Json.fromString("range"),
                        "configuration" -> Map(
                                              "begin"         -> Json.fromDouble(1.0).get,
                                              "end"           -> Json.fromDouble(3.0).get,
                                              "beginIncluded" -> Json.True,
                                              "endIncluded"   -> Json.False
                                            ).asJson
                      ).asJson
    )
    (param, json.asJson)
  }

  def valueFixture: (Array[Double], Json) = {
    val value     = Array(1.0, 2.0, 3.0)
    val jsonValue = Map(
                      "values" -> Seq(
                        Map(
                          "type"  -> Json.fromString("seq"),
                          "value" -> Map("sequence" -> Seq(1.0, 2.0, 3.0).asJson).asJson
                        )
                      )
                    )
    (value, jsonValue.asJson)
  }
}
