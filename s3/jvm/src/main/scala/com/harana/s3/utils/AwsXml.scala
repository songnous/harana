package com.harana.s3.utils

import com.google.common.net.PercentEscaper
import com.harana.s3.services.server.models.StorageType.{FOLDER, RELATIVE_PATH}
import com.harana.s3.services.server.models._
import com.harana.s3.utils.DateTime.{formatDate, iso8601DateFormat}
import org.apache.commons.lang3.StringUtils
import software.amazon.awssdk.services.s3.model._

import java.time.Instant
import javax.xml.stream.XMLStreamWriter
import scala.collection.mutable

object AwsXml {

  private val awsXmlNs = "http://s3.amazonaws.com/doc/2006-03-01/"
  private val fakeOwnerId = "75aa57f09aa0c8caeab4f8c24e99d10f8e7faeebf76c078efc7c6caea54ba06a"
  private val fakeOwnerDisplayName = "CustomersName@amazon.com"
  private val fakeInitiatorId = "arn:aws:iam::111122223333:" + "user/some-user-11116a31-17b5-4fb7-9df5-b288870f11xx"
  private val fakeInitiatorDisplayName = "umat-user-11116a31-17b5-4fb7-9df5-b288870f11xx"
  private val fakeRequestId = "4442587FB7D0A2F9"
  private val urlEscaper = new PercentEscaper("*-./_", false)

  def writeAccessControlPolicy(xml: XMLStreamWriter, publicRead: Boolean = false) = {
    xml.writeStartDocument()
    xml.writeStartElement("AccessControlPolicy")
    xml.writeDefaultNamespace(awsXmlNs)
    writeOwnerStanza(xml)
    xml.writeStartElement("AccessControlList")
    xml.writeStartElement("Grant")
    xml.writeStartElement("Grantee")
    xml.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance")
    xml.writeAttribute("xsi:type", "CanonicalUser")
    writeSimpleElement(xml, "ID", fakeOwnerId)
    writeSimpleElement(xml, "DisplayName", fakeOwnerDisplayName)
    xml.writeEndElement()
    writeSimpleElement(xml, "Permission", "FULL_CONTROL")
    xml.writeEndElement()

    if (publicRead) {
      xml.writeStartElement("Grant")
      xml.writeStartElement("Grantee")
      xml.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance")
      xml.writeAttribute("xsi:type", "Group")
      writeSimpleElement(xml, "URI", "http://acs.amazonaws.com/groups/global/AllUsers")
      xml.writeEndElement()
      writeSimpleElement(xml, "Permission", "READ")
      xml.writeEndElement()
    }

    xml.writeEndElement()
    xml.writeEndElement()
    xml.flush()
  }


  def writeLocationConstraint(xml: XMLStreamWriter) = {
    xml.writeStartDocument()
    xml.writeStartElement("LocationConstraint")
    xml.writeDefaultNamespace(awsXmlNs)
    xml.writeEndElement()
    xml.flush()
  }


  def writeListAllMyBucketsResult(xml: XMLStreamWriter, buckets: List[Bucket]) = {
    xml.writeStartDocument()
    xml.writeStartElement("ListAllMyBucketsResult")
    xml.writeDefaultNamespace(awsXmlNs)
    writeOwnerStanza(xml)
    xml.writeStartElement("Buckets")

    for (bucket <- buckets) {
      xml.writeStartElement("Bucket")
      writeSimpleElement(xml, "Name", bucket.name)
      writeSimpleElement(xml, "CreationDate", iso8601DateFormat(bucket.creationDate()).trim)
      xml.writeEndElement()
    }

    xml.writeEndElement()
    xml.writeEndElement()
    xml.flush()
  }


  def writeDeleteResult(xml: XMLStreamWriter, quiet: Boolean, keys: List[String]) = {
    xml.writeStartDocument()
    xml.writeStartElement("DeleteResult")
    xml.writeDefaultNamespace(awsXmlNs)

    if (!quiet) {
      for (key <- keys) {
        xml.writeStartElement("Deleted")
        writeSimpleElement(xml, "Key", key)
        xml.writeEndElement()
      }
    }

    xml.writeEndElement()
    xml.flush()
  }


  def writeCopyObjectResult(xml: XMLStreamWriter, lastModified: Instant, eTag: String) = {
    xml.writeStartDocument()
    xml.writeStartElement("CopyObjectResult")
    xml.writeDefaultNamespace(awsXmlNs)
    writeSimpleElement(xml, "LastModified", formatDate(lastModified))
    writeSimpleElement(xml, "ETag", maybeQuoteETag(eTag))
    xml.writeEndElement()
    xml.flush()
  }


