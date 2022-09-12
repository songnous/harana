package com.harana.modules.mixpanel


import java.util.UUID
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.mixpanel.Mixpanel.Service
import com.mixpanel.mixpanelapi.{ClientDelivery, MessageBuilder, MixpanelAPI}
import org.json.{JSONArray, JSONObject}
import zio.{IO, Task, ZLayer}

import scala.collection.JavaConverters._

object LiveMixpanel {

  private val api = new MixpanelAPI()

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    private val messageBuilder = config.secret("mixpanel-token").map(t => new MessageBuilder(t))

    def append(id: UUID, properties: Map[String, Object], modifiers: Map[String, Object] = Map()) =
      for {
        mb <- messageBuilder
        r <- Task(mb.append(id.toString, toJson(properties), toJson(modifiers)))
      } yield r


    def delete(id: UUID, modifiers: Map[String, Object] = Map()) =
      for {
        mb <- messageBuilder
        r <- Task(mb.delete(id.toString, toJson(modifiers)))
      } yield r


    def event(id: UUID, name: String, properties: Map[String, Object]) =
      for {
        mb <- messageBuilder
        r <- Task(mb.event(id.toString, name, toJson(properties)))
      } yield r


    def groupDelete(groupKey: String, groupId: String, modifiers: Map[String, Object] = Map()) =
      for {
        mb <- messageBuilder
        r <- Task(mb.groupDelete(groupKey, groupId, toJson(modifiers)))
      } yield r


    def groupMessage(groupKey: String, groupId: String, actionType: String, properties: Map[String, Object], modifiers: Map[String, Object] = Map()) =
      for {
        mb <- messageBuilder
        r <- Task(mb.groupMessage(groupKey, groupId, actionType, toJson(properties), toJson(modifiers)))
      } yield r


    def groupRemove(groupKey: String, groupId: String, properties: Map[String, Object], modifiers: Map[String, Object] = Map()) =
      for {
        mb <- messageBuilder
        r <- Task(mb.groupRemove(groupKey, groupId, toJson(properties), toJson(modifiers)))
      } yield r


    def groupSet(groupKey: String, groupId: String, properties: Map[String, Object], modifiers: Map[String, Object] = Map()) =
      for {
        mb <- messageBuilder
        r <- Task(mb.groupSet(groupKey, groupId, toJson(properties), toJson(modifiers)))
      } yield r


    def groupSetOnce(groupKey: String, groupId: String, properties: Map[String, Object], modifiers: Map[String, Object] = Map()) =
      for {
        mb <- messageBuilder
        r <- Task(mb.groupSetOnce(groupKey, groupId, toJson(properties), toJson(modifiers)))
      } yield r


    def groupUnion(groupKey: String, groupId: String, properties: Map[String, JSONArray], modifiers: Map[String, Object] = Map()) =
      for {
        mb <- messageBuilder
        r <- Task(mb.groupUnion(groupKey, groupId, properties.asJava, toJson(modifiers)))
      } yield r


    def groupUnset(groupKey: String, groupId: String, propertyNames: List[String], modifiers: Map[String, Object] = Map()) =
      for {
        mb <- messageBuilder
        r <- Task(mb.groupUnset(groupKey, groupId, propertyNames.asJava, toJson(modifiers)))
      } yield r


    def increment(id: UUID, properties: Map[String, Long], modifiers: Map[String, String] = Map()) =
      for {
        mb <- messageBuilder
        r <- Task(mb.increment(id.toString, properties.mapValues(Long.box).asJava, toJson(modifiers)))
      } yield r


    def peopleMessage(id: UUID, actionType: String, properties: Map[String, Object], modifiers: Map[String, Object] = Map()) =
      for {
        mb <- messageBuilder
        r <- Task(mb.peopleMessage(id.toString, actionType, toJson(properties), toJson(modifiers)))
      } yield r


    def remove(id: UUID, properties: Map[String, Object], modifiers: Map[String, Object] = Map()) =
      for {
        mb <- messageBuilder
        r <- Task(mb.remove(id.toString, toJson(properties), toJson(modifiers)))
      } yield r


    def send(messages: List[JSONObject]) = {
      val delivery = new ClientDelivery()
      messages.foreach(delivery.addMessage)
      IO(api.deliver(delivery)).unit
    }


    def set(id: UUID, properties: Map[String, Object], modifiers: Map[String, Object] = Map()) =
      for {
        mb <- messageBuilder
        r <- Task(mb.set(id.toString, toJson(properties), toJson(modifiers)))
      } yield r


    def setOnce(id: UUID, properties: Map[String, Object], modifiers: Map[String, Object] = Map()) =
      for {
        mb <- messageBuilder
        r <- Task(mb.setOnce(id.toString, toJson(properties), toJson(modifiers)))
      } yield r


    def trackCharge(id: UUID, amount: Double, properties: Map[String, Object], modifiers: Map[String, Object] = Map()) =
      for {
        mb <- messageBuilder
        r <- Task(mb.trackCharge(id.toString, amount, toJson(properties), toJson(modifiers)))
      } yield r


    def union(id: UUID, properties: Map[String, JSONArray], modifiers: Map[String, Object] = Map()) =
      for {
        mb <- messageBuilder
        r <- Task(mb.union(id.toString, properties.asJava, toJson(modifiers)))
      } yield r


    def unset(id: UUID, propertyNames: List[String], modifiers: Map[String, String] = Map()) =
      for {
        mb <- messageBuilder
        r <- Task(mb.unset(id.toString, propertyNames.asJava, toJson(modifiers)))
      } yield r


    private def toJson(properties: Map[String, Object]) = {
      val json = new JSONObject
      properties.foreach(p => json.put(p._1, p._2))
      json
    }
  }}
}