package com.harana.sdk.shared.models.common

import io.circe.generic.JsonCodec

@JsonCodec
case class GeoAddress(unitNumber: String,
                      streetNumber: String,
                      streetName: String,
                      streetType: String,
                      postalCode: String,
                      city: String,
                      state: String,
                      country: String)
