package com.harana.sdk.shared.plugin

trait AuthenticationHandler extends Service {

  def authenticate(username: String, password: String): Boolean

}

object AuthenticationHandler {
  type AuthenticationHandlerId = String
}