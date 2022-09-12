package com.harana.utils.exception



import com.harana.utils.StandardSpec
import com.harana.utils.exception.json.FailureDescriptionJsonProtocol
import com.harana.utils.serialization.Serialization

class FailureDescriptionSerializationSpec extends StandardSpec with FailureDescriptionJsonProtocol with Serialization {

  val id = HaranaFile.Id.randomId

  val failureDescription = FailureDescription(
    id,
    FailureCode.UnexpectedError,
    "Very descriptive title",
    Some("Very descriptive message"),
    Map(
      "count"    -> "100",
      "nodes[0]" -> "u1",
      "nodes[1]" -> "u2",
      "nodes[2]" -> "u3",
      "desc.a"   -> "1",
      "desc.b"   -> "foo"
    )
  )

  val failureDescriptionJson = Map(
    "id"      -> Json.fromString(id.toString),
    "code"    -> Json.fromString(failureDescription.code.toString),
    "title"   -> Json.fromString(failureDescription.title),
    "message" -> Json.fromString(failureDescription.message.get),
    "details" -> Map(
      "count"    -> Json.fromString("100"),
      "nodes[0]" -> Json.fromString("u1"),
      "nodes[1]" -> Json.fromString("u2"),
      "nodes[2]" -> Json.fromString("u3"),
      "desc.a"   -> Json.fromString("1"),
      "desc.b"   -> Json.fromString("foo")
    )
  )

  "FailureDescription" should {
    "serialize to Json" in {
      val json = failureDescription.asJson
      json shouldBe failureDescriptionJson
    }

    "deserialize from Json" in {
      val deserialized = failureDescriptionJson.as[FailureDescription]
      deserialized shouldBe failureDescription
    }

    "serialize and deserialize using Java serialization" in {
      val serialized   = serialize(failureDescription)
      val deserialized = deserialize[FailureDescription](serialized)
      deserialized shouldBe failureDescription
    }
  }

}
