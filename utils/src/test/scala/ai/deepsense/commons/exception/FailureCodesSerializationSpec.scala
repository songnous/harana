package com.harana.utils.exception

import com.harana.utils.StandardSpec
import com.harana.utils.exception.FailureCode.FailureCode
import com.harana.utils.serialization.Serialization

class FailureCodesSerializationSpec extends StandardSpec with Serialization {

  "FailureCode" should {
    "serialize and deserialize" in {
      val code         = FailureCode.UnexpectedError
      val serialized   = serialize(code)
      val deserialized = deserialize[FailureCode](serialized)
      deserialized shouldBe code
    }
  }

}
