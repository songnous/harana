package com.harana.sdk.shared.models.flow

sealed class Trigger(name: String, id: String)

object Trigger {
  case class FileArrival(name: String, id: String) extends Trigger(name, id)
  case class AWSSNSNotification(name: String, id: String) extends Trigger(name, id)
  case class Schedule(name: String, id: String, schedule: String) extends Trigger(name, id)
}