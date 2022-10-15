package com.harana.sdk.shared.models.flow.parameters.validators

import com.harana.sdk.shared.models.flow.exceptions.FlowError
import io.circe.derivation.{deriveDecoder, deriveEncoder}
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, HCursor, Json}

@SerialVersionUID(1)
trait Validator[ParameterType] extends Serializable {

  def validate(name: String, parameter: ParameterType): List[FlowError]

  def toHumanReadable(parameterName: String) = ""

}

object Validator {

  implicit def decoder[T: Decoder]: Decoder[Validator[T]] = Decoder.instance[Validator[T]] { c =>
    val content = c.downField("validator").success.get
    val decoder = c.downField("type").as[String].getOrElse(throw new Exception("Entity type not found")) match {
      case "AcceptAll"                => Right(RegexValidator.AcceptAll)
      case "ArrayLengthValidator"     => deriveDecoder[ArrayLengthValidator].apply(content)
      case "ArrayValidator"           => deriveDecoder[ArrayValidator[T]].apply(content)
      case "ComplexArrayValidator"    => deriveDecoder[ComplexArrayValidator[T]].apply(content)
      case "Name"                     => Right(ColumnValidator.Name)
      case "PrefixName"               => Right(ColumnValidator.PrefixName)
      case "RangeValidator"           => deriveDecoder[RangeValidator[T]].apply(content)
      case "SingleChar"               => Right(RegexValidator.SingleChar)
      case "URI"                      => Right(RegexValidator.URI)
    }
    decoder.asInstanceOf[Decoder.Result[Validator[T]]]
  }

  implicit def encoder[T: Encoder]: Encoder[Validator[T]] = Encoder.instance[Validator[T]] { entity =>
    val entityType = entity.getClass.getSimpleName.replace("$", "")
    val json = entityType match {
      case "AcceptAll"                => Json.Null
      case "ArrayLengthValidator"     => deriveEncoder[ArrayLengthValidator].apply(entity.asInstanceOf[ArrayLengthValidator])
      case "ArrayValidator"           => deriveEncoder[ArrayValidator[T]].apply(entity.asInstanceOf[ArrayValidator[T]])
      case "ComplexArrayValidator"    => deriveEncoder[ComplexArrayValidator[T]].apply(entity.asInstanceOf[ComplexArrayValidator[T]])
      case "Name"                     => Json.Null
      case "PrefixName"               => Json.Null
      case "RangeValidator"           => deriveEncoder[RangeValidator[T]].apply(entity.asInstanceOf[RangeValidator[T]])
      case "SingleChar"               => Json.Null
      case "URI"                      => Json.Null
    }
    io.circe.Json.obj("type" -> entityType.asJson, "validator" -> json)
  }
}