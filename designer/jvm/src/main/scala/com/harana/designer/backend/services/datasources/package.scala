package com.harana.designer.backend.services

import com.harana.modules.airbyte.{AirbyteOption, AirbyteProperty, AirbytePropertyType}
import com.harana.sdk.shared.models.flow.parameters._


package object datasources {

  def toParameter(ap: AirbyteProperty): Parameter[_] =
    ap.`type` match {
      case AirbytePropertyType.Boolean =>
        BooleanParameter(
          name = ap.name,
          default = ap.default.map(_.left.get.toBoolean),
          required = ap.required
        )

      case AirbytePropertyType.Integer =>
        IntParameter(
          name = ap.name,
          default = ap.default.map(p => p.toOption.get),
          required = ap.required,
          placeholder = ap.placeholder.map(_.toInt),
          allowNegative = ap.minimum.exists(_ > 0),
          options = ap.options.map {
            case _ @ AirbyteOption.Integer(value) => (value.toString, value)
          }
        )

// FIXME
//      case AirbytePropertyType.Object =>
//        Parameter.Object(
//          name = ap.name,
//          required = ap.required,
//          options = ap.options.map {
//            case _ @ AirbyteOption.Object(title, _, properties) => (title, properties.map(toParameter))
//          }
//        )

      case AirbytePropertyType.String =>
        StringParameter(
          name = ap.name,
          default = ap.default.map(a => a.left.get),
          required = ap.required,
          placeholder = ap.placeholder,
          options = ap.options.map {
            case _ @ AirbyteOption.String(value) => (value, value)
          }
        )

        // FIXME
      case _ =>
        BooleanParameter(
          name = ap.name,
          default = ap.default.map(_.left.get.toBoolean),
          required = ap.required
        )
    }
}
