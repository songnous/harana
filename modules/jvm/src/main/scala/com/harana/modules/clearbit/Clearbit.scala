package com.harana.modules.clearbit

import com.harana.modules.clearbit.models.RiskResponse
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object Clearbit {

  type Clearbit = Has[Clearbit.Service]

  trait Service {

    def calculateRisk(emailAddress: String, ipAddress: String, firstName: String, lastName: String): Task[RiskResponse]

  }
}