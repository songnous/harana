package com.harana.modules.mongo

import org.mongodb.scala.bson.BsonDocument
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.mongo.Mongo.Service
import com.harana.modules.mongo.bson.codec.{OptionCodec, SomeCodec}
import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.Id
import com.harana.sdk.shared.utils.Random
import com.mongodb.BasicDBObject
import com.mongodb.MongoCredential._
import com.mongodb.connection.ClusterConnectionMode
import io.circe.{Decoder, Encoder}
import io.micrometer.core.instrument.binder.mongodb._
import net.petitviolet.ulid4s.ULID
import org.bson.codecs.configuration.CodecRegistries.{fromCodecs, fromRegistries}
import org.bson.BsonInt32
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.MongoCredential.createScramSha256Credential
import org.mongodb.scala.bson._
import org.mongodb.scala.bson.conversions._
import org.mongodb.scala.model.Sorts.{ascending, descending}
import org.mongodb.scala.model._
import org.mongodb.scala.result.{DeleteResult, UpdateResult}
import org.mongodb.scala.{ConnectionString, MongoClient, MongoClientSettings, MongoDatabase, Observer, ServerAddress}
import zio.{Task, ZLayer}

import java.util.Date
import java.util.concurrent.atomic.AtomicReference
import scala.jdk.CollectionConverters._
import scala.reflect.runtime.universe._

object LiveMongo {

  private val clientRef = new AtomicReference[Option[MongoClient]](None)

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    private def mongoDatabase: Task[MongoDatabase] =
      for {
        client            <- getClient
        uri               <- config.optSecret("mongodb-uri")
        name              <- config.optString("mongodb.database").map(_.getOrElse(uri.map(ConnectionString(_).getDatabase).getOrElse("admin")))
        codecRegistry     <- Task(fromRegistries(fromCodecs(new OptionCodec(), new SomeCodec()), DEFAULT_CODEC_REGISTRY))
        db                <- Task(client.getDatabase(name).withCodecRegistry(codecRegistry)).onError(e => logger.error(e.prettyPrint))
      } yield db


    private def getClient: Task[MongoClient] =
      for {
        client            <- if (clientRef.get.isDefined) Task(clientRef.get.get) else
                              for {
                                uri               <- config.optSecret("mongodb-uri")
                                srvHost           <- config.optString(s"mongodb.srvHost")
                                multiple          <- config.boolean(s"mongodb.multiple", false)
                                seeds             <- config.listString(s"mongodb.seeds", List())
                                username          <- config.optSecret("mongodb-username")
                                password          <- config.optSecret("mongodb-password")
                                passwordSource    <- config.optString("mongodb.passwordSource")
                                database          <- config.optString("mongodb.database")
                                userDatabase      <- config.optString("mongodb.userDatabase")
                                authType          <- config.optString("mongodb.authenticationType")
                                useSSL            <- config.boolean("mongodb.ssl", default = true)
                                _                 =  System.setProperty("org.mongodb.async.type", "netty")

                                metricsRegistry   <- micrometer.registry

                                baseBuilder       =  MongoClientSettings.builder()
                                                      .addCommandListener(new MongoMetricsCommandListener(metricsRegistry))
                                                      .applyToClusterSettings(b => {
                                                        b.mode(if (multiple) ClusterConnectionMode.MULTIPLE else ClusterConnectionMode.SINGLE)
                                                        if (seeds.nonEmpty) b.hosts(seeds.map(s => new ServerAddress(s"$s")).asJava)
                                                        if (srvHost.isDefined) b.srvHost(srvHost.get).mode(ClusterConnectionMode.MULTIPLE)
                                                        if (uri.isDefined) b.applyConnectionString(ConnectionString(uri.get))
                                                      })
                                                      .applyToConnectionPoolSettings(b => b.addConnectionPoolListener(new MongoMetricsConnectionPoolListener(metricsRegistry)))
                                                      .applyToSslSettings(b => b.enabled(useSSL))
                                builder           =  if (username.isDefined) baseBuilder.credential(credential(authType, username.get, userDatabase.getOrElse("admin"), password, passwordSource)) else baseBuilder
                                client            =  MongoClient(builder.build())
                                _                 <- logger.info(s"Connecting to MongoDB: ${client.getClusterDescription.toString}")
                                db                <- Task(client.getDatabase("admin")).onError(e => logger.error(e.prettyPrint))
                                _                 <- execute(db.runCommand(BsonDocument("ping" -> 1)))
                                _                 <- logger.info(s"Connected to MongoDB and successfully pinged.")
                              } yield client
        _                 =  clientRef.set(Some(client))
      } yield client


