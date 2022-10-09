package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.components.cards.EventCard
import com.harana.sdk.shared.models.common.Component
import com.harana.sdk.shared.models.common.Parameter.Color
import com.harana.sdk.shared.models.flow.ActionTypeInfo
import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.parameters.custom.InnerWorkflow
import com.harana.sdk.shared.models.flow.parameters.selections.MultipleColumnSelection
import com.harana.sdk.shared.utils.HMap
import io.circe.derivation.{deriveDecoder, deriveEncoder}
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, Json}
import izumi.reflect.Tag

import scala.reflect.runtime.universe._
import java.util.Objects

trait Parameter[T] {
  val name: String
  val required: Boolean
  val default: Option[T]
  def constraints = ""
  val parameterType: ParameterType
  val isGriddable: Boolean = false

  def validate(value: T): List[FlowError] = List.empty
  def replicate(name: String): Parameter[T]
  def ->(value: T): ParameterPair[T] = ParameterPair(this, value)

  override def toString = s"Parameter($parameterType -> $name)"

  @unchecked
  override def equals(other: Any): Boolean = other match {
    case that: Parameter[T] => this.isInstanceOf[Parameter[T]] && name == that.name && parameterType == that.parameterType
    case _ => false
  }

  override def hashCode() = Objects.hash(name, parameterType)
}

object Parameter {
  class Values[K, V]

  implicit def decoder[T <: ActionTypeInfo]: Decoder[HMap[Parameter.Values]] = Decoder.instance[HMap[Parameter.Values]] { c =>
    var map = HMap.empty[Parameter.Values]

    c.downArray.values.getOrElse(List.empty[Json]).foreach { item =>
      val value = item.hcursor.downField("value").success.get

      item.hcursor.downField("type").as[String].getOrElse("Value type not found") match {
        case "array-double" =>                value.downArray.as[Array[Double]]
        case "array-int" =>                   value.downArray.as[Array[Int]]
        case "array-long" =>                  value.downArray.as[Array[Long]]
        case "array-string" =>                value.downArray.as[Array[String]]
        case "boolean" =>                     value.value.asBoolean.get
        case "string" =>                      map += (StringParameter(""), value.value.asString.get)
      }
    }

    Right(map)
  }

  implicit def encoder[T <: ActionTypeInfo]: Encoder[HMap[Parameter.Values]] = Encoder.instance[HMap[Parameter.Values]] { map =>
    map.underlying.map { case (key, value) =>
      val parameter = key.asInstanceOf[Parameter[_]]
      val valueJson = value match {
        case a: Array[Double] =>              ("array-double", a.map(Json.fromDouble).asJson)
        case a: Array[Int] =>                 ("array-int", a.map(Json.fromInt).asJson)
        case a: Array[Long] =>                ("array-long", a.map(Json.fromLong).asJson)
        case a: Array[String] =>              ("array-string", a.map(Json.fromString).asJson)
        case b: Boolean =>                    ("boolean", Json.fromBoolean(b))
        case c: Color =>                      ("color", deriveEncoder[Color](c))
        case d: Double =>                     ("double", Json.fromDouble(d).get)
        case i: InnerWorkflow =>              ("inner-workflow", deriveEncoder[InnerWorkflow](i))
        case i: Int =>                        ("int", Json.fromInt(i))
        case t: (String, Long) =>             ("ip-address", t.asJson)
        case j: Json =>                       ("json", j)
        case l: Long =>                       ("long", Json.fromLong(l))
        case m: MultipleColumnSelection =>    ("multiple-column-selection", deriveEncoder[MultipleColumnSelection](m))
//        case s: SingleColumnSelection =>      ("single-column-selection", deriveEncoder[SingleColumnSelection](s))
//        case s: SingleOrMultiColumnChoice =>  ("single-or-multi-column-choice", deriveEncoder[SingleOrMultiColumnChoice](s))
        case s: String =>                     ("string", Json.fromString(s))
      }
      Json.obj("name" -> Json.fromString(parameter.name), "type" -> Json.fromString(valueJson._1), "value" -> valueJson._2)
    }.asJson
  }

