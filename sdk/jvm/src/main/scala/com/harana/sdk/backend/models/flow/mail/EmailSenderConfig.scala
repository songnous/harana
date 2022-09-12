package com.harana.sdk.backend.models.flow.mail

import com.typesafe.config.{Config, ConfigFactory}
import jakarta.mail.{Authenticator, PasswordAuthentication, Session}

import java.util.Properties

case class EmailSenderAuthorizationConfig(user: String, password: String)

case class EmailSenderConfig(smtpHost: String, smtpPort: Int, from: String, authorizationConfig: Option[EmailSenderAuthorizationConfig]) {

  val sessionProperties: Properties = {
    val res = new Properties()
    res.put("mail.smtp.host", smtpHost)
    res.put("mail.smtp.port", smtpPort.toString)
    res.put("mail.from", from)
    res
  }

  private def mailAuthenticator: Option[Authenticator] = authorizationConfig.map { auth =>
    new Authenticator {
      override def getPasswordAuthentication = new PasswordAuthentication(auth.user, auth.password)
    }
  }

  def session = mailAuthenticator.map { authenticator =>
    Session.getInstance(sessionProperties, authenticator)
  }.getOrElse(Session.getInstance(sessionProperties))

}

object EmailSenderConfig {

  def apply(config: Config): EmailSenderConfig = {
    val smtpHost = config.getString("smtp.host")
    val smtpPort = config.getInt("smtp.port")
    val from     = config.getString("from")
    val auth     = EmailSenderAuthorizationConfig(config)
    EmailSenderConfig(smtpHost = smtpHost, smtpPort = smtpPort, from = from, authorizationConfig = auth)
  }

  def apply(): EmailSenderConfig = EmailSenderConfig(ConfigFactory.load().getConfig("email-sender"))

}

object EmailSenderAuthorizationConfig {

  def apply(config: Config): Option[EmailSenderAuthorizationConfig] =
    if (config.hasPath("user") && config.hasPath("pass")) {
      Some(
        EmailSenderAuthorizationConfig(
          user = config.getString("user"),
          password = config.getString("pass")
        )
      )
    } else
      None
}
