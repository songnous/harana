package com.harana.designer.frontend.utils

import com.harana.sdk.shared.models.jwt.DesignerClaims

import scala.scalajs.js

object AuthUtils {

  def decode(jwt: String): Option[DesignerClaims] =
    try {
      val json = js.Dynamic.global.window.atob(jwt.split('.')(1)).toString
      io.circe.parser.decode[DesignerClaims](json).right.toOption
    } catch {
      case e: Exception => None
    }
}