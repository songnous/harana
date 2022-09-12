package com.harana.id.services.signup.models

import java.time.Instant

import com.harana.modules.clearbit.models.RiskResponse
import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.Id
import io.circe.generic.JsonCodec

@JsonCodec
case class Signup(id: EntityId,
                  displayId: String,
                  signupDate: Instant,
                  firstName: String,
                  lastName: String,
                  emailAddress: String,
                  ipAddress: String,
                  riskResponse: RiskResponse) extends Id
