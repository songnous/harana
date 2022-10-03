package com.harana.sdk.backend.models.flow

import com.harana.sdk.shared.models.flow.ActionTypeInfo.ReportParameter
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterPair}
import com.harana.sdk.shared.models.flow.report.ReportType

object ReportTypeDefault {

  def apply(param: Parameter[ReportType]) =
    ParameterPair(param, ReportParameter.Extended())

}
