package com.harana.sdk.shared.plugin

import com.harana.sdk.shared.models.common.Parameter.ParameterName
import com.harana.sdk.shared.models.common.{Parameter, ParameterValue}
trait Service {

  def name: String

	def title: String

  def description: String

  def instanceParameters: List[Parameter]

  def globalParameters: List[Parameter]

  def onStartup(): Unit

  def onShutdown(): Unit

  def onAppContextChange(newContext: Map[String, _]): Unit

  def onUserConfigure(newparameterValues: Map[ParameterName, ParameterValue]): Unit

}

object Service {
	type ServiceId = String
}