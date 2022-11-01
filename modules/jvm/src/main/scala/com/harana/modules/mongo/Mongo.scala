package com.harana.modules.mongo

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.Id
import io.circe.{Decoder, Encoder}
import org.bson.BsonDocument
import org.mongodb.scala.bson.conversions._
import org.mongodb.scala.model.IndexOptions
import zio.macros.accessible
import zio.{Has, Task}

import scala.reflect.runtime.universe._

@accessible
object Mongo {
  type Mongo = Has[Mongo.Service]

  trait Service {

    def ping: Task[Unit]

    def aggregate(collectionName: String, stages: List[Bson]): Task[List[BsonDocument]]

    def get[E <: Id](collectionName: String, id: EntityId)(implicit tt: TypeTag[E], d: Decoder[E]): Task[Option[E]]

    def insert[E <: Id](collectionName: String, entity: E)(implicit tt: TypeTag[E], e: Encoder[E]): Task[Unit]

    def insertMany[E <: Id](collectionName: String, entities: List[E])(implicit tt: TypeTag[E], e: Encoder[E]): Task[Unit]

    def upsert[E <: Id](collectionName: String, entity: E)(implicit tt: TypeTag[E], e: Encoder[E]): Task[Unit]

    def update[E <: Id](collectionName: String, entity: E)(implicit tt: TypeTag[E], e: Encoder[E]): Task[Unit]

    def updateFields(collectionName: String, id: EntityId, keyValues: Map[String, Object]): Task[Unit]

    def appendToListField(collectionName: String, id: EntityId, field: String, value: Object): Task[Unit]

    def replace[E <: Id](collectionName: String, id: EntityId, entity: E, upsert: Boolean)(implicit tt: TypeTag[E], e: Encoder[E]): Task[Unit]

    def delete[E <: Id](collectionName: String, bson: Bson)(implicit tt: TypeTag[E]): Task[Unit]

    def delete[E <: Id](collectionName: String, id: EntityId)(implicit tt: TypeTag[E]): Task[Unit]

    def deleteEquals[E <: Id](collectionName: String, keyValues: Map[String, Object])(implicit tt: TypeTag[E]): Task[Unit]

    def distinctEquals(collectionName: String, field: String, keyValues: Map[String, Object]): Task[List[String]]

    def findEquals[E <: Id](collectionName: String, keyValues: Map[String, Object], sort: Option[(String, Boolean)] = None)(implicit tt: TypeTag[E], d: Decoder[E]): Task[List[E]]

    def findOne[E <: Id](collectionName: String, keyValues: Map[String, Object], sort: Option[(String, Boolean)] = None)(implicit tt: TypeTag[E], d: Decoder[E]): Task[Option[E]]

    def findOneAndDelete[E <: Id](collectionName: String, findValues: Map[String, Object], sort: Option[(String, Boolean)] = None)(implicit tt: TypeTag[E], d: Decoder[E]): Task[Option[E]]

    def find[E <: Id](collectionName: String, bson: Bson, sort: Option[(String, Boolean)] = None)(implicit tt: TypeTag[E], d: Decoder[E]): Task[List[E]]

    def countEquals(collectionName: String, keyValues: Map[String, Object]): Task[Long]

    def count(collectionName: String, bson: Bson): Task[Long]

    def textSearch[E <: Id](collectionName: String, text: String)(implicit tt: TypeTag[E], d: Decoder[E]): Task[List[E]]

    def textSearchFindEquals[E <: Id](collectionName: String, text: String, keyValues: Map[String, Object])(implicit tt: TypeTag[E], d: Decoder[E]): Task[List[E]]

    def all[E <: Id](collectionName: String, sort: Option[(String, Boolean)] = None)(implicit tt: TypeTag[E], d: Decoder[E]): Task[List[E]]

    def createIndex[E <: Id](collectionName: String, indexes: Map[String, Int], unique: Boolean = false, opts: Option[IndexOptions] = None)(implicit tt: TypeTag[E]): Task[Unit]

    def createTextIndex[E <: Id](collectionName: String, fields: List[String])(implicit tt: TypeTag[E]): Task[Unit]

    def createQueue(queueName: String, viqsibility: Option[Int] = None, delay: Option[Int] = None, maxRetries: Option[Int] = None): Task[Unit]

    def ackQueue[E <: Id](queueName: String, id: EntityId, visibility: Option[Int] = None)(implicit tt: TypeTag[E], d: Decoder[E]): Task[Option[EntityId]]

    def pingQueue[E <: Id](queueName: String, id: EntityId, visibility: Option[Int] = None)(implicit tt: TypeTag[E], d: Decoder[E]): Task[Option[EntityId]]

    def addToQueue[E <: Id](queueName: String, entities: List[E], delay: Option[Int] = None)(implicit tt: TypeTag[E], e: Encoder[E]): Task[List[EntityId]]

    def getFromQueue[E <: Id](queueName: String, visibility: Option[Int] = None)(implicit tt: TypeTag[Message[E]], d: Decoder[Message[E]]): Task[Option[Message[E]]]

    def countQueue[E <: Id](queueName: String, bson: Bson)(implicit tt: TypeTag[E]): Task[Long]

    def waitingCountForQueue[E <: Id](queueName: String)(implicit tt: TypeTag[E]): Task[Long]

    def totalCountForQueue[E <: Id](queueName: String)(implicit tt: TypeTag[E]): Task[Long]

    def inFlightCountForQueue[E <: Id](queueName: String)(implicit tt: TypeTag[E]): Task[Long]

    def completedCountForQueue[E <: Id](queueName: String)(implicit tt: TypeTag[E]): Task[Long]

    def purgeQueue[E <: Id](queueName: String)(implicit tt: TypeTag[E]): Task[Unit]
  }
}