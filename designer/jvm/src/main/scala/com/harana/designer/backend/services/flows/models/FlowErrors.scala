package com.harana.designer.backend.services.flows.models

sealed trait RunFlowError
object RunFlowError {
}

sealed trait StopFlowError
object StopFlowError {
}

sealed trait PreviewLinkError
object PreviewLinkError {
}

sealed trait MetricsError
object MetricsError {
}

sealed trait LogsError
object LogsError {
}

sealed trait ActionParametersError
object ActionParametersError {
}

sealed trait ShareViaEmailError
object ShareViaEmailError {
}