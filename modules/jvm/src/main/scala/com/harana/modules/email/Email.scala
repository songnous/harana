package com.harana.modules.email

import org.apache.commons.mail.EmailException
import zio.macros.accessible
import zio.{Has, IO}

@accessible
object Email {
  type Email = Has[Email.Service]
  type EmailAddress = String

  trait Service {
    def isValid(email: String): Boolean

    def domain(email: String): String

    def obfuscate(email: String): String

    def send(message: EmailMessage): IO[EmailException, String]
  }
}