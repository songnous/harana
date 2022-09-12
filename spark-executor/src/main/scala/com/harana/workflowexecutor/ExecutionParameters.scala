package com.harana.workflowexecutor

import java.net.URL

case class ExecutionParameters(workflowFilename: Option[String] = None,
                               outputDirectoryPath: Option[String] = None,
                               extraVars: Map[String, String] = Map.empty,
                               interactiveMode: Boolean = false,
                               messageQueueHost: Option[String] = None,
                               messageQueuePort: Option[Int] = None,
                               messageQueueUser: Option[String] = None,
                               messageQueuePass: Option[String] = None,
                               customCodeExecutorsPath: Option[String] = None,
                               pythonBinaryPath: Option[String] = None,
                               workflowId: Option[String] = None,
                               wmAddress: Option[String] = None,
                               wmUsername: Option[String] = None,
                               wmPassword: Option[String] = None,
                               mailServerHost: Option[String] = None,
                               mailServerPort: Option[Int] = None,
                               mailServerUser: Option[String] = None,
                               mailServerPassword: Option[String] = None,
                               mailServerSender: Option[String] = None,
                               notebookServerAddress: Option[URL] = None,
                               datasourceServerAddress: Option[URL] = None,
                               depsZip: Option[String] = None,
                               userId: Option[String] = None,
                               tempPath: Option[String] = None)