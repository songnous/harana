package com.harana.workflowexecutor.pyspark

import scala.jdk.CollectionConverters._

import com.typesafe.config.ConfigFactory

class PythonPathGenerator(private val pySparkPath: String) {

  val config = ConfigFactory.load.getConfig("pyspark")
  val additionalPythonPath = config.getStringList("python-path").asScala
  val additionalPaths = additionalPythonPath.map(p => s"$pySparkPath/$p")
  val pythonPathEnvKey = "PYTHONPATH"
  val envPythonPath = Option(System.getenv().get(pythonPathEnvKey))
  val generatedPythonPath = pySparkPath +: (additionalPaths ++ envPythonPath)

  def pythonPath(additionalPaths: String*) = (generatedPythonPath ++ additionalPaths).mkString(":")

  def env(additionalPaths: String*) = (pythonPathEnvKey, pythonPath(additionalPaths: _*))

}
