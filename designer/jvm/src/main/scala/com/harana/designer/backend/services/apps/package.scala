package com.harana.designer.backend.services

import com.harana.sdk.shared.models.apps.App
import com.harana.sdk.shared.models.jwt.DesignerClaims
import skuber.Service

package object apps {

  def serviceName(app: App, claims: DesignerClaims) =
    s"${app.title}-${claims.emailAddress}".replace("@", "-").replace(".", "-").toLowerCase


  def serviceUrl(service: Service.Spec) =
    s"http://${service.externalIPs.head}:${service.ports.head}"

}