  def writeErrorResult(xml: XMLStreamWriter, errorCode: String, message: String, elements: Map[String, String]) = {
    xml.writeStartDocument()
    xml.writeStartElement("Error")
    writeSimpleElement(xml, "Code", errorCode)
    writeSimpleElement(xml, "Message", message)
    elements.foreach { case(k, v) => writeSimpleElement(xml, k, v) }
    writeSimpleElement(xml, "RequestId", fakeRequestId)
    xml.writeEndElement()
    xml.flush()
  }


  def writeInitiateMultipartUploadResult(xml: XMLStreamWriter, bucket: String, key: String, uploadId: String) = {
    xml.writeStartDocument()
    xml.writeStartElement("InitiateMultipartUploadResult")
    xml.writeDefaultNamespace(awsXmlNs)
    writeSimpleElement(xml, "Bucket", bucket)
    writeSimpleElement(xml, "Key", key)
    writeSimpleElement(xml, "UploadId", uploadId)
    xml.writeEndElement()
    xml.flush()
  }


  def writeCompleteMultipartUploadResult(xml: XMLStreamWriter, bucket: String, key: String, uploadId: String, eTag: Option[String]) = {
    xml.writeStartDocument()
    xml.writeStartElement("CompleteMultipartUploadResult")
    xml.writeDefaultNamespace(awsXmlNs)
    xml.flush()

// FIXME
//    while (thread.isAlive) {
//      try thread.join(1000)
//      catch {
//        case ie: InterruptedException =>
//
//
//        // ignore
//      }
//      writer.write("\n")
//      writer.flush
//    }
//
//    if (exception.get != null) {
//      throw exception.get
//    }

    writeSimpleElement(xml, "Location", "http://Example-Bucket.s3.amazonaws.com/" + key)
    writeSimpleElement(xml, "Bucket", bucket)
    writeSimpleElement(xml, "Key", key)
    if (eTag.nonEmpty) writeSimpleElement(xml, "ETag", maybeQuoteETag(eTag.get))
    xml.writeEndElement()
    xml.flush()
  }


  def writeListPartsResult(xml: XMLStreamWriter,
                           bucket: String,
                           key: String,
                           uploadId: String,
                           encodingType: String,
                           parts: List[Part]) = {
    xml.writeStartDocument()
    xml.writeStartElement("ListPartsResult")
    xml.writeDefaultNamespace(awsXmlNs)

    if (encodingType != null && encodingType.equals("url")) {
      writeSimpleElement(xml, "EncodingType", encodingType)
    }

    writeSimpleElement(xml, "Bucket", bucket)
    writeSimpleElement(xml, "Key", encodeBlob(encodingType, key))
    writeSimpleElement(xml, "UploadId", uploadId)
    writeInitiatorStanza(xml)
    writeOwnerStanza(xml)
    writeSimpleElement(xml, "StorageClass", "STANDARD")

    parts.foreach { part =>
      xml.writeStartElement("Part")
      writeSimpleElement(xml, "PartNumber", String.valueOf(part.partNumber))
      writeSimpleElement(xml, "LastModified", formatDate(part.lastModified))
      writeSimpleElement(xml, "ETag", maybeQuoteETag(part.eTag))
      writeSimpleElement(xml, "Size", part.size.toString)
      xml.writeEndElement()
    }

    xml.writeEndElement()
    xml.flush()
  }


  def writeListMultipartUploadsResult(xml: XMLStreamWriter,
                                      bucket: String,
                                      prefix: Option[String],
                                      encodingType: String,
                                      uploads: List[MultipartUpload]) = {
    xml.writeStartDocument()
    xml.writeStartElement("ListMultipartUploadsResult")
    xml.writeDefaultNamespace(awsXmlNs)
    writeSimpleElement(xml, "Bucket", bucket)

    xml.writeEmptyElement("KeyMarker")
    xml.writeEmptyElement("UploadIdMarker")
    xml.writeEmptyElement("NextKeyMarker")
    xml.writeEmptyElement("NextUploadIdMarker")
    xml.writeEmptyElement("Delimiter")

    if (prefix.isEmpty) xml.writeEmptyElement("Prefix") else writeSimpleElement(xml, "Prefix", encodeBlob(encodingType, prefix.get))
    writeSimpleElement(xml, "MaxUploads", "1000")
    writeSimpleElement(xml, "IsTruncated", "false")

    for (upload <- uploads) {
      xml.writeStartElement("Upload")
      writeSimpleElement(xml, "Key", upload.key)
      writeSimpleElement(xml, "UploadId", upload.uploadId())
      writeInitiatorStanza(xml)
      writeOwnerStanza(xml)

      writeSimpleElement(xml, "StorageClass", "STANDARD")
      writeSimpleElement(xml, "Initiated", iso8601DateFormat(java.time.Instant.now()))
      xml.writeEndElement()
    }

    xml.writeEndElement()
    xml.flush()
  }