    private def credential(authType: Option[String], user: String, database: String, password: Option[String], passwordSource: Option[String]) = {
      authType.map(_.toLowerCase) match {
        case Some("scram-sha-256") => createScramSha256Credential(user, database, password.get.toCharArray)
        case Some("scram-sha-1") => createScramSha1Credential(user, database, password.get.toCharArray)
        // case Some("mongodb-cr" => createMongoCRCredential(user, database, mongoPassword)
        case Some("x.509") => createMongoX509Credential(user)
        case Some("kerberos") => createGSSAPICredential(user)
        case _ => createScramSha1Credential(user, database, password.get.toCharArray)
      }
    }


    def ping: Task[Unit] =
      for {
        client              <- getClient
        db                  <- Task(client.getDatabase("admin")).onError(e => logger.error(e.prettyPrint))
        _                   <- execute(db.runCommand(BsonDocument("ping" -> 1)))
      } yield ()


    def aggregate(collectionName: String, stages: List[Bson]): Task[List[BsonDocument]] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        results             <- execute(collection.aggregate(stages).collect()).map(_.toList)
      } yield results


    def get[E <: Id](collectionName: String, id: EntityId)(implicit tt: TypeTag[E], d: Decoder[E]): Task[Option[E]] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        results             <- executeGet(collection.find(byIdSelector(id)))
      } yield results


    def insert[E <: Id](collectionName: String, entity: E)(implicit tt: TypeTag[E], e: Encoder[E]): Task[Unit] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        bson                <- convertToBson(entity)
        results             <- execute(collection.insertOne(bson)).unit
      } yield results


    def insertMany[E <: Id](collectionName: String, entities: List[E])(implicit tt: TypeTag[E], e: Encoder[E]): Task[Unit] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        bsons               <- Task.foreach(entities)(convertToBson(_))
        results             <- execute(collection.insertMany(bsons)).unit
      } yield results


    def upsert[E <: Id](collectionName: String, entity: E)(implicit tt: TypeTag[E], e: Encoder[E]): Task[Unit] =
      replace(collectionName, entity.id, entity, upsert = true)


    def update[E <: Id](collectionName: String, entity: E)(implicit tt: TypeTag[E], e: Encoder[E]): Task[Unit] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        bson                <- convertToBson(entity)
        results             <- executeReplace(collection, entity.id, bson)
      } yield results


    def updateFields(collectionName: String, id: EntityId, keyValues: Map[String, Object]): Task[Unit] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        results             <- executeUpdate(collection, id, new BasicDBObject("$set", new BasicDBObject(keyValues.asJava)))
      } yield results


    def appendToListField(collectionName: String, id: EntityId, field: String, value: Object): Task[Unit] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        setDocument         =  new BasicDBObject(field, new BasicDBObject("$concat", List("$" + field, value)))
        results             <- executeUpdate(collection, id, new BasicDBObject("$set", setDocument))
      } yield results


    def replace[E <: Id](collectionName: String, id: EntityId, entity: E, upsert: Boolean)(implicit tt: TypeTag[E], e: Encoder[E]): Task[Unit] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        bson                <- convertToBson(entity)
        results             <- Task.effectAsync[Unit] { cb =>
                                collection.replaceOne(byIdSelector(entity.id), bson, ReplaceOptions().upsert(upsert)).subscribe(new Observer[UpdateResult] {
                                  def onNext(result: UpdateResult): Unit = cb(
                                    if (!result.wasAcknowledged()) Task.fail(new Exception("Result was not acknowledged"))
                                    else if (result.getMatchedCount == 0 && !upsert) Task.fail(new Exception("Entity not found"))
                                    else Task.fail(new Exception("Modification failure")).when(result.getModifiedCount == 0 && !upsert)
                                  )
                                  def onError(t: Throwable): Unit = cb(Task.fail(t))
                                  def onComplete(): Unit = cb(Task.unit)
                                })
        }
      } yield results


    def delete[E <: Id](collectionName: String, bson: Bson)(implicit tt: TypeTag[E]): Task[Unit] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        results             <- Task.effectAsync[Unit] { cb =>
          collection.deleteOne(bson).subscribe(new Observer[DeleteResult] {
            def onNext(result: DeleteResult): Unit = cb(
              if (!result.wasAcknowledged()) Task.fail(new Exception("Result was not acknowledged"))
              else Task.fail(new Exception("Entity not found")).when(result.getDeletedCount == 0)
            )
            def onError(t: Throwable): Unit = cb(Task.fail(t))
            def onComplete(): Unit = {}
          })
        }
      } yield results


    def delete[E <: Id](collectionName: String, id: EntityId)(implicit tt: TypeTag[E]): Task[Unit] =
      delete[E](collectionName, byIdSelector(id))


    def deleteEquals[E <: Id](collectionName: String, keyValues: Map[String, Object])(implicit tt: TypeTag[E]): Task[Unit] =
      delete[E](collectionName, new BasicDBObject(keyValues.asJava))


    def findEquals[E <: Id](collectionName: String, keyValues: Map[String, Object], sort: Option[(String, Boolean)] = None)(implicit tt: TypeTag[E], d: Decoder[E]): Task[List[E]] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        sortDoc             =  sort.map(s => if (s._2) ascending(s._1) else descending(s._1))
        findDoc             =  collection.find(new BasicDBObject(keyValues.asJava))
        results             <- executeFind[E](if (sort.isDefined) findDoc.sort(sortDoc.get) else findDoc)
      } yield results


    def find[E <: Id](collectionName: String, bson: Bson, sort: Option[(String, Boolean)] = None)(implicit tt: TypeTag[E], d: Decoder[E]): Task[List[E]] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        sortDoc             =  sort.map(s => if (s._2) ascending(s._1) else descending(s._1))
        findDoc             =  collection.find(bson)
        results             <- executeFind[E](if (sort.isDefined) findDoc.sort(sortDoc.get) else findDoc)
      } yield results


    def findOne[E <: Id](collectionName: String, keyValues: Map[String, Object], sort: Option[(String, Boolean)] = None)(implicit tt: TypeTag[E], d: Decoder[E]): Task[Option[E]] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        sortDoc             =  sort.map(s => if (s._2) ascending(s._1) else descending(s._1))
        query               =  new BasicDBObject(keyValues.asJava)
        result              <- executeGet[E](if (sort.isDefined) collection.find(query).sort(sortDoc.get).first() else collection.find(query).first())
      } yield result


    def findOneAndDelete[E <: Id](collectionName: String, keyValues: Map[String, Object], sort: Option[(String, Boolean)] = None)(implicit tt: TypeTag[E], d: Decoder[E]): Task[Option[E]] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        sortDoc             =  sort.map(s => if (s._2) ascending(s._1) else descending(s._1))
        query               =  new BasicDBObject(keyValues.asJava)
        options             =  FindOneAndDeleteOptions().sort(sortDoc.get)
        result              <- executeGet[E](if (sort.isDefined) collection.findOneAndDelete(query, options) else collection.findOneAndDelete(query))
      } yield result


    def distinctEquals(collectionName: String, field: String, keyValues: Map[String, Object]): Task[List[String]] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        results             <- execute(collection.distinct[String](field, new BasicDBObject(keyValues.asJava)).collect()).map(_.toList)
      } yield results


    def countEquals(collectionName: String, keyValues: Map[String, Object]): Task[Long] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        results             <- execute(collection.countDocuments(new BasicDBObject(keyValues.asJava)))
      } yield results


    def count(collectionName: String, bson: Bson): Task[Long] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        results             <- execute(collection.countDocuments(bson))
      } yield results


    def textSearch[E <: Id](collectionName: String, text: String)(implicit tt: TypeTag[E], d: Decoder[E]): Task[List[E]] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        results             <- executeFind[E](collection.find(Filters.text(text))
                                .projection(Projections.metaTextScore("score")).sort(Sorts.metaTextScore("score")))
      } yield results


    def textSearchFindEquals[E <: Id](collectionName: String, text: String, keyValues: Map[String, Object])(implicit tt: TypeTag[E], d: Decoder[E]): Task[List[E]] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        results             <- executeFind[E](collection.find(Filters.and(Filters.text(text), new BasicDBObject(keyValues.asJava)))
                                .projection(Projections.metaTextScore("score")).sort(Sorts.metaTextScore("score")))
      } yield results


    def all[E <: Id](collectionName: String, sort: Option[(String, Boolean)] = None)(implicit tt: TypeTag[E], d: Decoder[E]): Task[List[E]] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        sortDoc             =  sort.map(s => if (s._2) ascending(s._1) else descending(s._1))
        results             <- executeFind[E](if (sort.isDefined) collection.find().sort(sortDoc.get) else collection.find())
      } yield results


    def createIndex[E <: Id](collectionName: String, indexes: Map[String, Int], unique: Boolean = false, opts: Option[IndexOptions] = None)(implicit tt: TypeTag[E]): Task[Unit] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        keys                =  new BsonDocument()
        _                   =  indexes.foreach { case (k, v) => keys.append(k, new BsonInt32(v)) }
        results             <- Task.effectAsync[Unit] { cb =>
                                val options = if (opts.isDefined) opts.get else new IndexOptions()
                                val result = collection.createIndex(keys, options.unique(unique))

                                result.subscribe(new Observer[String] {
                                  override def onNext(result: String): Unit = cb(Task.succeed(()))
                                  override def onError(e: Throwable): Unit = cb(Task.fail(e))
                                  override def onComplete(): Unit = cb(Task.succeed(()))
                                })
                            }
      } yield results


    def createTextIndex[E <: Id](collectionName: String, fields: List[String])(implicit tt: TypeTag[E]): Task[Unit] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, collectionName)
        index               =  BsonDocument(fields.map(f => (f, BsonString("text"))))
        _                   <- execute(collection.createIndex(index))
      } yield ()


    def createQueue(queueName: String, visibility: Option[Int] = None, delay: Option[Int] = None, maxRetries: Option[Int] = None): Task[Unit] =
      for {
        db                  <- mongoDatabase
        _                   <- execute(db.createCollection(queueName))
        _                   <- createIndex(queueName, Map("deleted" -> 1, "visible" -> 1))
        _                   <- createIndex(queueName, Map("ack" -> 1), true, Some(IndexOptions().sparse(true)))
      } yield ()


    def ackQueue[E <: Id](queueName: String, id: EntityId, visibility: Option[Int] = None)(implicit tt: TypeTag[E], d: Decoder[E]): Task[Option[EntityId]] =
      for {
        db                  <- mongoDatabase
        queue               <- getCollection(db, queueName)
        query               =  BsonDocument(
                                "ack"     -> id,
                                "deleted" -> -1,
                                "visible" -> BsonDocument("$gt" -> new Date().getTime))
        update              =  BsonDocument("$set" -> BsonDocument("deleted" -> new Date().getTime))
        results             <- executeGet[E](queue.findOneAndUpdate(query, update, FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)))
      } yield results.map(_.id)


    def pingQueue[E <: Id](queueName: String, id: EntityId, visibility: Option[Int] = None)(implicit tt: TypeTag[E], d: Decoder[E]): Task[Option[EntityId]] =
      for {
        db                  <- mongoDatabase
        queue               <- getCollection(db, queueName)
        defaultVisibility   <- config.int("mongo.queueDefaultVisibility", 60)
        query               =  BsonDocument(
                                "ack" -> id,
                                "visible" -> BsonDocument("$gt" -> new Date().getTime),
                                "deleted" -> -1)
        update              =  BsonDocument(
                                "$set" -> BsonDocument("visible" -> nowWithSeconds(visibility.getOrElse(defaultVisibility))))
        results             <- executeGet[E](queue.findOneAndUpdate(query, update, FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)))
      } yield results.map(_.id)


    def addToQueue[E <: Id](queueName: String, entities: List[E], delay: Option[Int] = None)(implicit tt: TypeTag[E], e: Encoder[E]): Task[List[EntityId]] = {
      for {
        db                  <- mongoDatabase
        queue               <- getCollection(db, queueName)
        documents           =  entities.map { entity =>
                                  Message(
                                    id = Random.long,
                                    visible = Some(delay.map(nowWithSeconds).getOrElse(new Date())),
                                    payload = entity
                                  )
                                }
        bsons               <- Task.foreach(documents)(convertToBson(_))
        _                   <- if (entities.isEmpty) Task(List()) else execute(queue.insertMany(bsons))
      } yield documents.map(_.id)
    }


    def getFromQueue[E <: Id](queueName: String, visibility: Option[Int] = None)(implicit tt: TypeTag[Message[E]], d: Decoder[Message[E]]): Task[Option[Message[E]]] =
      for {
        db                  <- mongoDatabase
        queue               <- getCollection(db, queueName)
        defaultVisibility   <- config.int("mongo.queueDefaultVisibility", 60)
        query               = BsonDocument(
                                "visible" -> BsonDocument("$lte" -> new Date().getTime),
                                "deleted" -> -1
                              )
        update              = BsonDocument(
                                "$inc" -> BsonDocument("tries" -> 1),
                                "$set" -> BsonDocument(
                                  "ack" -> ULID.generate,
                                  "visible" -> nowWithSeconds(visibility.getOrElse(defaultVisibility)).getTime
                                )
                              )
        options             =  FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER).sort(BsonDocument("id" -> 1))
        results             <- executeGet(queue.findOneAndUpdate(query, update, options))
      } yield results


    def countQueue[E <: Id](queueName: String, bson: Bson)(implicit tt: TypeTag[E]): Task[Long] =
      for {
        db                  <- mongoDatabase
        collection          <- getCollection(db, queueName)
        count               <- execute(collection.countDocuments(bson))
      } yield count


    def waitingCountForQueue[E <: Id](queueName: String)(implicit tt: TypeTag[E]): Task[Long] = {
      val query = BsonDocument(
        "deleted" -> -1,
        "visible" -> BsonDocument("$lte" -> new Date().getTime)
      )
      countQueue(queueName, query)
    }


    def inFlightCountForQueue[E <: Id](queueName: String)(implicit tt: TypeTag[E]): Task[Long] = {
      val query = BsonDocument(
        "ack"     -> BsonDocument("$exists" -> true),
        "deleted" -> -1,
        "visible" -> BsonDocument("$gt" -> new Date().getTime)
      )
      countQueue(queueName, query)
    }


    def completedCountForQueue[E <: Id](queueName: String)(implicit tt: TypeTag[E]): Task[Long] = {
      val query = BsonDocument(
        "deleted" -> BsonDocument("$exists" -> true)
      )
      countQueue(queueName, query)
    }


    def purgeQueue[E <: Id](queueName: String)(implicit tt: TypeTag[E]): Task[Unit] = {
      val query = BsonDocument(
        "deleted" -> BsonDocument("$exists" -> true)
      )
      for {
        db          <- mongoDatabase
        collection  <- getCollection(db, queueName)
        results     <- execute(collection.deleteMany(query)).unit
      } yield results
    }


    def totalCountForQueue[E <: Id](queueName: String)(implicit tt: TypeTag[E]): Task[Long] =
      for {
        db          <- mongoDatabase
        collection  <- getCollection(db, queueName)
        count       <- execute(collection.countDocuments())
      } yield count

  }}
}