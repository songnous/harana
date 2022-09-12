package com.harana.workflowexecutor.communication.mq.serialization.json

import com.harana.workflowexecutor.communication.message.notebook.KernelManagerReady
import com.harana.workflowexecutor.communication.message.notebook.KernelManagerReadyJsonProtocol.kernelManagerReadyFormat

object NotebookProtocol {

  val kernelManagerReady = "kernelManagerReady"

  object KernelManagerReadyDeserializer extends DefaultJsonMessageDeserializer[KernelManagerReady](kernelManagerReady)

}
