package com.harana.id.services

import java.util.Date

import com.harana.modules.airtable.Airtable
import com.harana.modules.clearbit.Clearbit
import com.harana.modules.clearbit.models.RiskResponse
import com.harana.modules.email.{Email, EmailMessage}
import com.harana.modules.handlebars.Handlebars
import com.harana.modules.mongo.Mongo
import com.harana.modules.stripe.{StripeCustomers, StripeSubscriptionItems, StripeSubscriptions}
import com.harana.sdk.shared.models.common.User
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.outr.stripe.subscription.{CreateSubscriptionItem, SubscriptionItem}
import dev.fuxing.airtable.AirtableRecord
import io.vertx.core.http.{Cookie, CookieSameSite}
import org.pac4j.core.profile.CommonProfile
import zio.{Task, UIO}

package object auth {

  def createAirtableRecord(airtable: Airtable.Service, config: Config.Service, firstName: String, lastName: String, emailAddress: String, ipAddress: String, riskResponse: RiskResponse) =
    for {
      airtableBase          <- config.string("signup.airtable.base")
      airtableTable         <- config.string("signup.airtable.table")
      airtableRecord        =  new AirtableRecord()
      _                     =  airtableRecord.putField("First Name", firstName)
      _                     =  airtableRecord.putField("Last Name", lastName)
      _                     =  airtableRecord.putField("Email", emailAddress)
      _                     =  airtableRecord.putField("IP Address", ipAddress)
      _                     =  airtableRecord.putField("Signup Date", new Date())
      _                     =  airtableRecord.putField("Risk Score", riskResponse.risk.score)
      _                     =  airtableRecord.putField("Social Match", riskResponse.email.socialMatch.getOrElse(false))
      _                     =  airtableRecord.putField("Name Match", riskResponse.email.nameMatch.getOrElse(false))
      _                     <- airtable.createRecords(airtableBase, airtableTable, List(airtableRecord))
    } yield ()


  def updateAirtableRecord(airtable: Airtable.Service, config: Config.Service, emailAddress: String, ipAddress: String, riskResponse: RiskResponse) =
    for {
      airtableBase          <- config.string("signup.airtable.base")
      airtableTable         <- config.string("signup.airtable.table")
      airtableRecord        =  new AirtableRecord()
      _                     =  airtableRecord.putField("Last Login Date", new Date())
      _                     <- airtable.patchRecords(airtableBase, airtableTable, List(airtableRecord))
    } yield ()


  def clearbitRisk(clearbit: Clearbit.Service, firstName: String, lastName: String, emailAddress: String, ipAddress: String): Task[(RiskResponse, String)] =
    for {
      riskResponse          <- clearbit.calculateRisk(emailAddress, ipAddress.substring(0, ipAddress.lastIndexOf(":")), firstName, lastName)
      risk                  =  riskResponse.risk.level
    } yield (riskResponse, risk)


  def sendEmail(config: Config.Service, email: Email.Service, handlebars: Handlebars.Service, logger: Logger.Service, template: String, firstName: String, lastName: String, emailAddress: String) =
    for {
      fromName              <- config.string("signup.email.confirmation.from.name")
      fromEmail             <- config.string("signup.email.confirmation.from.email")
      subject               <- config.string("signup.email.confirmation.subject")
      template              <- handlebars.renderString(template, Map(
                                  "firstName" -> firstName,
                                  "lastName" -> lastName,
                                  "emailAddress" -> emailAddress,
                                )).onError(e => logger.error(s"Failed to render Handlebars template due to: ${e.prettyPrint}"))
      message               =  EmailMessage(
                                  from = (fromEmail, fromName),
                                  to = List(emailAddress),
                                  subject = subject,
                                  message = template
                                )
      _                     <- email.send(message).onError(e => logger.error(s"Failed to send message due to: ${e.prettyPrint}"))
    } yield message
}