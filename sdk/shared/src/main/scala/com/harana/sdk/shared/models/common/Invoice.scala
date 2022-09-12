package com.harana.sdk.shared.models.common

import java.time.Instant

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.Invoice.InvoiceId
import com.harana.sdk.shared.models.common.User.UserId
import enumeratum._
import io.circe.generic.JsonCodec
import squants.Money
import com.harana.sdk.shared.utils.CirceCodecs._
import com.harana.sdk.shared.utils.Random

@JsonCodec
case class Invoice(name: String,
                   description: String,
                   amount: Money,
                   recipient: String,
                   paymentStatus: PaymentStatus,
                   paymentMethod: PaymentMethod,
                   dueTime: Instant,
                   createdBy: Option[UserId],
                   created: Instant,
                   updatedBy: Option[UserId],
                   updated: Instant,
                   id: InvoiceId,
                   status: Status,
                   visibility: Visibility,
                   version: Long,
                   tags: Set[String],
                   relationships: Map[String, EntityId])
    extends Entity with Serializable {

  type EntityType = Invoice
}

object Invoice {
  type InvoiceId = String

  def apply(name: String, description: String, amount: Money, recipient: String, paymentStatus: PaymentStatus, paymentMethod: PaymentMethod, dueTime: Instant, createdBy: Option[User], visibility: Visibility, tags: Set[String]): Invoice = {
    apply(name, description, amount, recipient, paymentStatus, paymentMethod, dueTime, createdBy.map(_.id), Instant.now, createdBy.map(_.id), Instant.now, Random.long, Status.Active, visibility, 1L, tags, Map())
  }
}

sealed trait PaymentMethod extends EnumEntry
case object PaymentMethod extends Enum[PaymentMethod] with CirceEnum[PaymentMethod] {
  case object Mastercard extends PaymentMethod
  case object Visa extends PaymentMethod
  case object Amex extends PaymentMethod
  case object Diners extends PaymentMethod
  case object Paypal extends PaymentMethod
  case object Bitcoin extends PaymentMethod
  case object Cash extends PaymentMethod
  case object MoneyOrder extends PaymentMethod
  case object Cheque extends PaymentMethod
  val values = findValues
}

sealed trait PaymentStatus extends EnumEntry
case object PaymentStatus extends Enum[PaymentStatus] with CirceEnum[PaymentStatus] {
  case object Overdue extends PaymentStatus
  case object Pending extends PaymentStatus
  case object Paid extends PaymentStatus
  case object OnHold extends PaymentStatus
  case object Cancelled extends PaymentStatus
  val values = findValues
}
