package com.harana.modules.clearbit

import com.harana.modules.clearbit.Clearbit.Service
import com.harana.modules.clearbit.models.RiskResponse
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.core.okhttp.OkHttp
import zio.{Task, ZLayer}
import io.circe._
import io.circe.parser._

object LiveClearbit {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     okHttp: OkHttp.Service) => new Service {

    def calculateRisk(emailAddress: String, ipAddress: String, firstName: String, lastName: String): Task[RiskResponse] =
      for {
        apiKey      <- config.secret("clearbit-api-key")
        _           <- logger.debug(s"Calculating risk for email: $emailAddress")
        params      =  Map("email" -> emailAddress, "given_name" -> firstName, "family_name" -> lastName, "ip" -> ipAddress)
        response    <- okHttp.postForm("https://risk.clearbit.com/v1/calculate", params, credentials = Some((apiKey, ""))).mapError(e => new Exception(e.toString)).onError(e => logger.error(s"Failed to calculate risk: ${e.prettyPrint}"))
        risk        <- Task.fromEither(decode[RiskResponse](response.body().string())).onError(e => logger.error(s"Failed to decode risk to RiskResponse object: ${e.prettyPrint}"))
      } yield risk
  }}
}