  def writeListBucketResult(xml: XMLStreamWriter,
                            bucket: String,
                            prefix: Option[String],
                            s3Objects: List[S3Object],
                            encodingType: String,
                            isListV2: Boolean,
                            maxKeys: Int,
                            fetchOwner: Boolean,
                            marker: Option[String],
                            continuationToken: Option[String],
                            startAfter: Option[String],
                            delimiter: Option[String]) = {
    xml.writeStartDocument()
    xml.writeStartElement("ListBucketResult")
    xml.writeDefaultNamespace(awsXmlNs)
    writeSimpleElement(xml, "Name", bucket)

    if (prefix.isEmpty) xml.writeEmptyElement("Prefix") else writeSimpleElement(xml, "Prefix", encodeBlob(encodingType, prefix.get))
    if (isListV2) writeSimpleElement(xml, "KeyCount", String.valueOf(s3Objects.size))
    writeSimpleElement(xml, "MaxKeys", String.valueOf(maxKeys))

    if (!isListV2)
      if (marker.isEmpty) xml.writeEmptyElement("Marker") else writeSimpleElement(xml, "Marker", encodeBlob(encodingType, marker.get))
    else {
      if (continuationToken.isEmpty) xml.writeEmptyElement("ContinuationToken") else writeSimpleElement(xml, "ContinuationToken", encodeBlob(encodingType, continuationToken.get))
      if (startAfter.isEmpty) xml.writeEmptyElement("StartAfter") else writeSimpleElement(xml, "StartAfter", encodeBlob(encodingType, startAfter.get))
    }

    if (delimiter.nonEmpty) writeSimpleElement(xml, "Delimiter", encodeBlob(encodingType, delimiter.get))
    if (encodingType.equals("url")) writeSimpleElement(xml, "EncodingType", encodingType)

//    val nextMarker = s3Objects.nextMarker()
//    if (nextMarker != null) {
//      writeSimpleElement(xml, "IsTruncated", "true")
//      writeSimpleElement(xml, if (isListV2) "NextContinuationToken" else "NextMarker", encodeBlob(encodingType, nextMarker))
//    }
//    else
//      writeSimpleElement(xml, "IsTruncated", "false")

    writeSimpleElement(xml, "IsTruncated", "false")

    val commonPrefixes = mutable.Set.empty[String]

    for (s3Object <- s3Objects) {

      xml.writeStartElement("Contents")
      writeSimpleElement(xml, "Key", encodeBlob(encodingType, s3Object.key()))
      writeSimpleElement(xml, "LastModified", formatDate(s3Object.lastModified))
      writeSimpleElement(xml, "ETag", maybeQuoteETag(s3Object.eTag))
      writeSimpleElement(xml, "Size", String.valueOf(s3Object.size))
      writeSimpleElement(xml, "StorageClass", s3Object.storageClassAsString())
      if (fetchOwner) writeOwnerStanza(xml)
      xml.writeEndElement()

      // FIXME
//      if (s3Object.storageType.equals(RELATIVE_PATH))
//        commonPrefixes.add(s3Object.name)
    }

    for (commonPrefix <- commonPrefixes) {
      xml.writeStartElement("CommonPrefixes")
      writeSimpleElement(xml, "Prefix", encodeBlob(encodingType, commonPrefix))
      xml.writeEndElement()
    }

    xml.writeEndElement()
    xml.flush()
  }


  private def writeInitiatorStanza(xml: XMLStreamWriter) = {
    xml.writeStartElement("Initiator")
    writeSimpleElement(xml, "ID", fakeInitiatorId)
    writeSimpleElement(xml, "DisplayName", fakeInitiatorDisplayName)
    xml.writeEndElement()
  }


  private def writeOwnerStanza(xml: XMLStreamWriter) = {
    xml.writeStartElement("Owner")
    writeSimpleElement(xml, "ID", fakeOwnerId)
    writeSimpleElement(xml, "DisplayName", fakeOwnerDisplayName)
    xml.writeEndElement()
  }


  private def writeSimpleElement(xml: XMLStreamWriter, elementName: String, characters: String) = {
    xml.writeStartElement(elementName)
    xml.writeCharacters(if (characters == null) "" else characters)
    xml.writeEndElement()
  }


  private def encodeBlob(encodingType: String, blobName: String) =
    if (encodingType != null && encodingType == "url") urlEscaper.escape(blobName) else blobName


  private def maybeQuoteETag(eTag: String) =
    if (!eTag.startsWith("\"") && !eTag.endsWith("\"")) "\"" + eTag + "\"" else eTag

}
