package com.harana.workflowexecutor.communication.mq.serialization.json

import com.harana.sdk.shared.models.designer.flow.flows.Workflow
import com.harana.sdk.shared.models.designer.flow.utils.Id

object MQCommunication {
  val mqActorSystemName = "rabbitmq"

  def subscriberName(topic: String): String = s"${topic}_subscriber_${System.currentTimeMillis().toString}"
  def publisherName(topic: String): String = s"${topic}_publisher"
  def queueName(topic: String): String = s"${topic}_to_executor"

  object Actor {

    object Publisher {
      val harana = prefixedName("harana")
      def notebook(id: Id): String = prefixedName(s"notebook_$id")
      def heartbeat(workflowId: Id): String = prefixedName(s"heartbeat_$workflowId")
      def heartbeatAll: String = prefixedName(s"heartbeat_all")
      def ready(id: Id): String = prefixedName(s"ready_$id")
      def workflow(id: Id): String = prefixedName(id.toString)
      private def prefixedName = name("publisher") _
    }

    object Subscriber {
      val harana = prefixedName("harana")
      val notebook = prefixedName("notebook")
      val workflows: String = prefixedName("workflows")
      private def prefixedName = name("subscriber") _
    }
    private[this] def name(prefix: String)(suffix: String): String = s"${prefix}_$suffix"
  }

  object Exchange {
    val harana = "harana"

    def heartbeats(workflowId: Id): String = s"${harana}_heartbeats_$workflowId"
    def heartbeatsAll: String = s"${harana}_heartbeats_all"
    def ready(workflowId: Id): String = s"${harana}_ready_$workflowId"
  }

  object Topic {
    private val workflowPrefix = "workflow"
    private val notebook = "notebook"
    private val kernelManager = "kernelmanager"
    def allWorkflowsSubscriptionTopic(workflowId: String): String = subscriptionTopic(s"$workflowPrefix.$workflowId.*")
    def haranaPublicationTopic(sessionId: String): String = publicationTopic(s"harana.$sessionId")
    val notebookSubscriptionTopic = subscriptionTopic(notebook)
    val notebookPublicationTopic = publicationTopic(notebook)
    def kernelManagerSubscriptionTopic(workflowId: Id, sessionId: String): String = subscriptionTopic(kernelManagerTopic(workflowId, sessionId))
    def workflowPublicationTopic(workflowId: Id, sessionId: String): String = publicationTopic(workflowTopic(workflowId, sessionId))
    private def kernelManagerTopic(workflowId: Id, sessionId: String): String = s"$kernelManager.$sessionId.${workflowId.toString}"
    private def workflowTopic(workflowId: Id, sessionId: String): String = s"$workflowPrefix.$sessionId.${workflowId.toString}"
    private def subscriptionTopic(topic: String): String = s"$topic.from"
    private def publicationTopic(topic: String): String = s"$topic.to"
  }
}