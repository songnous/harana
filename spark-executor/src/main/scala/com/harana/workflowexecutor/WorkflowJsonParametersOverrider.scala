package com.harana.workflowexecutor

import io.circe.Json

object WorkflowJsonParametersOverrider extends DefaultJsonProtocol {

  def overrideParameters(workflow: Json, parameters: Map[String, String]) =
    parameters.foldLeft(workflow)(overrideParameter)

  private def overrideParameter(json: Json, param: (String, String)) = {
    val (key, value) = param
    val pathElems = key.split("\\.")
    val basePath = "workflow" / "nodes" / filter("id".is[String](_ == pathElems(0))) / "parameters"
    val path = pathElems.drop(1).foldLeft(basePath)((a, b) => a / b)
    json.update(path ! modify[String](_ => value))
  }
}