package com.harana.modules.mongo.bson.codec

import org.bson.{BsonReader, BsonWriter}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}

class BigDecimalStringCodec extends Codec[BigDecimal] {
  override def decode(reader: BsonReader, decoderContext: DecoderContext): BigDecimal = {
    BigDecimal(reader.readString())
  }

  override def encode(writer: BsonWriter, value: BigDecimal, encoderContext: EncoderContext): Unit = {
    writer.writeString(value.toString())
  }

  override def getEncoderClass: Class[BigDecimal] = classOf[BigDecimal]
}