  implicit val parameterDecoder: Decoder[Parameter[_]] = Decoder.instance[Parameter[_]] { c =>
    val content = c.downField("parameter").success.get
    c.downField("type").as[String].getOrElse(throw new Exception("Parameter type not found")) match {
      case "boolean"                            => deriveDecoder[BooleanParameter].apply(content)
      case "code-snippet"                       => deriveDecoder[CodeSnippetParameter].apply(content)
      case "color"                              => deriveDecoder[ColorParameter].apply(content)
//      case "column-selector"                    => deriveDecoder[ColumnSelectorParameter].apply(content)
      case "country"                            => deriveDecoder[CountryParameter].apply(content)
      case "data-source"                        => deriveDecoder[DataSourceParameter].apply(content)
      case "double-array"                       => deriveDecoder[DoubleArrayParameter].apply(content)
      case "double"                             => deriveDecoder[DoubleParameter].apply(content)
      case "dynamic"                            => deriveDecoder[DynamicParameter].apply(content)
      case "email"                              => deriveDecoder[EmailParameter].apply(content)
      case "emoji"                              => deriveDecoder[EmojiParameter].apply(content)
      case "float-array"                        => deriveDecoder[FloatArrayParameter].apply(content)
      case "float"                              => deriveDecoder[FloatParameter].apply(content)
      case "html"                               => deriveDecoder[HTMLParameter].apply(content)
      case "int-array"                          => deriveDecoder[IntArrayParameter].apply(content)
      case "int"                                => deriveDecoder[IntParameter].apply(content)
//      case "io-columns"                         => deriveDecoder[IOColumnsParameter].apply(content)
      case "ip-address-list"                    => deriveDecoder[IPAddressListParameter].apply(content)
      case "ip-address"                         => deriveDecoder[IPAddressParameter].apply(content)
      case "json"                               => deriveDecoder[JSONParameter].apply(content)
      case "long-array"                         => deriveDecoder[LongArrayParameter].apply(content)
      case "long"                               => deriveDecoder[LongParameter].apply(content)
      case "markdown"                           => deriveDecoder[MarkdownParameter].apply(content)
      case "multiple-column-creator"            => deriveDecoder[MultipleColumnCreatorParameter].apply(content)
      case "multiple-numeric"                   => deriveDecoder[MultipleNumericParameter].apply(content)
      case "numeric"                            => deriveDecoder[NumericParameter].apply(content)
      case "password"                           => deriveDecoder[PasswordParameter].apply(content)
      case "prefix-based-column-creator"        => deriveDecoder[PrefixBasedColumnCreatorParameter].apply(content)
      case "search-query"                       => deriveDecoder[SearchQueryParameter].apply(content)
      case "single-column-creator"              => deriveDecoder[SingleColumnCreatorParameter].apply(content)
      case "single-column-selector"             => deriveDecoder[SingleColumnSelectorParameter].apply(content)
      case "string-array"                       => deriveDecoder[StringArrayParameter].apply(content)
      case "string"                             => deriveDecoder[StringParameter].apply(content)
      case "string-set"                         => deriveDecoder[StringSetParameter].apply(content)
      case "tags"                               => deriveDecoder[TagsParameter].apply(content)
      case "timestamp-columns"                  => deriveDecoder[TimestampColumnsParameter].apply(content)
      case "timestamp-parts"                    => deriveDecoder[TimestampPartsParameter].apply(content)
      case "time-zone"                          => deriveDecoder[TimeZoneParameter].apply(content)
      case "uri"                                => deriveDecoder[URIParameter].apply(content)
      case "workflow"                           => deriveDecoder[WorkflowParameter].apply(content)
    }
  }

