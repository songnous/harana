package com.harana.sdk.backend.models.flow

object Jenkins {

  def isRunningOnJenkins = userName == "Jenkins" || userName == "jenkins"

  private def userName = System.getProperty("user.name")

}
