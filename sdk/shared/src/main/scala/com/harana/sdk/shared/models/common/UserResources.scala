package com.harana.sdk.shared.models.common

import io.circe.generic.JsonCodec

@JsonCodec
case class UserResources(diskSpace: Int,
                         flowsCPU: Int,
                         flowsExecutorCount: Int,
                         flowsExecutorMemory: Int,
                         terminalAllowRoot: Boolean,
                         terminalCPU: Int,
                         terminalMemory: Int)