  implicit def parameterEncoder: Encoder[Parameter[_]] = Encoder.instance[Parameter[_]] { parameter =>
    val pt = parameter match {
      case p: BooleanParameter                  => ("boolean", deriveEncoder[BooleanParameter].apply(p))
      case p: CodeSnippetParameter              => ("code-snippet", deriveEncoder[CodeSnippetParameter].apply(p))
      case p: ColorParameter                    => ("color", deriveEncoder[ColorParameter].apply(p))
//      case p: ColumnSelectorParameter           => ("column-selector", deriveEncoder[ColumnSelectorParameter].apply(p))
      case p: CountryParameter                  => ("country", deriveEncoder[CountryParameter].apply(p))
      case p: DataSourceParameter               => ("data-source", deriveEncoder[DataSourceParameter].apply(p))
      case p: DoubleArrayParameter              => ("double-array", deriveEncoder[DoubleArrayParameter].apply(p))
      case p: DoubleParameter                   => ("double", deriveEncoder[DoubleParameter].apply(p))
      case p: DynamicParameter                  => ("dynamic", deriveEncoder[DynamicParameter].apply(p))
      case p: EmailParameter                    => ("email", deriveEncoder[EmailParameter].apply(p))
      case p: EmojiParameter                    => ("emoji", deriveEncoder[EmojiParameter].apply(p))
      case p: FloatArrayParameter               => ("float-array", deriveEncoder[FloatArrayParameter].apply(p))
      case p: FloatParameter                    => ("float", deriveEncoder[FloatParameter].apply(p))
      case p: HTMLParameter                     => ("html", deriveEncoder[HTMLParameter].apply(p))
      case p: IntArrayParameter                 => ("int-array", deriveEncoder[IntArrayParameter].apply(p))
      case p: IntParameter                      => ("int", deriveEncoder[IntParameter].apply(p))
//      case p: IOColumnsParameter                => ("io-columns", deriveEncoder[IOColumnsParameter].apply(p))
      case p: IPAddressListParameter            => ("ip-address-list", deriveEncoder[IPAddressListParameter].apply(p))
      case p: IPAddressParameter                => ("ip-address", deriveEncoder[IPAddressParameter].apply(p))
      case p: JSONParameter                     => ("json", deriveEncoder[JSONParameter].apply(p))
      case p: LongArrayParameter                => ("long-array", deriveEncoder[LongArrayParameter].apply(p))
      case p: LongParameter                     => ("long", deriveEncoder[LongParameter].apply(p))
      case p: MarkdownParameter                 => ("markdown", deriveEncoder[MarkdownParameter].apply(p))
      case p: MultipleColumnCreatorParameter    => ("multiple-column-creator", deriveEncoder[MultipleColumnCreatorParameter].apply(p))
      case p: MultipleNumericParameter          => ("multiple-numeric", deriveEncoder[MultipleNumericParameter].apply(p))
      case p: NumericParameter                  => ("numeric", deriveEncoder[NumericParameter].apply(p))
      case p: PasswordParameter                 => ("password", deriveEncoder[PasswordParameter].apply(p))
      case p: PrefixBasedColumnCreatorParameter => ("prefix-based-column-creator", deriveEncoder[PrefixBasedColumnCreatorParameter].apply(p))
      case p: SearchQueryParameter              => ("search-query", deriveEncoder[SearchQueryParameter].apply(p))
      case p: SingleColumnCreatorParameter      => ("single-column-creator", deriveEncoder[SingleColumnCreatorParameter].apply(p))
      case p: SingleColumnSelectorParameter     => ("single-column-selector", deriveEncoder[SingleColumnSelectorParameter].apply(p))
      case p: StringArrayParameter              => ("string-array", deriveEncoder[StringArrayParameter].apply(p))
      case p: StringParameter                   => ("string", deriveEncoder[StringParameter].apply(p))
      case p: StringSetParameter                => ("string-set", deriveEncoder[StringSetParameter].apply(p))
      case p: TagsParameter                     => ("tags", deriveEncoder[TagsParameter].apply(p))
      case p: TimestampColumnsParameter         => ("timestamp-columns", deriveEncoder[TimestampColumnsParameter].apply(p))
      case p: TimestampPartsParameter           => ("timestamp-parts", deriveEncoder[TimestampPartsParameter].apply(p))
      case p: TimeZoneParameter                 => ("time-zone", deriveEncoder[TimeZoneParameter].apply(p))
      case p: URIParameter                      => ("uri", deriveEncoder[URIParameter].apply(p))
      case p: WorkflowParameter                 => ("workflow", deriveEncoder[WorkflowParameter].apply(p))
    }
    io.circe.Json.obj("type" -> Json.fromString(pt._1), "parameter" -> pt._2)
  }
}