package com.harana.workflowexecutor.rabbitmq

import akka.actor.ActorRef
import com.harana.sdk.backend.models.designer.flow.utils.Logging
import com.harana.workflowexecutor.communication.mq.serialization.json.MQSerializer
import com.newmotion.akka.rabbitmq.{Channel, ChannelMessage}

/** Class used to publish data to exchange under given topic.
  *
  * @param exchange
  *   name of the Exchange
  * @param messageSerializer
  *   implementation of MessageMQSerializer that is able to serialize all messages published using this publisher
  * @param publisherActor
  *   created by rabbitmq
  */
case class MQPublisher(exchange: String, messageSerializer: MQSerializer, publisherActor: ActorRef) extends Logging {

  def publish(topic: String, message: Any): Unit = {
    val data: Array[Byte] = messageSerializer.serializeMessage(message)
    publisherActor ! ChannelMessage(publish(topic, data), dropIfNoChannel = false)
  }

  private def publish(topic: String, data: Array[Byte])(channel: Channel): Unit =
    channel.basicPublish(exchange, topic, null, data)

}
