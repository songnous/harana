package com.harana.s3.services.s3_server.models

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlElementWrapper, JacksonXmlProperty}

case class CompleteMultipartUploadRequest(@JacksonXmlProperty(localName = "Part")
                                          @JacksonXmlElementWrapper(useWrapping = false)
                                          parts: List[Part])

case class Part(@JacksonXmlProperty(localName = "PartNumber") partNumber: Int,
                @JacksonXmlProperty(localName = "ETag") eTag: String)