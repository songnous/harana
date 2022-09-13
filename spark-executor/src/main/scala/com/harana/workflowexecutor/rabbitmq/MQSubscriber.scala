package com.harana.workflowexecutor.rabbitmq

import akka.actor.ActorRef
import com.harana.sdk.backend.models.flow.utils.{Logging, Serialization}
import com.harana.workflowexecutor.communication.mq.serialization.json.MQDeserializer
import com.newmotion.akka.rabbitmq.BasicProperties
import com.newmotion.akka.rabbitmq.Channel
import com.newmotion.akka.rabbitmq.DefaultConsumer
import com.newmotion.akka.rabbitmq.Envelope

case class MQSubscriber(subscriberActor: ActorRef, mqMessageDeserializer: MQDeserializer, channel: Channel) extends DefaultConsumer(channel) with Logging with Serialization {

  override def handleDelivery(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte]) =
    try
      subscriberActor ! mqMessageDeserializer.deserializeMessage(body)
    catch {
      case e: Exception => logger.error("Message deserialization failed", e)
    }
}