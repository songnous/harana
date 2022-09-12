package com.harana.designer.backend.services.settings.models

sealed trait ListPlansError
object ListPlansError {
}

sealed trait ChangePlanError
object ChangePlanError {
}

sealed trait CancelPlanError
object CancelPlanError {
}

sealed trait GetUserProfileError
object GetUserProfileError {
}

sealed trait SaveUserProfileError
object SaveUserProfileError {
}

sealed trait ListConnectionsError
object ListConnectionsError {
}

sealed trait DeleteConnectionError
object DeleteConnectionError {
}

sealed trait SaveConnectionError
object SaveConnectionError {
}

sealed trait TestConnectionError
object TestConnectionError {
}