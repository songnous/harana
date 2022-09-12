package com.harana.utils.json.envelope


import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EnvelopeSpec extends AnyFlatSpec with Matchers with DefaultJsonProtocol {

  val StringLabel = "ExampleOfStringLabel"

  val WrongStringLabel = "WrongExampleOfStringLabel"

  val EnvelopeStringJsonFormat = new EnvelopeJsonFormat[String](StringLabel)

  "Envelope[String]" should "be encoded to and decoded from JSON" in {
    val exampleString   = "Johny Bravo"
    val envelope        = Envelope(exampleString)
    val encodedEnvelope = EnvelopeStringJsonFormat.write(envelope)
    encodedEnvelope shouldBe Json(StringLabel -> Json.fromString(exampleString))
    val decodedEnvelope = EnvelopeStringJsonFormat.read(encodedEnvelope.asJson)
    decodedEnvelope.content shouldBe exampleString
  }

  "Wrongly structured Envelope[String] JSON" should "throw exception during decoding" in {
    val emptyJsonSet = Json.Null
    an[DeserializationException] should
      be thrownBy EnvelopeStringJsonFormat.read(emptyJsonSet.asJson)
  }

  "Wrongly labeled Envelope[String] JSON" should "throw exception during decoding" in {
    val wrongLabeledJson = Json(WrongStringLabel -> Json.fromString(""))
    an[DeserializationException] should be thrownBy EnvelopeStringJsonFormat.read(wrongLabeledJson.asJson)
  }

}
