package com.harana.sdk.backend.models.flow.mail

import com.harana.sdk.backend.models.flow.mail.templates.Template
import com.harana.sdk.backend.models.flow.mail.templates.TemplateInstanceToLoad
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.backend.models.flow.mail.templates.{Template, TemplateInstanceToLoad}
import com.harana.sdk.backend.models.flow.utils.Logging
import com.sun.mail.smtp.SMTPTransport
import jakarta.activation.DataHandler
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeBodyPart
import jakarta.mail.internet.MimeMessage
import jakarta.mail.internet.MimeMultipart
import jakarta.mail.util.ByteArrayDataSource
import jakarta.mail._

import java.io.InputStream
import scala.jdk.CollectionConverters._
import scala.util.Failure
import scala.util.Success
import scala.util.Try

class EmailSender private (emailSenderConfig: EmailSenderConfig) extends Logging {

  import EmailSender._

  val session: Session = emailSenderConfig.session

  private def createEmptyMessage: MimeMessage = new MimeMessage(session)

  private def createMessage(subject: String, to: Seq[String]): MimeMessage = {
    val msg = createEmptyMessage
    msg.setSubject(subject)
    val toAddresses = to.flatMap(InternetAddress.parse(_, false).asInstanceOf[Array[Address]])
    msg.setRecipients(Message.RecipientType.TO, toAddresses.toArray)
    msg.setFrom(InternetAddress.parse(emailSenderConfig.from).head)
    msg
  }

  def createTextMessage(subject: String, text: String, subtype: String, to: Seq[String]) = {
    val msg = createMessage(subject, to)
    msg.setText(text, null, subtype)
    msg
  }

  def createPlainMessage(subject: String, text: String, to: Seq[String]) =
    createTextMessage(subject, text, "plain", to)

  def createHtmlMessage(subject: String, html: String, to: Seq[String]) =
    createTextMessage(subject, html, "html", to)

  def createHtmlMessageFromTemplate[T: Template](subject: String, templateInstance: TemplateInstanceToLoad, to: Seq[String]) = {

    val TemplateInstanceToLoad(templateName, templateContext) = templateInstance

    for {
      template <- implicitly[Template[T]].loadTemplate(templateName)
      html      = implicitly[Template[T]].renderTemplate(template, templateContext)
    } yield createHtmlMessage(subject, html, to)
  }

  def sendEmail(message: Message) = {
    val transport = session.getTransport("smtp").asInstanceOf[SMTPTransport]
    val sent = Try {
      transport.connect()
      transport.sendMessage(message, message.getAllRecipients)
    }

    transport.close()

    sent match {
      case Success(_) if logger.isDebugEnabled => println(s"Mail sent to ${recipientsForLogging(message)}")
      case Failure(t)                          => logger.error(s"Unable to send message to ${recipientsForLogging(message)}", t)
      case _                                   =>
    }

    sent.failed.toOption
  }

  private def copyMessageWithoutContent(oldMsg: MimeMessage) = {
    val newMsg = createEmptyMessage
    oldMsg.saveChanges()
    copyMessageHeaders(oldMsg, newMsg)
    newMsg
  }

  private def copyMessageHeaders(from: MimeMessage, to: MimeMessage) =
    for (line <- from.getAllHeaderLines.asScala)
      to.addHeaderLine(line)

  private def createAttachmentBodyPart(attachmentStream: InputStream, filename: String, contentTypeOpt: Option[String]) = {
    val contentType = contentTypeOpt.getOrElse("application/octet-stream")
    val bodyPart    = new MimeBodyPart()
    bodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(attachmentStream, contentType)))
    bodyPart.setFileName(filename)

    bodyPart
  }

  def attachAttachment(
      msg: Message,
      attachment: InputStream,
      filename: String,
      contentTypeOpt: Option[String]
  ): Message = {

    val attachmentBodyPart = createAttachmentBodyPart(
      attachment,
      filename,
      contentTypeOpt
    )

    if (msg.getContentType.toLowerCase.startsWith("multipart") || msg.getContent.isInstanceOf[Multipart]) {
      msg.getContent.asInstanceOf[Multipart].addBodyPart(attachmentBodyPart)
      msg
    } else {
      val newMsg    = copyMessageWithoutContent(msg.asInstanceOf[MimeMessage])
      val multipart = new MimeMultipart()
      val firstPart = new MimeBodyPart()
      firstPart.setContent(msg.getContent, msg.getContentType)
      multipart.addBodyPart(firstPart)
      multipart.addBodyPart(attachmentBodyPart)
      newMsg.setContent(multipart)
      newMsg
    }
  }
}

object EmailSender {

  def apply(emailSenderConfig: EmailSenderConfig): EmailSender = new EmailSender(emailSenderConfig)

  def apply(): EmailSender = new EmailSender(EmailSenderConfig())

  private def recipientsForLogging(msg: Message) = msg.getAllRecipients.mkString("[", ", ", "]")

}
