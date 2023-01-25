package com.harana.modules.email

import com.harana.modules.core.config.Config
import com.harana.modules.email.Email.Service
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import javax.mail.internet.InternetAddress
import org.apache.commons.mail._
import models.{EmailAddress => hrmcEmailAddress}
import zio.{IO, ZLayer}

import scala.jdk.CollectionConverters._

object LiveEmail {
  val layer = ZLayer.fromServices { (config: Config.Service, logger: Logger.Service, micrometer: Micrometer.Service) => new Service {

      def isValid(email: String): Boolean =
        hrmcEmailAddress.isValid(email)

      def domain(email: String): String =
        hrmcEmailAddress.Domain(email)

      def obfuscate(email: String): String =
        hrmcEmailAddress(email).obfuscated.value

      def send(message: EmailMessage): IO[EmailException, String] = {

        val format =
          if (message.attachments.nonEmpty) MultiPart
          else if (message.richMessage.nonEmpty) Rich
          else Plain

        val commonsMail: Email = format match {
          case Plain => new SimpleEmail().setMsg(message.message)
          case Rich => new HtmlEmail().setHtmlMsg(message.richMessage.get).setTextMsg(message.message)
          case MultiPart =>
            val multipartEmail = new MultiPartEmail()
            message.attachments.foreach { file =>
              val attachment = new EmailAttachment()
              attachment.setPath(file.getAbsolutePath)
              attachment.setDisposition(EmailAttachment.ATTACHMENT)
              attachment.setName(file.getName)
              multipartEmail.attach(attachment)
            }
            multipartEmail.setMsg(message.message)
        }

        message.to.foreach(commonsMail.addTo)
        message.cc.foreach(commonsMail.addCc)
        message.bcc.foreach(commonsMail.addBcc)

        for {
          host      <- config.secret("email-host")
          auth      <- config.boolean("email.useAuthentication", default = false)
          username  <- config.secret("email-username")
          password  <- config.secret("email-password")
          ssl       <- config.boolean("email.useSSL", default = true)
          port      <- config.int("email.port", if (ssl) 25 else 587)
        } yield {
          commonsMail.setHostName(host)
          if (auth) commonsMail.setAuthentication(username, password)
          commonsMail.setSSLOnConnect(ssl)
          commonsMail.setSmtpPort(port)
          commonsMail.setFrom(message.from._1, message.from._2)
          commonsMail.setSubject(message.subject)
          if (message.replyTo.nonEmpty) commonsMail.setReplyTo(List(new InternetAddress(message.replyTo.get)).asJava)
          commonsMail.send()
        }
      }
    }
  }
}