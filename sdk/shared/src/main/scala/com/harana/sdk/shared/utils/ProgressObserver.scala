package com.harana.sdk.shared.utils

trait ProgressObserver {

  def updatePercentage(percentage: Int): Unit

  def updateStatus(message: String): Unit

}
