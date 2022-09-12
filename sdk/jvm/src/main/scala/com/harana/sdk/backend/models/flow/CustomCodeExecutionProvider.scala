package com.harana.sdk.backend.models.flow

case class CustomCodeExecutionProvider(pythonCodeExecutor: CustomCodeExecutor,
                                       rCodeExecutor: CustomCodeExecutor,
                                       actionExecutionDispatcher: ActionExecutionDispatcher)
