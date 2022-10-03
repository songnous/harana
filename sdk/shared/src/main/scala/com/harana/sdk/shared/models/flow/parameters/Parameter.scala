package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.common.Parameter.Color
import com.harana.sdk.shared.models.flow.ActionTypeInfo
import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.parameters.custom.InnerWorkflow
import com.harana.sdk.shared.models.flow.parameters.selections.MultipleColumnSelection
import com.harana.sdk.shared.utils.HMap
import io.circe.derivation.deriveEncoder
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, Json}
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

  implicit def decoder[T <: ActionTypeInfo](implicit tt: TypeTag[T]): Decoder[HMap[Parameter.Values]] = Decoder.instance[HMap[Parameter.Values]] { c =>
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

  implicit def encoder[T <: ActionTypeInfo](implicit tt: TypeTag[T]): Encoder[HMap[Parameter.Values]] = Encoder.instance[HMap[Parameter.Values]] { map =>
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
}