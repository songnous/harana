package com.harana.s3.utils

import com.harana.s3.services.models.StorageType.{FOLDER, RELATIVE_PATH}
import com.harana.s3.services.models._
import com.harana.s3.services.urlEscaper
import com.harana.s3.utils.DateTime.{formatDate, iso8601DateFormat}

import java.util.Date
import javax.xml.stream.XMLStreamWriter
import scala.collection.mutable
import scala.util.control.Breaks.{break, breakable}

object AwsXml {

  private val awsXmlNs = "http://s3.amazonaws.com/doc/2006-03-01/"
  private val fakeOwnerId = "75aa57f09aa0c8caeab4f8c24e99d10f8e7faeebf76c078efc7c6caea54ba06a"
  private val fakeOwnerDisplayName = "CustomersName@amazon.com"
  private val fakeInitiatorId = "arn:aws:iam::111122223333:" + "user/some-user-11116a31-17b5-4fb7-9df5-b288870f11xx"
  private val fakeInitiatorDisplayName = "umat-user-11116a31-17b5-4fb7-9df5-b288870f11xx"
  private val fakeRequestId = "4442587FB7D0A2F9"

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
      val creationDate = if (bucket.creationDate == null) new Date(0) else bucket.creationDate
      writeSimpleElement(xml, "CreationDate", iso8601DateFormat(creationDate).trim)
      xml.writeEndElement()
    }

    xml.writeEndElement()
    xml.writeEndElement()
    xml.flush()
  }


  def writeDeleteResult(xml: XMLStreamWriter, quiet: Boolean, blobNames: Set[String]) = {
    xml.writeStartDocument()
    xml.writeStartElement("DeleteResult")
    xml.writeDefaultNamespace(awsXmlNs)

    if (!quiet) {
      for (blobName <- blobNames) {
        xml.writeStartElement("Deleted")
        writeSimpleElement(xml, "Key", blobName)
        xml.writeEndElement()
      }
    }

    xml.writeEndElement()
    xml.flush()
  }


  def writeCopyObjectResult(xml: XMLStreamWriter, lastModified: Long, eTag: String) = {
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


  def writeInitiateMultipartUploadResult(xml: XMLStreamWriter, container: String, key: String, uploadId: String) = {
    xml.writeStartDocument()
    xml.writeStartElement("InitiateMultipartUploadResult")
    xml.writeDefaultNamespace(awsXmlNs)
    writeSimpleElement(xml, "Bucket", container)
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
    if (eTag.isDefined) writeSimpleElement(xml, "ETag", maybeQuoteETag(eTag.get))
    xml.writeEndElement()
    xml.flush()
  }


  def writeListMultipartUploadsResult(xml: XMLStreamWriter,
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

    for (part <- parts) {
      xml.writeStartElement("Part")
      writeSimpleElement(xml, "PartNumber", String.valueOf(part.number))
      if (part.lastModified.isDefined) writeSimpleElement(xml, "LastModified", formatDate(part.lastModified.get))
      if (part.eTag.isDefined) writeSimpleElement(xml, "ETag", maybeQuoteETag(part.eTag.get))
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
      if (prefix.isEmpty || (prefix.isDefined && upload.key.startsWith(prefix.get))) {
        xml.writeStartElement("Upload")
        writeSimpleElement(xml, "Key", upload.key)
        writeSimpleElement(xml, "UploadId", upload.id)
        writeInitiatorStanza(xml)
        writeOwnerStanza(xml)

        writeSimpleElement(xml, "StorageClass", "STANDARD")
        writeSimpleElement(xml, "Initiated", iso8601DateFormat(new Date()))
        xml.writeEndElement()
      }
    }
    xml.writeEndElement()
    xml.flush()
  }


  def writeListBucketResult(xml: XMLStreamWriter,
                            bucket: String,
                            prefix: Option[String],
                            items: PageSet[Item],
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
    if (isListV2) writeSimpleElement(xml, "KeyCount", String.valueOf(items.size))
    writeSimpleElement(xml, "MaxKeys", String.valueOf(maxKeys))

    if (!isListV2)
      if (marker.isEmpty) xml.writeEmptyElement("Marker") else writeSimpleElement(xml, "Marker", encodeBlob(encodingType, marker.get))
    else {
      if (continuationToken.isEmpty) xml.writeEmptyElement("ContinuationToken") else writeSimpleElement(xml, "ContinuationToken", encodeBlob(encodingType, continuationToken.get))
      if (startAfter.isEmpty) xml.writeEmptyElement("StartAfter") else writeSimpleElement(xml, "StartAfter", encodeBlob(encodingType, startAfter.get))
    }

    if (delimiter.isDefined) writeSimpleElement(xml, "Delimiter", encodeBlob(encodingType, delimiter.get))
    if (encodingType.equals("url")) writeSimpleElement(xml, "EncodingType", encodingType)

    val nextMarker = items.nextMarker()
    if (nextMarker != null) {
      writeSimpleElement(xml, "IsTruncated", "true")
      writeSimpleElement(xml, if (isListV2) "NextContinuationToken" else "NextMarker", encodeBlob(encodingType, nextMarker))
    }
    else
      writeSimpleElement(xml, "IsTruncated", "false")

    val commonPrefixes = mutable.Set.empty[String]
    breakable {
      for (item <- items) {
        if (item.storageType.equals(FOLDER)) {
          xml.writeStartElement("Contents")
          writeSimpleElement(xml, "Key", encodeBlob(encodingType, item.name))
          if (item.lastModifiedDate.isDefined) writeSimpleElement(xml, "LastModified", formatDate(item.lastModifiedDate.get))
          if (item.eTag.isDefined) writeSimpleElement(xml, "ETag", maybeQuoteETag(item.eTag.get))
          writeSimpleElement(xml, "Size", String.valueOf(item.size))
          writeSimpleElement(xml, "StorageClass", item.storageClass.name())
          if (fetchOwner) writeOwnerStanza(xml)
          xml.writeEndElement()
        }

        if (item.storageType.equals(RELATIVE_PATH))
          commonPrefixes.add(item.name)

        if (item.storageType != FOLDER || item.storageType != RELATIVE_PATH)
          break;
      }
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
    xml.writeCharacters(characters)
    xml.writeEndElement()
  }


  private def encodeBlob(encodingType: String, blobName: String) =
    if (encodingType != null && encodingType == "url") urlEscaper.escape(blobName) else blobName


  private def maybeQuoteETag(eTag: String) =
    if (!eTag.startsWith("\"") && !eTag.endsWith("\"")) "\"" + eTag + "\"" else eTag

}
