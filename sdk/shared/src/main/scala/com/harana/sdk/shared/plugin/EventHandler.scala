package com.harana.sdk.shared.plugin

import com.harana.sdk.shared.models.common.Event
import com.harana.sdk.shared.utils.ProgressObserver

trait EventHandler extends Service {
  def handleEvent(event: Event, progressObserver: ProgressObserver): Unit
}

object EventHandler {
  type EventHandlerId = String
}