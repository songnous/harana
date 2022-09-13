package com.harana.sdk.backend.models.flow.parameters.multivalue

import com.harana.sdk.backend.models.flow.UnitSpec
import com.harana.sdk.shared.models.flow.parameters.multivalue.MultipleValuesParameter
import io.circe.Json
import io.circe.syntax.EncoderOps

class MultipleValuesParameterJsonSpec extends UnitSpec {

  "MultipleValuesParameter" should {
    "deserialize from json" in {
      val gridParam = MultipleValuesParameter.fromJson[Int](json.asJson)
      gridParam.values shouldBe Seq(1, 2, 3, 4, 5)
    }
  }

  val json = Map(
                "values" -> Seq(
                  Map(
                    "type"    -> Json.fromString("seq"),
                    "value"   -> Map("sequence" -> Seq(Json.fromInt(1), Json.fromDouble(2.0).get, Json.fromInt(3), Json.fromInt(4)).asJson).asJson
                  ),
                  Map(
                    "type"    -> Json.fromString("seq"),
                    "value"   -> Map("sequence" -> Seq(Json.fromInt(4), Json.fromDouble(5.0).get).asJson).asJson
                  )
                )
              )
}
