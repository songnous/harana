package com.harana.sdk.shared.components.cards

import com.harana.sdk.shared.models.common.{Component, Invoice}
import io.circe.generic.JsonCodec

@JsonCodec
case class InvoiceCard(invoice: Invoice,
                       showTitle: Boolean,
                       showSocial: Boolean,
                       showMessaging: Boolean,
                       value: String) extends Component
