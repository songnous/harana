package com.harana.designer.backend.services

import com.harana.modules.airbyte.{AirbyteOption, AirbyteProperty, AirbytePropertyType}

import java.lang.Boolean
import com.harana.sdk.shared.models.common.{Parameter, ParameterValue}

package object datasources {

  def toParameter(ap: AirbyteProperty): Parameter =
    ap.`type` match {
      case AirbytePropertyType.Boolean =>
        Parameter.Boolean(
          name = ap.name,
          default = ap.default.map(a => ParameterValue.Boolean(Boolean.valueOf(a.left.get))),
          required = ap.required
        )

      case AirbytePropertyType.Integer =>
        Parameter.Integer(
          name = ap.name,
          default = ap.default.map(p => ParameterValue.Integer(p.toOption.get)),
          required = ap.required,
          placeholder = ap.placeholder.map(_.toInt),
          allowNegative = ap.minimum.map(_ > 0),
          allowPositive = Some(true),
          options = ap.options.map {
            case _ @ AirbyteOption.Integer(value) => (value.toString, ParameterValue.Integer(value))
          }
        )

      case AirbytePropertyType.Object =>
        Parameter.Object(
          name = ap.name,
          required = ap.required,
          options = ap.options.map {
            case _ @ AirbyteOption.Object(title, _, properties) => (title, properties.map(toParameter))
          }
        )

      case AirbytePropertyType.String =>
        Parameter.String(
          name = ap.name,
          default = ap.default.map(a => ParameterValue.String(a.left.get)),
          required = ap.required,
          placeholder = ap.placeholder,
          options = ap.options.map {
            case _ @ AirbyteOption.String(value) => (value, ParameterValue.String(value))
          }
        )

        // FIXME
      case _ =>
        Parameter.Boolean(
          name = ap.name,
          default = ap.default.map(a => ParameterValue.Boolean(Boolean.valueOf(a.left.get))),
          required = ap.required
        )
    }
}
