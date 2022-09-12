package org.apache.spark.api.r


class SparkRBackend {

  private val backend: RBackend = new RBackend()
  private val backendThread: Thread = new Thread("SparkRBackend") {
    override def run(): Unit = backend.run()
  }

  private var portNumber: Int = _
  private var entryPointTrackingId: String = _

  def start(entryPoint: Object): Unit = {
    entryPointTrackingId = backend.jvmObjectTracker.addAndGetId(entryPoint).id
    portNumber = backend.init()._1
    backendThread.start()
  }

  def close(): Unit = {
    backend.close()
    backendThread.join()
  }

  def port: Int = portNumber

  def entryPointId: String = entryPointTrackingId
}
