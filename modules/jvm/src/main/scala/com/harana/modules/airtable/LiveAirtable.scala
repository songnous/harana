package com.harana.modules.airtable

import java.util
import java.util.concurrent.atomic.AtomicReference
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.airtable.Airtable.Service
import com.harana.modules.mongo.LiveMongo.clientRef
import com.harana.modules.core.micrometer.Micrometer
import dev.fuxing.airtable.{AirtableApi, AirtableRecord}
import dev.fuxing.airtable.AirtableTable.{PaginationList, QuerySpec}
import org.mongodb.scala.MongoClient
import zio.{Task, UIO, ZLayer}

import scala.jdk.CollectionConverters._

object LiveAirtable {

  private val clientRef = new AtomicReference[Option[AirtableApi]](None)

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    private def client =
      for {
        client            <- if (clientRef.get.nonEmpty) Task(clientRef.get.get) else
                            for {
                              key             <- config.secret("airtable-key")
                              api             <- Task(new AirtableApi(key))
                            } yield api
        _                 =  clientRef.set(Some(client))
      } yield client


    def tableIterator(base: String, table: String, query: QuerySpec): Task[util.Iterator[AirtableRecord]] =
      for {
        api       <- client
        iterator  =  api.base(base).table(table).iterator(query)
      } yield iterator


    def listTable(base: String, table: String, query: QuerySpec): Task[PaginationList] =
      for {
        api       <- client
        list      <- Task(api.base(base).table(table).list(query))
      } yield list


    def getRecord(base: String, table: String, id: String): Task[AirtableRecord] =
      for {
        api       <- client
        record    =  api.base(base).table(table).get(id)
      } yield record


    def createRecords(base: String, table: String, records: List[AirtableRecord]): Task[Unit] =
      for {
        api       <- client
        _         <- Task(api.base(base).table(table).post(records.asJava))
      } yield ()


    def patchRecords(base: String, table: String, records: List[AirtableRecord]): Task[Unit] =
      for {
        api       <- client
        _  <- Task(api.base(base).table(table).patch(records.asJava))
      } yield ()


    def replaceRecords(base: String, table: String, records: List[AirtableRecord]): Task[Unit] =
      for {
        api       <- client
        _         <- Task(api.base(base).table(table).put(records.asJava))
      } yield ()


    def deleteRecords(base: String, table: String, recordIds: List[String]): Task[Unit] =
      for {
        api       <- client
        _         <- Task(api.base(base).table(table).delete(recordIds.asJava))
      } yield ()

  }}
}