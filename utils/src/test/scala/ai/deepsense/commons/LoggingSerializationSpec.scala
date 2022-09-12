package com.harana.utils

import org.slf4j.Logger
import com.harana.utils.serialization.Serialization

class LoggingSerializationSpec extends StandardSpec with UnitTestSupport with Serialization {

  "Object" when {
    "mixes-in SerializableLogging" should {
      "be serializable" in {
        val testObject   = new SerializableTestObject()
        testObject.getLogger.trace("Logging just to force initiation of lazy logger")
        val deserialized = serializeDeserialize[SerializableTestObject](testObject)
        deserialized.getLogger should not be null
        deserialized.getLogger.trace("If this is printed everything is OK")
      }
    }
  }

}

class SerializableTestObject extends Serializable with Logging {

  def getLogger: Logger = this.logger

}
