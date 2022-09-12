package com.harana.workflowexecutor.communication.protocol

case class Heartbeat(workflowId: String, sparkUiAddress: Option[String])