package com.harana.designer.backend.services

import java.io.{File, FileReader}

import com.harana.designer.shared.PreviewData
import com.opencsv.CSVReader
import org.apache.avro.Schema
import org.apache.avro.file.DataFileReader
import org.apache.avro.generic.{GenericDatumReader, GenericRecord}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.parquet.example.data.simple.SimpleGroup
import org.apache.parquet.example.data.simple.convert.GroupRecordConverter
import org.apache.parquet.hadoop.ParquetFileReader
import org.apache.parquet.hadoop.util.HadoopInputFile
import org.apache.parquet.io.ColumnIOFactory
import zio.Task
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.JulianFields

import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer
import scala.util.Try

package object files {

  def previewAvro(file: File, maximumRows: Int): Task[PreviewData] =
    for {
      datumReader       <- Task(new GenericDatumReader[GenericRecord]())
      fileReader        <- Task(DataFileReader.openReader(file, datumReader))
      records           <- Task(fileReader.iterator().asScala.take(maximumRows).toList)
      schema            =  records.head.getSchema
      data              =  toPreviewData(schema, records)
    } yield data


  def previewCsv(file: File, maximumRows: Int): Task[PreviewData] =
    for {
      reader            <- Task(new CSVReader(new FileReader(file)))
      records           <- Task(reader.iterator().asScala.take(maximumRows).toList)
      data              =  PreviewData(records.head.toList, records.slice(1, records.size).map(_.toList))
    } yield data


  def previewParquet(file: File, maximumRows: Int): Task[PreviewData] = Task {
    val reader = ParquetFileReader.open(HadoopInputFile.fromPath(new Path(file.getAbsolutePath), new Configuration()))
    val schema = reader.getFooter.getFileMetaData.getSchema
    val groups = new ListBuffer[SimpleGroup]()
    var totalRows = 0

    val pages = Iterator.continually(reader.readNextRowGroup).takeWhile(_ != null)
    pages.foreach { page =>
      val columnIO = new ColumnIOFactory().getColumnIO(schema)
      val recordReader = columnIO.getRecordReader(page, new GroupRecordConverter(schema))
      val count = Math.min(page.getRowCount, maximumRows).toInt
      for (_ <- 0 until count) groups += recordReader.read().asInstanceOf[SimpleGroup]
      totalRows += page.getRowCount.toInt
    }

    val data = groups.toList.map { group =>
      val fields = new ListBuffer[String]()
      for (i <- 0 until schema.getColumns.size) {
        try {
          if (isInt96(group, i)) fields += toTimestamp(group.getInt96(i, 0).getBytes).toString
          else fields += group.getValueToString(i, 0)
        } catch {
          case e: Exception => fields += ""
        }
      }
      fields.toList
    }

    PreviewData(schema.getFields.asScala.toList.map(_.getName), data)
  }

  private def isInt96(group: SimpleGroup, fieldIndex: Int) =
    group.getType.getType(fieldIndex).asPrimitiveType.getPrimitiveTypeName.equals(PrimitiveTypeName.INT96)

  private def toTimestamp(int96Bytes: Array[Byte]) = {
    var julianDay = 0
    var index = int96Bytes.length
    while (index > 8) {
      index -= 1
      julianDay <<= 8
      julianDay += int96Bytes(index) & 0xFF
    }

    var nanos = 0
    while (index > 0) {
      index -= 1
      nanos <<= 8
      nanos += int96Bytes(index) & 0xFF
    }

    LocalDate.MIN.`with`(JulianFields.JULIAN_DAY, julianDay).atTime(LocalTime.NOON).plusNanos(nanos)
  }

  private def toPreviewData(schema: Schema, records: List[GenericRecord]) = {
    val headers = schema.getFields.asScala.toList.map(_.name())
    val rows = records.map { r => headers.map(h => Try(r.get(h).toString).getOrElse("")) }
    PreviewData(headers, rows)
  }
}