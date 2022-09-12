package com.harana.modules.airtable

import java.util

import dev.fuxing.airtable.AirtableRecord
import dev.fuxing.airtable.AirtableTable.{PaginationList, QuerySpec}
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object Airtable {
  type Airtable = Has[Airtable.Service]

  trait Service {

    def tableIterator(base: String, table: String, query: QuerySpec): Task[util.Iterator[AirtableRecord]]

    def listTable(base: String, table: String, query: QuerySpec): Task[PaginationList]

    def getRecord(base: String, table: String, id: String): Task[AirtableRecord]

    def createRecords(base: String, table: String, records: List[AirtableRecord]): Task[Unit]

    def patchRecords(base: String, table: String, records: List[AirtableRecord]): Task[Unit]

    def replaceRecords(base: String, table: String, records: List[AirtableRecord]): Task[Unit]

    def deleteRecords(base: String, table: String, recordIds: List[String]): Task[Unit]

  }
}