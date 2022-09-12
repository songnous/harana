package com.harana.modules

import java.io.File

import com.harana.modules.email.Email.EmailAddress

package object email {

  sealed abstract class MailType
  case object Plain extends MailType
  case object Rich extends MailType
  case object MultiPart extends MailType

  case class EmailMessage(from: (EmailAddress, String),
                          replyTo: Option[EmailAddress] = None,
                          to: List[EmailAddress],
                          cc: List[EmailAddress] = List(),
                          bcc: List[EmailAddress] = List(),
                          subject: String,
                          message: String,
                          richMessage: Option[String] = None,
                          attachments: List[File] = List())
}